package com.app.lika.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
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

    @Valid
    private List<CriteriaRequest> criteria;

    public List<CriteriaRequest> getCriteria() {
        return criteria == null ? new ArrayList<>() : new ArrayList<>(criteria);
    }
}
