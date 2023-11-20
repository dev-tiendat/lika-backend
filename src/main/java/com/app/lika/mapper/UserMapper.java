package com.app.lika.mapper;

import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.DTO.UserSummary;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.security.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
     User userRequestToEntity(UserRequest userRequest);

     User userProfileToEntity(UserProfile userProfile);

     @Mapping(target = "firstName", source = "firstName")
     @Mapping(target = "lastName", source = "lastName")
     UserSummary userPrincipalToUserSummary(UserPrincipal userPrincipal);

     UserProfile entityToUserProfile(User user);

     UserSummary entityToUserSummary(User user);
}
