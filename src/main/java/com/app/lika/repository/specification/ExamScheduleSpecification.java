package com.app.lika.repository.specification;

import com.app.lika.model.ExamSchedule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Map;

public class ExamScheduleSpecification extends SearchCriteria<ExamSchedule> {

    public ExamScheduleSpecification(Map<String, String> filters, String search) {
        super(filters, search);
    }

    @Override
    public Predicate toPredicate(Root<ExamSchedule> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search) {
        return null;
    }
}
