package com.app.lika.controller;

import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.security.CurrentUser;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.ExamSetService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/examSet")
public class ExamSetController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "name"};
    private final ExamSetService examSetService;

    @Autowired
    public ExamSetController(ExamSetService examSetService) {
        this.examSetService = examSetService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<PagedResponse<ExamSetDTO>>> getAllExamSets(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "order", required = false, defaultValue = AppConstants.DEFAULT_SORT_METHOD) String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortField,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "subjectId", required = false) String subjectId
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);
        PaginationUtils.sortColumnCheck(SORT_COLUMNS, sortField);

        FilterBy filters = new FilterBy();
        filters.addFilter("subjectId", subjectId);
        filters.addFilter("status", status != null ? status.toString() : "");

        SortOrder sortOrder = SortOrder.fromValue(sort);
        SortBy sortBy = new SortBy(sortField, sortOrder);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, status != null ? 999 : size, query, sortBy, filters);

        PagedResponse<ExamSetDTO> data = examSetService.getAllExamSets(paginationCriteria);
        APIResponse<PagedResponse<ExamSetDTO>> response = new APIResponse<>("Get all exam sets successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<ExamSetDTO>> addExamSet(
            @RequestBody @Valid CreateExamSetRequest createExamSetRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExamSetDTO data = examSetService.addExamSet(createExamSetRequest, currentUser);
        APIResponse<ExamSetDTO> response = new APIResponse<>("Add Exam set successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<ExamSetDTO>> updateExamSet(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid CreateExamSetRequest createExamSetRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExamSetDTO data = examSetService.updateExamSet(id, createExamSetRequest, currentUser);
        APIResponse<ExamSetDTO> response = new APIResponse<>("Updated Exam set successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<ExamSetDTO>> deleteExamSet(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExamSetDTO data = examSetService.deleteExamSet(id, currentUser);
        APIResponse<ExamSetDTO> response = new APIResponse<>("Deleted Exam set successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
