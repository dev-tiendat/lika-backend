package com.app.lika.mapper;

import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.DTO.UserSummary;
import com.app.lika.payload.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
     User userRequestToEntity(UserRequest userRequest);

     User userProfileToEntity(UserProfile userProfile);

     UserProfile entityToUserProfile(User user);

     UserSummary entityToUserSummary(User user);
}
