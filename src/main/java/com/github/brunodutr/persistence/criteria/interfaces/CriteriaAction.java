package com.github.brunodutr.persistence.criteria.interfaces;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface CriteriaAction {

	Predicate run(Path<?> path, Object value);
}
