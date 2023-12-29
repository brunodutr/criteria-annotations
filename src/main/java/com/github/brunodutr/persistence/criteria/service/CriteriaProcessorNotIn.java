package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorNotIn extends CriteriaProcessorIn {

	@Override
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
		return super.process(criteriaBuilder, root, object, field).not();
	}

}
