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
			// case columnName contains a dot, it means that it is a nested column and we need to create a path to it (ex: user.name)
			if (columnName.contains(".")) {
				String[] columnNames = columnName.split("\\.");
				Path<?> path = root.get(columnNames[0]);
				for (int i = 1; i < columnNames.length; i++) {
					path = path.get(columnNames[i]);
				}
				return criteriaAction.run(path, value);
			}

			Path<?> path = root.get(columnName);
			return criteriaAction.run(path, value);
		}

		return null;

	}

	default String formatException(final Class<?> type, final Field field) {
		return String.format(this.getClass().getSimpleName() + "works only with %s. Field: %s", type.getSimpleName(), field.getName());
	}
}
