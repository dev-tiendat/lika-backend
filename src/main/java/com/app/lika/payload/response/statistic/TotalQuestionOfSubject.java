package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TotalQuestionOfSubject {
    private String subjectName;

    private Long numberOfQuestion;
}
