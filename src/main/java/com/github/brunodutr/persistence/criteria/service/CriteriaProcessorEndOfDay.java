package com.github.brunodutr.persistence.criteria.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class CriteriaProcessorEndOfDay implements ICriteriaProcessor {

	@Override
	@SuppressWarnings("unchecked")
	public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {

		return processAnnotation(root, object, field, (path, value) -> {

			LocalDateTime dateTime;

			if (value instanceof LocalDate date) {
				dateTime = date.atStartOfDay().plusDays(1).minusSeconds(1);
			} else {
				throw new UnsupportedOperationException(formatException(LocalDate.class, field));
			}

			Path<LocalDateTime> pathTime = (Path<LocalDateTime>) path;

			return criteriaBuilder.lessThan(pathTime, dateTime);
		});
	}

}
