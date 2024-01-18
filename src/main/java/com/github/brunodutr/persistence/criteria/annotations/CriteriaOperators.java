package com.github.brunodutr.persistence.criteria.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to be used to name the column of the entity that will be used in the criteria.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface CriteriaOperators {

	CriteriaOperator[] value();

}
