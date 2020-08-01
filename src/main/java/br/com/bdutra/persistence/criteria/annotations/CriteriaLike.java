package br.com.bdutra.persistence.criteria.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RUNTIME)
public @interface CriteriaLike {

	String start() default "%";
	
	String end() default "%";
}
