package com.app.lika.payload.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationCriteria {
    private Integer pageNumber;

    private Integer pageSize;

    private String query;

    private SortBy sortBy;

    private FilterBy filters;

    public PaginationCriteria(Integer pageNumber, Integer pageSize, String query, SortBy sortBy, FilterBy filters) {
        setPageNumber(pageNumber);
        setPageSize(pageSize);
        this.query = query;
        this.sortBy = sortBy;
        this.filters = filters;
    }

    public Integer getPageNumber() {
        return (null == pageNumber) ? 0 : pageNumber;
    }

    public Integer getPageSize() {
        return (null == pageSize) ? 10 : pageSize;
    }

    public boolean isFilterByEmpty() {
        if(null == this.filters
                || null == this.filters.getMapOfFilters()
                || this.filters.getMapOfFilters().isEmpty()){
            return true;
        }

        for (var entry : this.filters.getMapOfFilters().entrySet()) {
            if(entry.getValue() != null){
                return false;
            }
        }

        return true;
    }

}
