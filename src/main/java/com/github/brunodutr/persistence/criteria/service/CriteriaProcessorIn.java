package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;
import java.util.Collection;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorIn implements ICriteriaProcessor {

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field)
			throws Exception {
		return processAnnotation(root, object, field, (path, value) -> {

			if (value instanceof Collection collection) {

				if (collection.isEmpty()) {
					return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
				}

			} else {
				throw new UnsupportedOperationException(formatException(Collection.class, field));
			}

			return path.in(value);

		});
	}

}
