package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorIsNotNull implements ICriteriaProcessor {

	@Override
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		return processAnnotation(root, object, field, (path, value) -> { 
			
			if(value == null) {
				return null;
			}
			
			if(value instanceof Boolean) {
				
				Boolean isNotNull = (Boolean) value;
				
				return isNotNull ? criteriaBuilder.isNotNull(path) : null;
				
			} else {
				
				throw new UnsupportedOperationException("CriteriaProcessorIsNotNull funciona apenas para o tipo java.lang.Boolean");
			}
			
		});
	}


}
