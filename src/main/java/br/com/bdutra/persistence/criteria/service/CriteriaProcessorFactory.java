package br.com.bdutra.persistence.criteria.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.bdutra.persistence.criteria.annotations.CriteriaEndOfDay;
import br.com.bdutra.persistence.criteria.annotations.CriteriaEqual;
import br.com.bdutra.persistence.criteria.annotations.CriteriaGreaterThan;
import br.com.bdutra.persistence.criteria.annotations.CriteriaIn;
import br.com.bdutra.persistence.criteria.annotations.CriteriaIsNotNull;
import br.com.bdutra.persistence.criteria.annotations.CriteriaIsNull;
import br.com.bdutra.persistence.criteria.annotations.CriteriaLessThan;
import br.com.bdutra.persistence.criteria.annotations.CriteriaLike;
import br.com.bdutra.persistence.criteria.annotations.CriteriaNotIn;
import br.com.bdutra.persistence.criteria.annotations.CriteriaStartOfDay;
import br.com.bdutra.persistence.criteria.annotations.CriterianNotEqual;

class CriteriaProcessorFactory {

	private static final Map<Class<? extends Annotation>, ICriteriaProcessor> PROCESSORS = new HashMap<>();

	static {
		PROCESSORS.put(CriteriaEqual.class, new CriteriaProcessorEqual());
		PROCESSORS.put(CriteriaStartOfDay.class, new CriteriaProcessorStartOfDay());
		PROCESSORS.put(CriteriaEndOfDay.class, new CriteriaProcessorEndOfDay());
		PROCESSORS.put(CriteriaLike.class, new CriteriaProcessorLike());
		PROCESSORS.put(CriteriaNotIn.class, new CriteriaProcessorNotIn());
		PROCESSORS.put(CriteriaIn.class, new CriteriaProcessorIn());
		PROCESSORS.put(CriteriaGreaterThan.class, new CriteriaProcessorGreaterThan());
		PROCESSORS.put(CriteriaLessThan.class, new CriteriaProcessorLessThan());
		PROCESSORS.put(CriteriaIsNull.class, new CriteriaProcessorIsNull());
		PROCESSORS.put(CriteriaIsNotNull.class, new CriteriaProcessorIsNotNull());
		PROCESSORS.put(CriterianNotEqual.class, new CriteriaProcessorNotEqual());
	}

	public static ICriteriaProcessor getProcessor(Field field) {

		List<ICriteriaProcessor> processors = Stream.of(field.getAnnotations())
				                                    .map(Annotation::annotationType)
				                                    .map(PROCESSORS::get)
				                                    .filter(Objects::nonNull)
				                                    .collect(Collectors.toList());

		if (processors.isEmpty()) {
			return null;
		} else if (processors.size() == 1) {
			return processors.get(0);
		} else {
			throw new IllegalArgumentException(String.format("Field %s deve ter somente um predicado do Criteria Annotations", field.getName()));
		}

	}
}
