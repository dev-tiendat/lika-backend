package com.app.lika.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TotalUser {
    private Long total;

    private Long adminUser;

    private Long teacherUser;

    private Long studentUser;
}
