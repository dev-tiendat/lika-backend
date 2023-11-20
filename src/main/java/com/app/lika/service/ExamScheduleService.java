package com.app.lika.service;

import com.app.lika.model.Status;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.payload.response.StudentExamSchedule;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamScheduleService {
    List<StudentExamSchedule> getAllExamScheduleForMe(UserPrincipal currentUser);
    PagedResponse<ExamScheduleDTO> getAllExamSchedule(PaginationCriteria paginationCriteria);
    ExamScheduleDTO addExamSchedule(ExamScheduleRequest examScheduleRequest);
    ExamScheduleDTO updateExamSchedule(ExamScheduleRequest examScheduleRequest, long id);
    ExamScheduleDTO deleteExamSchedule(Long id);
    ExamScheduleDTO enableAndCancelExamSchedule(long id, Status status);
}
