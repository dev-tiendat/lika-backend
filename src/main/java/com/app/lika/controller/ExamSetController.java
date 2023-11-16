package com.app.lika.controller;

import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.security.CurrentUser;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.ExamSetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/examSet")
public class ExamSetController {
    private final ExamSetService examSetService;

    @Autowired
    public ExamSetController(ExamSetService examSetService) {
        this.examSetService = examSetService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<ExamSetDTO>> addExamSet(
            @RequestBody @Valid CreateExamSetRequest createExamSetRequest,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExamSetDTO data = examSetService.addExamSetService(createExamSetRequest, currentUser);
        APIResponse<ExamSetDTO> response = new APIResponse<>("Add Exam set successful", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
