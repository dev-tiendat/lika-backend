package com.app.lika.service;

import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface ExamSetService {
    PagedResponse<ExamSetDTO> getAllExamSets(PaginationCriteria paginationCriteria);

    ExamSetDTO addExamSet(CreateExamSetRequest createExamSetRequest, UserPrincipal currentUser);

    ExamSetDTO updateExamSet(Long id, CreateExamSetRequest createExamSetRequest, UserPrincipal currentUser);

    ExamSetDTO deleteExamSet(Long id, UserPrincipal currentUser);
}
