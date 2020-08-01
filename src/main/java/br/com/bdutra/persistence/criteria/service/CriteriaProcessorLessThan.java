package br.com.bdutra.persistence.criteria.service;

import java.lang.reflect.Field;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.bdutra.persistence.criteria.annotations.CriteriaLessThan;

class CriteriaProcessorLessThan implements ICriteriaProcessor {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate process(CriteriaBuilder criteriaBuilder, Root<?> root, Object object, Field field) throws Exception {
		
		return processAnnotation(root, object, field, (path, value) -> {

			if(!(value instanceof Comparable)) {
				throw new UnsupportedOperationException("CriteriaProcessorLessThan funciona apenas para tipos que extendem java.lang.Comparable");
			}
			
			Expression<Comparable> expression = (Expression<Comparable>) path;
			Comparable comparable = (Comparable) value;
			
			CriteriaLessThan criteriaLessThan = field.getAnnotation(CriteriaLessThan.class);

			if(criteriaLessThan.equal()) {
				
				return criteriaBuilder.<Comparable>lessThanOrEqualTo(expression, comparable);
				
			} else {
				
				return criteriaBuilder.<Comparable>lessThan(expression, comparable);
				
			}
		});

	}

}
