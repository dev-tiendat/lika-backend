package com.app.lika.payload.response;

import com.app.lika.model.examSet.Status;
import com.app.lika.payload.DTO.CriteriaDTO;
import com.app.lika.payload.DTO.ExamDTO;
import com.app.lika.payload.DTO.UserSummary;
import lombok.Data;

import java.util.List;

@Data
public class ExamSetInfoResponse {
    private Long id;

    private String name;

    private SubjectNameResponse subject;

    private List<ExamDTO> exams;
}
