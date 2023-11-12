package com.app.lika.payload.pagination;

import java.util.HashMap;
import java.util.Map;

public class FilterBy {
    private final Map<String, String> mapOfFilters;

    public FilterBy() {
        mapOfFilters = new HashMap<>();
    }

    public Map<String, String> getMapOfFilters() {
        return mapOfFilters;
    }

    public void addFilter(String filterColumn, String filterValue) {
        mapOfFilters.put(filterColumn, filterValue);
    }
}
