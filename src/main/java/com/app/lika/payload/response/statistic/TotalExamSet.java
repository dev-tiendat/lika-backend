package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalExamSet {
    private Long total;

    private Long usedExamSet;

    private Long remainingExamSet;
}
