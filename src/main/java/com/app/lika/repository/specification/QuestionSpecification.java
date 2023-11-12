package com.app.lika.repository.specification;

import com.app.lika.model.Chapter;
import com.app.lika.model.question.Question;
import com.app.lika.model.Subject;
import com.app.lika.model.user.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionSpecification extends SearchCriteria<Question> {
    public QuestionSpecification(Map<String, String> filters, String search) {
        super(filters, search);
    }

    @Override
    public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search) {
        List<Predicate> predicates = new ArrayList<>();
        String subjectId = filters.get("subjectId");
        String teacherId = filters.get("teacherId");

        Join<Question, Chapter> questionChapterJoin = root.join("chapter", JoinType.LEFT);
        if (StringUtils.isNotEmpty(subjectId)) {
            Join<Chapter, Subject> chapterSubjectJoin = questionChapterJoin.join("subject",JoinType.LEFT);
            Predicate equalSubjectId = buildPredicate(SearchOperation.EQUAL, chapterSubjectJoin.get("subjectId"), subjectId);

            predicates.add(equalSubjectId);
        }

        if (StringUtils.isNotEmpty(teacherId)) {
            Join<Question, User> questionUserJoin = root.join("teacher", JoinType.LEFT);
            Predicate equalTeacherId = buildPredicate(SearchOperation.EQUAL, questionUserJoin.get("id"), teacherId);

            predicates.add(equalTeacherId);
        }

        if (StringUtils.isNotEmpty(search)) {
            Predicate likeQuestionContent = buildPredicate(SearchOperation.MATCH, root.get("content"), search);
            Predicate equalQuestionId = buildPredicate(SearchOperation.EQUAL, root.get("id"), search);
            Predicate likeChapterName = buildPredicate(SearchOperation.MATCH, questionChapterJoin.get("chapterName"), search);

            predicates.add(builder.or(likeChapterName,equalQuestionId, likeQuestionContent));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
