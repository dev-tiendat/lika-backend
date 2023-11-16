package com.app.lika.controller;

import com.app.lika.model.role.RoleName;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.service.UserService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/teachers")
public class TeacherController {
    private final UserService userService;

    @Autowired
    public TeacherController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<PagedResponse<UserProfile>>> getAllTeachers(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);

        FilterBy filters = new FilterBy();
        filters.addFilter("role", Integer.toString(RoleName.ROLE_TEACHER.ordinal()));

        SortBy sortBy = new SortBy(AppConstants.DEFAULT_SORT_BY, SortOrder.ASC);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size, query, sortBy, filters);

        PagedResponse<UserProfile> data = userService.getAllUsers(paginationCriteria);
        APIResponse<PagedResponse<UserProfile>> response = new APIResponse<>("Get all teachers successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
