package com.app.lika.payload.response;

import com.app.lika.payload.DTO.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TakeExamResponse {
    private ExamInfo examInfo;

    private UserSummary student;

    private List<ExamQuestion> questions;
}
