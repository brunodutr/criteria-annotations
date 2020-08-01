package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

class CriteriaProcessorIsNull implements ICriteriaProcessor {

	@Override
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		return processAnnotation(root, object, field, (path, value) ->  {
			
			if(value == null) {
				return null;
			}
			
			if(value instanceof Boolean) {
				
				Boolean isNull = (Boolean) value;
				
				return isNull ? criteriaBuilder.isNull(path) : null;
				
			} else {
				
				throw new UnsupportedOperationException("CriteriaProcessorIsNull funciona apenas para o tipo java.lang.Boolean");
			}
			
		});
	}


}
