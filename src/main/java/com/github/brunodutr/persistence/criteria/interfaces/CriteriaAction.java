package com.github.brunodutr.persistence.criteria.interfaces;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@FunctionalInterface
public interface CriteriaAction {

	Predicate run(Path<?> path, Object value);
}
