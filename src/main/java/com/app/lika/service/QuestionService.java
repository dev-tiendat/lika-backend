package com.app.lika.service;

import com.app.lika.model.Status;
import com.app.lika.payload.DTO.QuestionDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.QuestionRequest;
import com.app.lika.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    PagedResponse<QuestionDTO> getAllQuestions(PaginationCriteria paginationCriteria);

    QuestionDTO addQuestion(QuestionRequest questionRequest, UserPrincipal currentUser);

    QuestionDTO updateQuestion(QuestionRequest questionRequest, UserPrincipal currentUser, Long id);

    QuestionDTO deleteQuestion(Long id, UserPrincipal currentUser);

    QuestionDTO enableOrDisableQuestion(Long id, UserPrincipal currentPrincipal, Status status);
}
