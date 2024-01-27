package com.app.lika.payload.response;

import lombok.Data;

import java.util.Date;

@Data
public class ExamInfo {
    private Long id;

    private Integer examCode;

    private String title;

    private String subjectName;

    private String summary;

    private Long publishedAt;

    private Long closedAt;

    private Integer timeAllowance;
}

