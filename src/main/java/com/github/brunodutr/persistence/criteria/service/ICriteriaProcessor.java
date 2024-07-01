package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.github.brunodutr.persistence.criteria.interfaces.CriteriaAction;

interface ICriteriaProcessor {

	Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception;

	default Predicate processAnnotation(Root<?> root, Object object, Field field, CriteriaAction criteriaAction) throws Exception {

		String columnName = CriteriaUtils.getColumnName(field);
		Object value = CriteriaUtils.getValueWithReflection(object, field);

		if (CriteriaUtils.hasValue(value)) {	
			Path<?> path = CriteriaUtils.getPath(root, columnName);
			return criteriaAction.run(path, value);
		}

		return null;

	}

	default String formatException(final Class<?> type, final Field field) {
		return String.format(this.getClass().getSimpleName() + "works only with %s. Field: %s", type.getSimpleName(), field.getName());
	}
}
