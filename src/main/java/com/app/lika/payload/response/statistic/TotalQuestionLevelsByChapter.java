package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalQuestionLevelsByChapter {
    private String chapterName;

    private Long numberOfEasyQuestions;

    private Long numberOfMediumQuestions;

    private Long numberOfHardQuestions;
}
