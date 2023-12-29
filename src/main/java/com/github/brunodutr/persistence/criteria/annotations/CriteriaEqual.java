package com.github.brunodutr.persistence.criteria.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to be used to indicate that the criteria will be used to compare equality of the column.
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface CriteriaEqual {

}
