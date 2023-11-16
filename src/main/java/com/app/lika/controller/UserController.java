package com.app.lika.controller;

import com.app.lika.model.user.Status;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.request.UserRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.security.CurrentUser;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.UserService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.app.lika.model.user.User;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "firstName", "lastName", "username","email","dateOfBirth"};

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<PagedResponse<UserProfile>>> getAllUsers(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "order", required = false, defaultValue = AppConstants.DEFAULT_SORT_METHOD) String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortField,
            @RequestParam(name = "role", required = false) Integer role,
            @RequestParam(name = "status", required = false) Integer status
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);
        PaginationUtils.sortColumnCheck(SORT_COLUMNS, sortField);

        FilterBy filters = new FilterBy();
        filters.addFilter("role", role == null ? null : role.toString());
        filters.addFilter("status", status == null ? null : status.toString());

        SortOrder sortOrder = SortOrder.fromValue(sort);
        SortBy sortBy = new SortBy(sortField, sortOrder);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size, query, sortBy, filters);

        PagedResponse<UserProfile> data = userService.getAllUsers(paginationCriteria);
        APIResponse<PagedResponse<UserProfile>> response = new APIResponse<>("Get all users successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<APIResponse<UserProfile>> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserProfile data = userService.getCurrentUser(currentUser);
        APIResponse response = new APIResponse<>("Get profile current user successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> getUserProfile(@PathVariable(name = "username") String username) {
        UserProfile data = userService.getUserProfile(username);
        APIResponse<UserProfile> response = new APIResponse<>("Get profile of" + username + "successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//
//    @GetMapping("/me/examSchedule")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<APIResponse<>>

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> addUser(@RequestBody @Valid UserRequest userRequest) {
        UserProfile data = userService.addUser(userRequest);
        APIResponse<UserProfile> response = new APIResponse<>("Add user successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> deleteUser(@PathVariable(name = "username") String username) {
        UserProfile data = userService.deleteUser(username);
        APIResponse response = new APIResponse("Delete user successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{username}")
    public ResponseEntity<APIResponse<UserProfile>> updateUser(
            @Valid @RequestBody UserRequest newUser,
            @PathVariable(name = "username") String username,
            @CurrentUser UserPrincipal currentUser
    ) {
        UserProfile data = userService.updateUser(newUser, username, currentUser);
        APIResponse<UserProfile> response = new APIResponse<>("Updated profile of" + username + "successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> giveAdmin(@PathVariable(name = "username") String username) {
        UserProfile data = userService.giveAdmin(username);
        APIResponse<UserProfile> response = new APIResponse<>("You gave ADMIN role to user: " + username, data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{username}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> activateAccount(@PathVariable(name = "username") String username) {
        UserProfile data = userService.activateOrDeactivateUser(username, Status.ACTIVE);
        APIResponse<UserProfile> response = new APIResponse<>("You activate user of " + username + " successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{username}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserProfile>> deactivateAccount(@PathVariable(name = "username") String username) {
        UserProfile data = userService.activateOrDeactivateUser(username, Status.INACTIVE);
        APIResponse<UserProfile> response = new APIResponse<>("You deactivate user of " + username + " successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
