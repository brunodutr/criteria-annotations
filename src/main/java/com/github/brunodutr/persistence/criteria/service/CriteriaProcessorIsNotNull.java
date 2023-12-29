package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorIsNotNull implements ICriteriaProcessor {

	@Override
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
		return processAnnotation(root, object, field, (path, value) -> { 
			
			if (value == null) {
				return null;
			}
			
			if (value instanceof Boolean isNotNull) {

				return isNotNull ? criteriaBuilder.isNotNull(path) : null;
				
			} else {
				throw new UnsupportedOperationException(formatException(Boolean.class, field));
			}
			
		});
	}


}
