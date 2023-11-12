package com.app.lika.service;

import com.app.lika.model.user.Status;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;
import com.app.lika.model.user.User;

@Service
public interface UserService {
    PagedResponse<UserProfile> getAllUsers(PaginationCriteria paginationCriteria);

    UserProfile getCurrentUser(UserPrincipal currentUser);

    UserProfile getUserProfile(String username);

    User addUser(UserRequest userRequest);

    void deleteUser(String username);

    void giveAdmin(String username);

    User updateUser(User newUser,String username, UserPrincipal userPrincipal);

    void activateOrDeactivateUser(String username, Status status);
}
