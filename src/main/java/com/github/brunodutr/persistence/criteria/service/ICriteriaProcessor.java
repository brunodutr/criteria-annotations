package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.github.brunodutr.persistence.criteria.interfaces.CriteriaAction;

interface ICriteriaProcessor {

	Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception;

	default Predicate processAnnotation(Root<?> root, Object object, Field field, CriteriaAction criteriaAction) throws Exception {

		String columnName = CriteriaUtils.getColumnName(field);
		Object value = CriteriaUtils.getValueWithReflection(object, field);

		if (CriteriaUtils.hasValue(value)) {		
			Path<?> path = root.get(columnName);
			return criteriaAction.run(path, value);
		}

		return null;

	}
}
