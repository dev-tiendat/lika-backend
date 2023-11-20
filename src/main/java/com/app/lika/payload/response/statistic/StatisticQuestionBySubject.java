package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatisticQuestionBySubject {
    private Long totalEasyQuestion;

    private Long totalMediumQuestion;

    private Long totalHardQuestion;

    List<TotalQuestionLevelsByChapter> totalQuestionLevelsByChapters;
}
