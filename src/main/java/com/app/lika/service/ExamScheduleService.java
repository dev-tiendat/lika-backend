package com.app.lika.service;

import com.app.lika.model.Status;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.payload.response.ExamScheduleStudentResponse;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface ExamScheduleService {

    PagedResponse<ExamScheduleDTO> getAllExamSchedule(PaginationCriteria paginationCriteria);
    ExamScheduleDTO addExamSchedule(ExamScheduleRequest examScheduleRequest);
    ExamScheduleDTO updateExamSchedule(ExamScheduleRequest examScheduleRequest, long id);
    void deleteExamSchedule(Long id);
    void enableAndCancelExamSchedule(long id, Status status);
}
