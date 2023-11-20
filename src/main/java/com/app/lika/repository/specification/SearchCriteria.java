package com.app.lika.repository.specification;

import com.app.lika.exception.AppException;
import com.app.lika.payload.pagination.FilterBy;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class SearchCriteria<T> implements Specification<T> {
    private static final long serialVersion = 1L;

    private Map<String, String> filters;

    private String search;

    private CriteriaBuilder builder;

    public SearchCriteria(Map<String, String> filters, String search) {
        this.setFilters(filters);
        this.search = search;
    }

    public abstract Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search);

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.builder = builder;
        return this.toPredicate(root, query, builder, this.filters, this.search);
    }

    public Predicate buildPredicate(SearchOperation operation, Expression<String> keyExpression, String value) {
        Expression<String> expression = keyExpression.as(String.class);
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(expression, value);
            case LESS_THAN -> builder.lessThan(expression, value);
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(expression, value);
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(expression, value);
            case NOT_EQUAL -> builder.notEqual(expression, value);
            case EQUAL -> builder.equal(expression, value);
            case MATCH -> builder.like(builder.lower(expression), "%" + value.toLowerCase() + "%");
            case MATCH_END -> builder.like(builder.lower(expression), value.toLowerCase() + "%");
            case MATCH_START -> builder.like(builder.lower(expression), "%" + value.toLowerCase());
            case IN -> builder.in(expression).value(value);
            case NOT_IN -> builder.not(expression.in(value));
        };
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters == null ? new HashMap<>() : filters;
    }

}
