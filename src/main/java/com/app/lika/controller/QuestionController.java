package com.app.lika.controller;

import com.app.lika.model.Status;
import com.app.lika.payload.DTO.QuestionDTO;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.request.QuestionRequest;
import com.app.lika.payload.response.APIMessageResponse;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.security.CurrentUser;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.QuestionService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/questions")
public class QuestionController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "questionContent", "type", "level"};
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<PagedResponse<QuestionDTO>>> getAllQuestions(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "order", required = false, defaultValue = AppConstants.DEFAULT_SORT_METHOD) String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortField,
            @RequestParam(name = "subjectId", required = false) String subjectId,
            @RequestParam(name = "teacherId", required = false) Long teacherId
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);
        PaginationUtils.sortColumnCheck(SORT_COLUMNS, sortField);

        FilterBy filters = new FilterBy();
        filters.addFilter("subjectId", subjectId);
        filters.addFilter("teacherId", teacherId == null ? null : teacherId.toString());

        SortOrder sortOrder = SortOrder.fromValue(sort);
        SortBy sortBy = new SortBy(sortField, sortOrder);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size, query, sortBy, filters);

        PagedResponse<QuestionDTO> data = questionService.getAllQuestions(paginationCriteria);
        APIResponse<PagedResponse<QuestionDTO>> response = new APIResponse<>("Get all users successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<QuestionDTO>> addQuestion(
            @RequestBody @Valid QuestionRequest questionRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        QuestionDTO data = questionService.addQuestion(questionRequest, currentUser);
        APIResponse<QuestionDTO> response = new APIResponse<>("Add new question successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<QuestionDTO>> updateQuestion(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid QuestionRequest questionRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        QuestionDTO data = questionService.updateQuestion(questionRequest, currentUser, id);
        APIResponse<QuestionDTO> response = new APIResponse<>("Update question successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIMessageResponse> deleteQuestion(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        questionService.deleteQuestion(id, currentUser);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Delete question successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIMessageResponse> enableQuestion(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        questionService.enableOrDisableQuestion(id, currentUser, Status.ENABLE);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Enable question successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIMessageResponse> disableQuestion(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        questionService.enableOrDisableQuestion(id, currentUser, Status.DISABLE);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Disable question successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
