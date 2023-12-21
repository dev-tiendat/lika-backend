package com.app.lika.mapper;

import com.app.lika.model.role.Role;
import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.DTO.UserSummary;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.security.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User userRequestToEntity(UserRequest userRequest);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserSummary userPrincipalToUserSummary(UserPrincipal userPrincipal);

    @Mapping(target = "dateOfBirth", expression = "java(toTimestamp(user.getDateOfBirth()))")
    @Mapping(target = "roles", expression = "java(toRoleStrings(user.getRoles()))")
    UserProfile entityToUserProfile(User user);

    UserSummary entityToUserSummary(User user);

    default Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    default Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    default List<String> toRoleStrings(List<Role> roles) {
        return roles.stream().map(role -> role.getName().name()).toList();
    }
}
