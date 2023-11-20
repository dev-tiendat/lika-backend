package com.app.lika.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamScheduleRequest {
    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String summary;

    @NotNull
    private Long publishedAt;

    @NotNull
    private Integer timeAllowance;

    private Long examSetId;

    @NotNull
    private String teacherUsername;

    private List<String> studentsUsernames;

    public List<String> getStudentsUsernames() {
        return studentsUsernames == null ? new ArrayList<>() : new ArrayList<>(studentsUsernames);
    }
}
