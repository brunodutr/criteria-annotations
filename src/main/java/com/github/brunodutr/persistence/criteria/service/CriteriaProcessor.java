package com.github.brunodutr.persistence.criteria.service;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaOperator;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaOperators;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaOrderBy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CriteriaProcessor<T> {

	private final EntityManager entityManager;
	private final Class<T> entityClass;
	private Object object;
	private Integer page;
	private Integer size;

	private record CriteriaProcessorResult(String fieldName, Predicate predicate) { }

	private CriteriaProcessor(final EntityManager entityManager, final Class<T> entityClass) {
		super();
		this.entityManager = entityManager;
		this.entityClass = entityClass;
	}

	public static <T> CriteriaProcessor<T> create(final EntityManager entityManager, final Class<T> entityClass) {
		return new CriteriaProcessor<>(entityManager, entityClass);
	}

	private Predicate[] where(final CriteriaBuilder criteriaBuilder, final Root<T> root) {

		Field[] fields = object.getClass().getDeclaredFields();

		Set<String> fieldsToRemoveFromMap = new HashSet<>();
		Map<String, Predicate> mapOfPredicates = Stream.of(fields).map(field -> {

			try {

				ICriteriaProcessor processor = CriteriaProcessorFactory.getProcessor(field);

				if (processor != null) {

					Predicate predicate = processor.process(criteriaBuilder, root, object, field);

					if (predicate != null) {
						return new CriteriaProcessorResult(field.getName(), predicate);
					} else {
						return null;
					}
				} else {
					return null;
				}

			} catch (Exception e) {
				throw new RuntimeException("Error processing field " + field.getName(), e);
			}

		}).filter(Objects::nonNull).collect(Collectors.toMap(CriteriaProcessorResult::fieldName, CriteriaProcessorResult::predicate));

		// verify if object has CriteriaOperator or CriteriaOperators annotations
		if (object.getClass().isAnnotationPresent(CriteriaOperator.class) && object.getClass().isAnnotationPresent(CriteriaOperators.class)) {
			throw new IllegalArgumentException("Cannot have both CriteriaOperator and CriteriaOperators annotations.");
		} else if (object.getClass().isAnnotationPresent(CriteriaOperators.class)) {

			CriteriaOperators criteriaOperators = object.getClass().getAnnotation(CriteriaOperators.class);

			if (criteriaOperators.value() == null) {
				throw new IllegalArgumentException("CriteriaOperators value cannot be null.");
			}

			if (criteriaOperators.value().length == 0) {
				throw new IllegalArgumentException("CriteriaOperators value cannot be empty.");
			}

			// iterate over CriteriaOperator values
			Arrays.stream(criteriaOperators.value()).forEach(criteriaOperator -> {

				Predicate criteriaOperatorPredicate = CriteriaProcessorOperator.process(criteriaBuilder, mapOfPredicates, criteriaOperator);

				// mark fields to remove from map
                fieldsToRemoveFromMap.addAll(Arrays.asList(criteriaOperator.value()));

				// add CriteriaOperator predicate to map with uuid key
				mapOfPredicates.put(UUID.randomUUID().toString(), criteriaOperatorPredicate);

			});

		} else if (object.getClass().isAnnotationPresent(CriteriaOperator.class)) {

			CriteriaOperator criteriaOperator = object.getClass().getAnnotation(CriteriaOperator.class);

			Predicate criteriaOperatorPredicate = CriteriaProcessorOperator.process(criteriaBuilder, mapOfPredicates, criteriaOperator);

			// mark fields to remove from map
			fieldsToRemoveFromMap.addAll(Arrays.asList(criteriaOperator.value()));

			// add CriteriaOperator predicate to map with uuid key
			mapOfPredicates.put(UUID.randomUUID().toString(), criteriaOperatorPredicate);

		}

		// remove fields from map
		fieldsToRemoveFromMap.forEach(mapOfPredicates::remove);

		return mapOfPredicates.values().stream().filter(Objects::nonNull).toArray(Predicate[]::new);
	}


	private Order[] orderBy(final CriteriaBuilder criteriaBuilder, final Root<T> root) {

		if (object.getClass().isAnnotationPresent(CriteriaOrderBy.class)) {

			CriteriaOrderBy criteriaOrderBy = object.getClass().getAnnotation(CriteriaOrderBy.class);

			String sort = criteriaOrderBy.sort();

			if ((!sort.equalsIgnoreCase("asc")) && (!sort.equalsIgnoreCase("desc"))) {
				throw new IllegalArgumentException("Sort value must be asc or desc.");
			}

			return Stream.of(criteriaOrderBy.columns()).filter(StringUtils::isNotBlank).map(column -> {

				Path<?> path = CriteriaUtils.getPath(root, column);

				if (sort.equals("asc")) {
					return criteriaBuilder.asc(path);
				} else {
					return criteriaBuilder.desc(path);
				}

			}).toArray(Order[]::new);

		}

		return new Order[] {};

	}
	
	public CriteriaProcessor<T> filter(final Object filter) {
		this.object = filter;
		return this;
	}

	public CriteriaProcessor<T> paginate(final int page, final int size) {

		if (page < 0) {
			throw new IllegalArgumentException("Page must be greater than or equal to zero.");
		}

		if (size < 1) {
			throw new IllegalArgumentException("Size must be greater than zero.");
		}

		this.page = page;
		this.size = size;

		return this;
	}
	
	public List<T> getResultList() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);

		criteriaQuery.where(where(criteriaBuilder, root));
		criteriaQuery.orderBy(orderBy(criteriaBuilder, root));
		criteriaQuery.select(root);

		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

		if (page != null && size != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}

		return query.getResultList();
	}

	public Long count() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> countRoot = countCriteriaQuery.from(entityClass);

		countCriteriaQuery.where(where(criteriaBuilder, countRoot));
		countCriteriaQuery.select(criteriaBuilder.count(criteriaBuilder.literal(1)));

		TypedQuery<Long> query = entityManager.createQuery(countCriteriaQuery);

		return query.getSingleResult();

	}
	

}
