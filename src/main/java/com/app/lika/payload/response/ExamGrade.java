package com.app.lika.payload.response;

import lombok.Data;

@Data
public class ExamGrade {

    private String examScheduleName;

    private Long publishedAt;

    private Integer timeAllowance;

    private Float grade;

}
