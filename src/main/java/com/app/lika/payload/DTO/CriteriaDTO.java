package com.app.lika.payload.DTO;

import com.app.lika.model.question.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriteriaDTO {

    private Long id;

    @NotNull
    private Short quantity;

    @NotNull
    private Level level;

    @NotNull
    private Long chapterId;
}
