package com.app.lika.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExamResultGrade {
    private Long id;

    private Float grade;

    private String examName;

    private int numberOfQuestions;

    private int numberOfRightAnswer;
}
