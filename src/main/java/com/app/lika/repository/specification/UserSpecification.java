package com.app.lika.repository.specification;

import com.app.lika.model.role.Role;
import com.app.lika.model.user.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSpecification extends SearchCriteria<User> {

    public UserSpecification(Map<String, String> filter, String search) {
        super(filter, search);
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder, Map<String, String> filter, String search) {
        List<Predicate> predicates = new ArrayList<>();
        String role = filter.get("role");
        String status = filter.get("status");

        if (StringUtils.isNotEmpty(role)) {
            Join<Role, User> userRoleJoin = root.join("roles", JoinType.RIGHT);
            Predicate equalRole = buildPredicate(SearchOperation.EQUAL, userRoleJoin.get("id"), role);
            predicates.add(equalRole);
        }

        if (StringUtils.isNotEmpty(status)) {
            Predicate equalRole = buildPredicate(SearchOperation.EQUAL, root.get("status"), status);
            predicates.add(equalRole);
        }

        if (StringUtils.isNotEmpty(search)) {
            Predicate likeUsername = buildPredicate(SearchOperation.MATCH, root.get("username"), search);
            Predicate likeFirstName = buildPredicate(SearchOperation.MATCH, root.get("firstName"), search);
            Predicate likeLastName = buildPredicate(SearchOperation.MATCH, root.get("lastName"), search);

            predicates.add(builder.or(likeFirstName, likeLastName, likeUsername));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
