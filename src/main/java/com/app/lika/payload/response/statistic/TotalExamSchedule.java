package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalExamSchedule {
    private Long total;

    private Long examScheduleCompleted;

    private Long examScheduleIncomplete;

    private Long examScheduleCancel;
}
