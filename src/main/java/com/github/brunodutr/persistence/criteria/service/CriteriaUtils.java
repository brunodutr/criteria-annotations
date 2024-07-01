package com.github.brunodutr.persistence.criteria.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaColumn;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

class CriteriaUtils {

	public static Object getValueWithReflection(final Object object, final Field field) throws IllegalAccessException {

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

	public static Boolean hasValue(final Object fieldValue) {

		if (fieldValue instanceof String) {

			return isNotBlank((String) fieldValue);

		} else {

			return fieldValue != null;
		}
	}

	public static String getColumnName(final Field field) {

		if (field.isAnnotationPresent(CriteriaColumn.class)) {

			CriteriaColumn criteriaColumn = field.getAnnotation(CriteriaColumn.class);

			if (isNotBlank(criteriaColumn.value())) {
				return criteriaColumn.value();
			}

		}

		return field.getName();

	}

	public static Path<?> getPath(final Root<?> root, final String columnName) {

		if (columnName.contains(".")) {
			String[] columnNames = columnName.split("\\.");
			Path<?> path = root.get(columnNames[0]);
			for (int i = 1; i < columnNames.length; i++) {
				path = path.get(columnNames[i]);
			}
			return path;
		}

		return root.get(columnName);
	}
}
