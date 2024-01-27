package com.app.lika.controller;

import com.app.lika.payload.request.SubmitExamRequest;
import com.app.lika.payload.response.*;
import com.app.lika.security.CurrentUser;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/tests")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/oldTest")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<List<ExamGrade>>> getAllOldTest(
            @CurrentUser UserPrincipal currentUser
    ) {
        List<ExamGrade> data = testService.getAllOldExam(currentUser);
        APIResponse<List<ExamGrade>> response = new APIResponse<>("Take all old exam successful ! ", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/examSchedule/{id}/takeExam")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<TakeExamResponse>> takeExam(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        TakeExamResponse data = testService.takeExam(id, currentUser);
        APIResponse<TakeExamResponse> response = new APIResponse<>("Take exam successful ! Good luck !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/examSchedule/{id}/submitExam")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<ExamResultGrade>> takeExam(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser,
            @RequestBody SubmitExamRequest submitExamRequest
    ) {
        ExamResultGrade data = testService.submitExam(id, currentUser, submitExamRequest.getAnswers());
        APIResponse<ExamResultGrade> response = new APIResponse<>("Take exam successful ! Good luck !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/examSchedule/{id}/getOldExam")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<ExamResultDetailResponse>> getOldExam(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser
    ) {
        ExamResultDetailResponse data = testService.getOldExam(id, currentUser);
        APIResponse<ExamResultDetailResponse> response = new APIResponse<>("Get old exam successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
