package com.app.lika.payload.response;

import com.app.lika.model.Status;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.payload.DTO.UserSummary;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamScheduleStudentResponse {
    private Long id;

    private String title;

    private String summary;

    private Date publishedAt;

    private Integer timeAllowance;

    private Status status;

    private ExamSet examSet;
}

