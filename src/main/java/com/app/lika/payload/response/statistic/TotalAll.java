package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalAll {
    private TotalExamSchedule totalExamSchedule;

    private TotalSubject totalSubject;

    private TotalQuestion totalQuestion;

    private TotalExamSet totalExamSet;

    private TotalUser totalUser;
}
