package com.app.lika.controller;

import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.service.ExamScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/examSchedule")
public class ExamScheduleController {
    private final ExamScheduleService examScheduleService;

    @Autowired
    public ExamScheduleController(ExamScheduleService examScheduleService) {
        this.examScheduleService = examScheduleService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<ExamScheduleDTO>> addExamSchedule(@RequestBody @Valid ExamScheduleRequest examScheduleRequest){
        ExamScheduleDTO data = examScheduleService.addExamSchedule(examScheduleRequest);
        APIResponse<ExamScheduleDTO> response = new APIResponse<>("Add new Exam schedule successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
