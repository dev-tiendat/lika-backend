package com.app.lika.controller;


import com.app.lika.model.Chapter;
import com.app.lika.model.Subject;
import com.app.lika.payload.DTO.SubjectDTO;
import com.app.lika.payload.pagination.*;
import com.app.lika.payload.request.CreateSubjectRequest;
import com.app.lika.payload.response.APIMessageResponse;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.IdentityAvailability;
import com.app.lika.payload.response.SubjectNameResponse;
import com.app.lika.service.ChapterService;
import com.app.lika.service.SubjectService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.PaginationUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects")
public class SubjectController {

    private static final String[] SORT_COLUMNS = new String[]{"subjectId", "subjectName", "creditHours"};

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/checkSubjectIdAvailability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<IdentityAvailability>> checkSubjectAvailability(@RequestParam(name = "id") String id) {
        Boolean isAvailable = subjectService.checkSubjectIdAvailability(id);
        IdentityAvailability data = new IdentityAvailability(isAvailable);
        APIResponse<IdentityAvailability> response = new APIResponse<>("Check subject availability :" + isAvailable, data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<PagedResponse<SubjectDTO>>> getAllSubjects(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "order", required = false, defaultValue = AppConstants.DEFAULT_SORT_METHOD) @Pattern(regexp = "asc|desc") String sort,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SUBJECT_ID) String sortField
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);
        PaginationUtils.sortColumnCheck(SORT_COLUMNS, sortField);

        SortOrder sortOrder = SortOrder.fromValue(sort);
        SortBy sortBy = new SortBy(sortField, sortOrder);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size, query, sortBy, null);

        PagedResponse<SubjectDTO> data = subjectService.getAllSubjects(paginationCriteria);
        APIResponse<PagedResponse<SubjectDTO>> response = new APIResponse<>("Get all subjects successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/haveChapter")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<PagedResponse<SubjectNameResponse>>> getAllSubjectNameHaveQuestion(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        PaginationUtils.validatePageNumberAndSize(page, size);

        FilterBy filters = new FilterBy();
        filters.addFilter("isHaveChapter", Boolean.TRUE.toString());

        SortBy sortBy = new SortBy("subjectName", SortOrder.ASC);
        PaginationCriteria paginationCriteria = new PaginationCriteria(page, size, query, sortBy, filters);
        PagedResponse<SubjectNameResponse> data = subjectService.getAllSubjectsHaveChapter(paginationCriteria);

        APIResponse<PagedResponse<SubjectNameResponse>> response = new APIResponse<>("Get all subjects successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}/chapters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<Chapter>> getChaptersBySubject(@PathVariable(name = "id") String id) {
        List<Chapter> data = chapterService.getAllChapterBySubjectId(id);
        APIResponse<Chapter> response = new APIResponse("Get all chapters by " + id + " successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Subject>> addSubject(@RequestBody @Valid CreateSubjectRequest subject) {
        Subject data = subjectService.addSubject(subject);
        APIResponse<Subject> response = new APIResponse<>("Add subject successful!", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIMessageResponse> deleteSubject(@PathVariable String id) {
        subjectService.deleteSubject(id);
        APIMessageResponse response = new APIMessageResponse(Boolean.TRUE, "Delete subject successful !");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<SubjectDTO>> updateSubject(
            @RequestBody @Valid SubjectDTO newSubject,
            @PathVariable(name = "id") String id
    ) {
        SubjectDTO data = subjectService.updateSubject(newSubject, id);
        APIResponse<SubjectDTO> response = new APIResponse<>("Updated subject by " + id + " successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
