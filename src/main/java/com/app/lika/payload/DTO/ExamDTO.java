package com.app.lika.payload.DTO;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.question.Question;
import com.app.lika.payload.response.QuestionResponse;
import lombok.Data;

import java.util.List;

@Data
public class ExamDTO {
    private Long id;

    private Integer examCode;

    private List<QuestionResponse> questions;
}
