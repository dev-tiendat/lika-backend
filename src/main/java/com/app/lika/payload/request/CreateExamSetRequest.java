package com.app.lika.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateExamSetRequest {
        @NotNull
        @NotBlank
        private String name;

        @NotNull
        @NotBlank
        private String subjectId;

        @NotNull
        private Short quantityOfExam;

        private List<CriteriaRequest> criteria;
}
