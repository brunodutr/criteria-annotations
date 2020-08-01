package com.github.brunodutr.persistence.criteria.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaColumn;

class CriteriaUtils {

	public static Object getValueWithReflection(Object object, Field field) throws IllegalAccessException {

		boolean accesibleChanged = false;

		if (!field.isAccessible()) {
			field.setAccessible(true);
			accesibleChanged = true;
		}

		Object fieldValue = field.get(object);

		if (accesibleChanged) {
			field.setAccessible(false);
		}

		return fieldValue;
	}

	public static Boolean hasValue(Object fieldValue) {

		if (fieldValue instanceof String) {

			return isNotBlank((String) fieldValue);

		} else {

			return fieldValue != null;
		}
	}

	public static String getColumnName(Field field) {

		if (field.isAnnotationPresent(CriteriaColumn.class)) {

			CriteriaColumn criteriaColumn = field.getAnnotation(CriteriaColumn.class);

			if (isNotBlank(criteriaColumn.value())) {
				return criteriaColumn.value();
			}

		}

		return field.getName();

	}
}
