package com.github.brunodutr.persistence.criteria.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;
import java.util.Collection;

class CriteriaProcessorIn implements ICriteriaProcessor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

			// convert collection to string
			Object[] valueString = collection.stream().map(Object::toString).toArray();

			return path.in(valueString);

		});
	}

}
