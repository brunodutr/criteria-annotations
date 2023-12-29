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
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		return processAnnotation(root, object, field, (path, value) ->  {
			
			if (!(value instanceof String)) {
				throw new IllegalArgumentException(
						"CriteriaLike funciona apenas para o tipo java.lang.String");
			}
			
			CriteriaLike criteriaLike = field.getAnnotation(CriteriaLike.class);
			
			String texto = (String) value;
			
			texto = criteriaLike.start() + texto + criteriaLike.end();
			
			Expression<String> pathString = (Expression<String>) path;

			return criteriaBuilder.like(pathString, texto);
		});
	}


}
