package com.app.lika.payload.DTO;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.question.Question;
import lombok.Data;

import java.util.List;

@Data
public class ExamDTO {
    private Long id;

    private Integer examCode;

    private ExamSet examSet;

    private List<Question> questions;
}
