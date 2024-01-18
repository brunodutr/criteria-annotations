package com.github.brunodutr.persistence.criteria.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface CriteriaSubQuery {

    boolean distinct() default false;

    Class<?> type();

    String field();

    String path() default "";

}
