package com.app.lika.payload.pagination;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SortBy {

    private String field;

    private SortOrder sortOrder;

    public SortBy(String field, SortOrder sortOrder) {
        this.field = field;
        this.sortOrder = sortOrder;
    }
}
