package com.github.brunodutr.persistence.criteria.service;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaSubQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import java.lang.reflect.Field;

class CriteriaProcessorSubQuery implements ICriteriaProcessor {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Predicate process(final CriteriaBuilder criteriaBuilder, final Root<?> root, final Object object, final Field field) throws Exception {
        return processAnnotation(root, object, field, (path, value) -> {

            CriteriaSubQuery criteriaSubQuery = field.getAnnotation(CriteriaSubQuery.class);

            CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
            String columnName = CriteriaUtils.getColumnName(field);

            if (criteriaSubQuery.type() == null) {
                throw new UnsupportedOperationException("CriteriaSubQuery.type() is required");
            }

            if (criteriaSubQuery.entity() == null) {
                throw new UnsupportedOperationException("CriteriaSubQuery.entity() is required");
            }

            Subquery subquery = criteriaQuery.subquery(criteriaSubQuery.type());
            Root subRoot = subquery.from(criteriaSubQuery.entity());

            if (criteriaSubQuery.field() == null || criteriaSubQuery.field().isBlank()) {
                subquery.select(subRoot.get(columnName));
            } else {
                subquery.select(subRoot.get(criteriaSubQuery.field()));
            }

            if (criteriaSubQuery.distinct()) {
                subquery.distinct(true);
            }

            if (criteriaSubQuery.path() == null || criteriaSubQuery.path().isBlank()) {
                subquery.where(criteriaBuilder.equal(subRoot.get(columnName), value));
            } else {
                subquery.where(criteriaBuilder.equal(subRoot.get(criteriaSubQuery.path()), value));
            }

            return criteriaBuilder.in(path).value(subquery);
        });
    }
}
