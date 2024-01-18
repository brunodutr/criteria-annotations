package com.github.brunodutr.persistence.criteria.service;

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
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CriteriaProcessor<T> {

	private CriteriaBuilder criteriaBuilder;
	private Root<T> root;
	private Object object;
	private Integer page;
	private Integer size;
	private CriteriaQuery<T> criteriaQuery;
	private CriteriaQuery<Long> countCriteriaQuery;
	private EntityManager entityManager;
	
	private Predicate[] where() {

		Field[] fields = object.getClass().getDeclaredFields();

		return Stream.of(fields).map(field -> {

			try {

				ICriteriaProcessor processor = CriteriaProcessorFactory.getProcessor(field);
				
				if (processor != null) {
					return processor.process(criteriaBuilder, root, object, field);
				} else {
					return null;
				}
							
			} catch (Exception e) {
				throw new RuntimeException("Error processing field " + field.getName(), e);
			}

		}).filter(Objects::nonNull).toArray(Predicate[]::new);

	}


	private Order[] orderBy() {

		if (object.getClass().isAnnotationPresent(CriteriaOrderBy.class)) {

			CriteriaOrderBy criteriaOrderBy = object.getClass().getAnnotation(CriteriaOrderBy.class);

			String sort = criteriaOrderBy.sort();

			if ((!sort.equalsIgnoreCase("asc")) && (!sort.equalsIgnoreCase("desc"))) {
				throw new IllegalArgumentException("Sort value must be asc or desc.");
			}

			return Stream.of(criteriaOrderBy.columns()).filter(StringUtils::isNotBlank).map(column -> {

				Path<Object> path = root.get(column);

				if (sort.equals("asc")) {
					return criteriaBuilder.asc(path);
				} else {
					return criteriaBuilder.desc(path);
				}

			}).toArray(Order[]::new);

		}

		return new Order[] {};

	}

	private CriteriaProcessor(final EntityManager entityManager, final Class<T> entityClass) {
		super();
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
		this.criteriaQuery = criteriaBuilder.createQuery(entityClass);
		this.countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		this.root = criteriaQuery.from(entityClass);
	}
	
	public static <T> CriteriaProcessor<T> create(final EntityManager entityManager, final Class<T> entityClass) {
		return new CriteriaProcessor<T>(entityManager, entityClass);
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

		criteriaQuery.where(where());
		criteriaQuery.orderBy(orderBy());
		criteriaQuery.select(root);

		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

		if (page != null && size != null) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}

		return query.getResultList();
	}

	public Long count() {

		countCriteriaQuery.where(where());
		countCriteriaQuery.select(criteriaBuilder.count(root));

		TypedQuery<Long> query = entityManager.createQuery(countCriteriaQuery);

		return query.getSingleResult();

	}
	

}
