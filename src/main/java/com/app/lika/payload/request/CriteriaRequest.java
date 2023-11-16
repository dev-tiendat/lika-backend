package com.app.lika.payload.request;

import com.app.lika.model.question.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriteriaRequest {

    private Long id;

    @NotNull
    private Short quantity;

    @NotNull
    private Level level;

    @NotNull
    private Long chapterId;
}
