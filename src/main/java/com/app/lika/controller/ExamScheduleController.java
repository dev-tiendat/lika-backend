package com.app.lika.controller;

import com.app.lika.model.Status;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.payload.response.APIMessageResponse;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.service.ExamScheduleService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/examSchedule")
public class ExamScheduleController {
    private static final String[] SORT_COLUMNS = new String[]{"id", "title", "publicAt", "status"};
    private final ExamScheduleService examScheduleService;

    @Autowired
    public ExamScheduleController(ExamScheduleService examScheduleService) {
        this.examScheduleService = examScheduleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<PagedResponse<ExamScheduleDTO>>> getAllExamSchedule(
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

        PagedResponse<ExamScheduleDTO> data = examScheduleService.getAllExamSchedule(paginationCriteria);
        APIResponse<PagedResponse<ExamScheduleDTO>> response = new APIResponse<>("Get all users successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<ExamScheduleDTO>> addExamSchedule(@RequestBody @Valid ExamScheduleRequest examScheduleRequest) {
        ExamScheduleDTO data = examScheduleService.addExamSchedule(examScheduleRequest);
        APIResponse<ExamScheduleDTO> response = new APIResponse<>("Add new Exam schedule successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<ExamScheduleDTO>> updateExamSchedule(
            @RequestBody @Valid ExamScheduleRequest examScheduleRequest,
            @PathVariable(name = "id") Long id
    ) {
        ExamScheduleDTO data = examScheduleService.updateExamSchedule(examScheduleRequest, id);
        APIResponse<ExamScheduleDTO> response = new APIResponse<>("Update exam schedule successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIMessageResponse> deleteExamSchedule(@PathVariable(name = "id") Long id) {
        examScheduleService.deleteExamSchedule(id);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Delete exam schedule successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIMessageResponse> enableExamSchedule(@PathVariable(name = "id") Long id) {
        examScheduleService.enableAndCancelExamSchedule(id, Status.ENABLE);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Enable exam schedule successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIMessageResponse> cancelExamSchedule(@PathVariable(name = "id") Long id) {
        examScheduleService.enableAndCancelExamSchedule(id, Status.DISABLE);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Cancel exam schedule successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
