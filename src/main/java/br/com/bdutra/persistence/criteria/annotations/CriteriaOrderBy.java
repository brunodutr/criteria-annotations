package br.com.bdutra.persistence.criteria.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface CriteriaOrderBy {

	String[] columns() default {};
	
	String sort() default "asc";
	
}
