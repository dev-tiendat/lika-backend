package com.app.lika.payload.request;

import com.app.lika.model.answer.Correct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequest {
    private Long id;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    @NotBlank
    private Correct isCorrect;
}
