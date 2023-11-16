package com.app.lika.service;

import com.app.lika.model.user.Status;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    PagedResponse<UserProfile> getAllUsers(PaginationCriteria paginationCriteria);

    UserProfile getCurrentUser(UserPrincipal currentUser);

    UserProfile getUserProfile(String username);

    UserProfile addUser(UserRequest userRequest);

    UserProfile deleteUser(String username);

    UserProfile giveAdmin(String username);

    UserProfile updateUser(UserRequest newUser,String username, UserPrincipal userPrincipal);

    UserProfile activateOrDeactivateUser(String username, Status status);
}
