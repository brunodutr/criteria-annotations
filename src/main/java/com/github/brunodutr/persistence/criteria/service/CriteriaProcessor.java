package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaOrderBy;

public class CriteriaProcessor<T> {

	private CriteriaBuilder criteriaBuilder;
	private Root<T> root;
	private Object object;
	private CriteriaQuery<T> criteriaQuery;
	private EntityManager entityManager;
	
	private Predicate[] where() {

		Field[] fields = object.getClass().getDeclaredFields();

		return Stream.of(fields).map(field -> {

			try {

				ICriteriaProcessor processor = CriteriaProcessorFactory.getProcessor(field);
				
				if(processor != null) {
					return processor.process(criteriaBuilder, root, object, field);
				} else {
					return null;
				}
							
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}).filter(Objects::nonNull).toArray(Predicate[]::new);

	}

	private Order[] orderBy() {

		if (object.getClass().isAnnotationPresent(CriteriaOrderBy.class)) {

			CriteriaOrderBy criteriaOrderBy = object.getClass().getAnnotation(CriteriaOrderBy.class);

			String sort = criteriaOrderBy.sort();

			if ((!sort.equalsIgnoreCase("asc")) && (!sort.equalsIgnoreCase("desc"))) {
				throw new IllegalArgumentException("Sort deve ser 'asc' ou 'desc'");
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

	private CriteriaProcessor(EntityManager entityManager, Class<T> entityClass) {
		super();
		this.entityManager = entityManager;
		this.criteriaBuilder = entityManager.getCriteriaBuilder();
		this.criteriaQuery = criteriaBuilder.createQuery(entityClass);
		this.root = criteriaQuery.from(entityClass);
	}
	
	public static <T> CriteriaProcessor<T> create(EntityManager entityManager, Class<T> entityClass) {
		return new CriteriaProcessor<T>(entityManager, entityClass);
	}
	
	public CriteriaProcessor<T> filter(Object filter){
		this.object = filter;
		return this;
	}
	
	public List<T> getResultList() {

		criteriaQuery.where(where());
		criteriaQuery.orderBy(orderBy());
		criteriaQuery.select(root);
		
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		
		return query.getResultList();
	}
	
	

}
