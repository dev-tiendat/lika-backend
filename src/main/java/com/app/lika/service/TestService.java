package com.app.lika.service;

import com.app.lika.payload.response.ExamResultDetailResponse;
import com.app.lika.payload.response.ExamResultGrade;
import com.app.lika.payload.response.TakeExamResponse;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TestService {
    TakeExamResponse takeExam(Long scheduleId, UserPrincipal currentUser);

    ExamResultGrade submitExam(Long scheduleId, UserPrincipal currentUser, List<Long> answers);

    ExamResultDetailResponse getOldExam(Long scheduleId, UserPrincipal currentUser);
}
