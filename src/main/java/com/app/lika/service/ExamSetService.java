package com.app.lika.service;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface ExamSetService {
    ExamSet addExamSetService(CreateExamSetRequest createExamSetRequest);
}
