package com.app.lika.service;

import com.app.lika.model.Subject;
import com.app.lika.payload.DTO.SubjectDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.CreateSubjectRequest;
import com.app.lika.payload.response.SubjectNameResponse;
import org.springframework.stereotype.Service;

@Service
public interface SubjectService {
    PagedResponse<SubjectDTO> getAllSubjects(PaginationCriteria paginationCriteria);

    PagedResponse<SubjectNameResponse> getAllSubjectsHaveChapter(PaginationCriteria paginationCriteria);

    Boolean checkSubjectIdAvailability(String id);

    Subject addSubject(CreateSubjectRequest subjectRequest);

    SubjectDTO updateSubject(SubjectDTO subjectRequest, String id);

    void deleteSubject(String id);

}
