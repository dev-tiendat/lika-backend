package com.app.lika.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class StudentExamSchedule {
    private Long id;

    private String examScheduleName;

    private String subjectId;

    private String subjectName;

    private Long publishedAt;

    private Long closedAt;

    private Integer timeAllowance;
}
