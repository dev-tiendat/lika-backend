package com.app.lika.payload.DTO;

import com.app.lika.model.Exam;
import com.app.lika.model.examSet.Status;
import com.app.lika.payload.response.SubjectNameResponse;
import lombok.Data;

import java.util.List;

@Data
public class ExamSetDTO {
    private Long id;

    private String name;

    private Status status;

    private SubjectNameResponse subject;

    private UserSummary createdBy;

    private List<CriteriaDTO> criteria;

    private List<ExamDTO> exams;
}
