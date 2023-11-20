package com.app.lika.payload.response;

import com.app.lika.payload.DTO.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExamResultDetailResponse {
    private ExamInfo examInfo;

    private UserSummary student;

    private List<ExamQuestion> questions;

    private List<Long> studentAnswerIds;
}