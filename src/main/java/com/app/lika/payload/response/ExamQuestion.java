package com.app.lika.payload.response;

import com.app.lika.model.question.Level;
import com.app.lika.model.question.QuestionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ExamQuestion {
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String image;

    private Level level;

    private QuestionType type;

    private List<ExamAnswer> answers;
}
