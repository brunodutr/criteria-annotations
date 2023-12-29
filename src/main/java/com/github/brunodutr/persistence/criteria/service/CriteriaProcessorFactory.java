package com.github.brunodutr.persistence.criteria.service;

import com.github.brunodutr.persistence.criteria.annotations.CriteriaEndOfDay;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaEqual;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaGreaterThan;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaIn;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaIsNotNull;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaIsNull;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaLessThan;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaLike;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaNotIn;
import com.github.brunodutr.persistence.criteria.annotations.CriteriaStartOfDay;
import com.github.brunodutr.persistence.criteria.annotations.CriterianNotEqual;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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

	public static ICriteriaProcessor getProcessor(final Field field) {

		List<ICriteriaProcessor> processors = Stream.of(field.getAnnotations())
				                                    .map(Annotation::annotationType)
				                                    .map(PROCESSORS::get)
				                                    .filter(Objects::nonNull)
				                                    .toList();

		if (processors.isEmpty()) {
			return null;
		} else if (processors.size() == 1) {
			return processors.get(0);
		} else {
			throw new IllegalArgumentException("Only one annotation is allowed per field. Field: " + field.getName());
		}

	}
}
