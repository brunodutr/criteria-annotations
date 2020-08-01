package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

class CriteriaProcessorEqual implements ICriteriaProcessor {

	@Override
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		return processAnnotation(root, object, field, criteriaBuilder::equal);
	}


}
