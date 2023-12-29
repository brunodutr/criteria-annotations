package com.github.brunodutr.persistence.criteria.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to be used to name the column of the entity that will be used in the criteria.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface CriteriaColumn {

	String value();
}
