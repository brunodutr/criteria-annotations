package br.com.bdutra.persistence.criteria.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

class CriteriaProcessorStartOfDay implements ICriteriaProcessor {

	@Override
	@SuppressWarnings("unchecked")
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		
		return processAnnotation(root, object, field, (path, value) -> {

			LocalDateTime dateTime;

			if (value instanceof LocalDate) {
				LocalDate date = (LocalDate) value;
				dateTime = date.atStartOfDay();
			} else {
				throw new UnsupportedOperationException(
						"CriteriaStartOfDay funciona apenas para o tipo java.time.LocalDate");
			}

			Path<LocalDateTime> pathTime = (Path<LocalDateTime>) path;

			return criteriaBuilder.greaterThan(pathTime, dateTime);
		});

	}

}
