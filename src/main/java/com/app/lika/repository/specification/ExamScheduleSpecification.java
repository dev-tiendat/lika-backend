package com.app.lika.repository.specification;

import com.app.lika.model.Chapter;
import com.app.lika.model.ExamSchedule;
import com.app.lika.model.Subject;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.question.Question;
import com.app.lika.model.user.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamScheduleSpecification extends SearchCriteria<ExamSchedule> {

    public ExamScheduleSpecification(Map<String, String> filters, String search) {
        super(filters, search);
    }

    @Override
    public Predicate toPredicate(Root<ExamSchedule> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search) {
        List<Predicate> predicates = new ArrayList<>();
        String subjectId = filters.get("subjectId");
        String teacherId = filters.get("teacherId");

        Join<ExamSchedule, ExamSet> examScheduleExamSetJoin = root.join("examSet", JoinType.LEFT);
        if (StringUtils.isNotEmpty(subjectId)) {
            Join<ExamSet, Subject> examSetSubjectJoin = examScheduleExamSetJoin.join("subject", JoinType.LEFT);
            Predicate equalSubjectId = buildPredicate(SearchOperation.EQUAL, examSetSubjectJoin.get("subjectId"), subjectId);

            predicates.add(equalSubjectId);
        }

        Join<ExamSchedule, User> examScheduleUserJoin = root.join("teacher", JoinType.LEFT);
        if (StringUtils.isNotEmpty(teacherId)) {
            Predicate equalTeacherId = buildPredicate(SearchOperation.EQUAL, examScheduleUserJoin.get("id"), teacherId);

            predicates.add(equalTeacherId);
        }

        if (StringUtils.isNotEmpty(search)) {
            Predicate likeQuestionContent = buildPredicate(SearchOperation.MATCH, root.get("title"), search);
            Predicate equalQuestionId = buildPredicate(SearchOperation.EQUAL, root.get("id"), search);
            Predicate likeFirstName = buildPredicate(SearchOperation.MATCH, examScheduleUserJoin.get("firstName"), search);
            Predicate likeLastName = buildPredicate(SearchOperation.MATCH, examScheduleUserJoin.get("lastName"), search);
            predicates.add(builder.or(likeLastName, likeFirstName, equalQuestionId, likeQuestionContent));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
