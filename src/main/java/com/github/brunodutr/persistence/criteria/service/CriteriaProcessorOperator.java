package com.github.brunodutr.persistence.criteria.service;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class CriteriaProcessorOperator {

    public static Predicate process(final CriteriaBuilder criteriaBuilder, final Map<String, Predicate> map, final CriteriaOperator criteriaOperator) {
        // get value for CriteriaOperator
        String[] fieldsToApplyOperator = criteriaOperator.value();

        // verify if CriteriaOperator has value
        if (fieldsToApplyOperator == null) {
            throw new IllegalArgumentException("CriteriaOperator value cannot be null.");
        }

        // verify if CriteriaOperator has value
        if (fieldsToApplyOperator.length == 0) {
            throw new IllegalArgumentException("CriteriaOperator value cannot be empty.");
        }

        if (fieldsToApplyOperator.length == 1) {
            return map.get(fieldsToApplyOperator[0]);
        }

        // get Predicate for field
        return Arrays.stream(fieldsToApplyOperator)
                .map(map::get)
                .filter(Objects::nonNull)
                .reduce((a, b) -> {
                    CriteriaOperator.Operator operator = criteriaOperator.operator();

                    if (operator.equals(CriteriaOperator.Operator.AND)) {
                        return criteriaBuilder.and(a, b);
                    } else if (operator.equals(CriteriaOperator.Operator.OR)) {
                        return criteriaBuilder.or(a, b);
                    } else {
                        throw new IllegalArgumentException("Operator " + operator + " not supported.");
                    }
                }).orElse(null);
    }
}
