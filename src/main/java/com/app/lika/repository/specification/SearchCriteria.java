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
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(keyExpression, value);
            case LESS_THAN -> builder.lessThan(keyExpression, value);
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(keyExpression, value);
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(keyExpression, value);
            case NOT_EQUAL -> builder.notEqual(keyExpression, value);
            case EQUAL -> builder.equal(keyExpression, value);
            case MATCH -> builder.like(builder.lower(keyExpression), "%" + value.toLowerCase() + "%");
            case MATCH_END -> builder.like(builder.lower(keyExpression), value.toLowerCase() + "%");
            case MATCH_START -> builder.like(builder.lower(keyExpression), "%" + value.toLowerCase());
            case IN -> builder.in(keyExpression).value(value);
            case NOT_IN -> builder.not(keyExpression.in(value));
        };

    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters == null ? new HashMap<>() : filters;
    }
}
