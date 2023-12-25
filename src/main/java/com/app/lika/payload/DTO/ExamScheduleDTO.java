package com.app.lika.payload.DTO;

import com.app.lika.model.Status;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.payload.response.ExamSetInfoResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamScheduleDTO {
    private Long id;

    private String title;

    private String summary;

    private Long publishedAt;

    private Long closedAt;

    private Integer timeAllowance;

    private Status status;

    private ExamSetInfoResponse examSet;

    private UserSummary teacher;

    private List<UserSummary> students;
}
