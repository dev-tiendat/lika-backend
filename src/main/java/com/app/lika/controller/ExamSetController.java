package com.app.lika.controller;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.service.ExamSetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/examSet")
public class ExamSetController {
    private final ExamSetService examSetService;

    @Autowired
    public ExamSetController(ExamSetService examSetService) {
        this.examSetService = examSetService;
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<APIResponse<ExamSet>> addExamSet(@RequestBody @Valid CreateExamSetRequest createExamSetRequest) {
        ExamSet data = examSetService.addExamSetService(createExamSetRequest);
        APIResponse<ExamSet> response = new APIResponse<>("Add Exam set successful", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
