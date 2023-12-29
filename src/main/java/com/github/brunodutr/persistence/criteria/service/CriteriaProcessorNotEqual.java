package com.github.brunodutr.persistence.criteria.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;

class CriteriaProcessorNotEqual implements ICriteriaProcessor {

	@Override
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
		return processAnnotation(root, object, field, criteriaBuilder::notEqual);
	}


}
