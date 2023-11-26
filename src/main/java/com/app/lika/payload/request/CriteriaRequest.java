package com.app.lika.payload.request;

import com.app.lika.model.question.Level;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriteriaRequest {

    private Long id;

    @NotNull
    @Min(value = 1,message = "Criteria need quantity between 1 to 5")
    @Max(value = 5,message = "Criteria need quantity between 1 to 5")
    private Short quantity;

    @NotNull
    private Level level;

    @NotNull
    private Long chapterId;
}
