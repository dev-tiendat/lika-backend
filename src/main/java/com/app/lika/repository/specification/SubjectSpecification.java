package com.app.lika.repository.specification;

import com.app.lika.model.Subject;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectSpecification extends SearchCriteria<Subject> {

    public SubjectSpecification(Map<String, String> filters, String search) {
        super(filters, search);
    }

    @Override
    public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filters, String search) {
        List<Predicate> predicates = new ArrayList<>();
        String isHaveChapter = filters.get("isHaveChapter");

        if (StringUtils.isNotEmpty(isHaveChapter)
                && Boolean.parseBoolean(isHaveChapter)) {
            root.join("chapters");
        }

        if (StringUtils.isNotEmpty(search)) {
            Predicate likeSubjectId = buildPredicate(SearchOperation.MATCH, root.get("subjectId"), search);
            Predicate likeSubjectName = buildPredicate(SearchOperation.MATCH, root.get("subjectName"), search);
            predicates.add(builder.or(likeSubjectId, likeSubjectName));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
