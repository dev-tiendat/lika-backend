package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatisticQuestion {
    List<StatisticQuestionBySubject> questionBySubjects;
}
