package com.github.brunodutr.persistence.criteria.service;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaGreaterThan;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;

class CriteriaProcessorGreaterThan implements ICriteriaProcessor {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
		
		return processAnnotation(root, object, field, (path, value) -> {

			if (!(value instanceof Comparable comparable)) {
				throw new UnsupportedOperationException(formatException(Comparable.class, field));
			}
			
			Expression<Comparable> expression = (Expression<Comparable>) path;

			CriteriaGreaterThan criteriaGreaterThan = field.getAnnotation(CriteriaGreaterThan.class);

			if (criteriaGreaterThan.equal()) {
				
				return criteriaBuilder.<Comparable>greaterThanOrEqualTo(expression, comparable);
				
			} else {
				
				return criteriaBuilder.<Comparable>greaterThan(expression, comparable);
				
			}
		});

	}

}
