package com.app.lika.repository.specification;

import com.app.lika.model.Subject;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.user.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class  ExamSetSpecification extends SearchCriteria<ExamSet> {

    public ExamSetSpecification(Map<String, String> filters, String search) {
        super(filters, search);
    }

    @Override
    public Predicate toPredicate(Root<ExamSet> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search) {
        List<Predicate> predicates = new ArrayList<>();
        String subjectId = filters.get("subjectId");

        Join<ExamSet, Subject> examSetSubjectJoin = root.join("subject");
        if(StringUtils.isNotEmpty(subjectId)){
            Predicate equalSubjectId = buildPredicate(SearchOperation.EQUAL,root.get("subjectId"),subjectId);

            predicates.add(equalSubjectId);
        }

        if(StringUtils.isNotEmpty(search)){
            Join<ExamSet, User> examSetUserJoin = root.join("createdBy");
            Predicate likeName = buildPredicate(SearchOperation.MATCH, root.get("name"), search);
            Predicate likeId = buildPredicate(SearchOperation.MATCH, root.get("id"), search);
            Predicate likeSubjectName =buildPredicate(SearchOperation.MATCH, examSetSubjectJoin.get("subjectName"),search);
            Predicate likeFirstNameCreatedBy = buildPredicate(SearchOperation.MATCH, examSetUserJoin.get("firstName"), search);
            Predicate likeLastNameCreatedBy = buildPredicate(SearchOperation.MATCH, examSetUserJoin.get("lastName"), search);

            predicates.add(builder.or(likeName,likeId,likeFirstNameCreatedBy,likeLastNameCreatedBy,likeSubjectName));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
