package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;
import java.util.Collection;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorIn implements ICriteriaProcessor {

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field)
			throws Exception {
		return processAnnotation(root, object, field, (path, value) -> {

			if (value instanceof Collection) {
				Collection collection = (Collection) value;

				if (collection.isEmpty()) {
					return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
				}

			} else {
				throw new IllegalArgumentException(String.format("%s funciona apenas para o tipo java.util.Collection", this.getClass().getSimpleName()));
			}

			return path.in(value);

		});
	}

}
