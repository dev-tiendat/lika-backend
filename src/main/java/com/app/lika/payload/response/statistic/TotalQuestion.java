package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalQuestion {
    private Long total;

    private Long disableQuestion;

    private Long enableQuestion;
}
