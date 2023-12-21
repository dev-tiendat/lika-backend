package com.app.lika.payload.DTO;

import com.app.lika.model.answer.Correct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnswerDTO {
    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    private Correct isCorrect;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 1)
    private String optionLetter;

}
