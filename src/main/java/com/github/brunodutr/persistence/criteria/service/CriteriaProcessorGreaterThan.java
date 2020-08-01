package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaGreaterThan;

class CriteriaProcessorGreaterThan implements ICriteriaProcessor {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		
		return processAnnotation(root, object, field, (path, value) -> {

			if(!(value instanceof Comparable)) {
				throw new UnsupportedOperationException("CriteriaProcessorGreaterThan funciona apenas para tipos que extendem java.lang.Comparable");
			}
			
			Expression<Comparable> expression = (Expression<Comparable>) path;
			Comparable comparable = (Comparable) value;
			
			CriteriaGreaterThan criteriaGreaterThan = field.getAnnotation(CriteriaGreaterThan.class);

			if(criteriaGreaterThan.equal()) {
				
				return criteriaBuilder.<Comparable>greaterThanOrEqualTo(expression, comparable);
				
			} else {
				
				return criteriaBuilder.<Comparable>greaterThan(expression, comparable);
				
			}
		});

	}

}
