package com.app.lika.payload.pagination;

import com.app.lika.exception.BadRequestException;

public enum SortOrder {

    ASC("asc"),
    DESC("desc");

    private final String value;

    SortOrder(String v) {
        value = v;
    }

    public static SortOrder fromValue(String v){
        for(SortOrder c : SortOrder.values()){
            if(c.getValue().equals(v)){
                return c;
            }
        }

        throw new BadRequestException("Sort method invalid!");
    }

    public String getValue() {
        return value;
    }
}
