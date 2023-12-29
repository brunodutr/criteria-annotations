package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaLike;

class CriteriaProcessorLike implements ICriteriaProcessor {

	@SuppressWarnings("unchecked")
	@Override
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
		return processAnnotation(root, object, field, (path, value) ->  {
			
			if (!(value instanceof String texto)) {
				throw new UnsupportedOperationException(formatException(String.class, field));
			}
			
			CriteriaLike criteriaLike = field.getAnnotation(CriteriaLike.class);

			texto = criteriaLike.start() + texto + criteriaLike.end();
			
			Expression<String> pathString = (Expression<String>) path;

			return criteriaBuilder.like(pathString, texto);
		});
	}


}
