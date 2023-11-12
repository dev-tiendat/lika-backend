package com.app.lika.payload.request;

import com.app.lika.model.question.Level;
import com.app.lika.model.question.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    @NotNull
    @NotBlank
    private String content;

    private String image;

    @NotNull
    private QuestionType type;

    @NotNull
    private Level level;

    @NotNull
    private Long chapterId;

    List<AnswerRequest> answers;
}
