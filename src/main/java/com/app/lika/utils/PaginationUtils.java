package com.app.lika.utils;

import com.app.lika.exception.APIException;
import com.app.lika.exception.BadRequestException;
import com.app.lika.model.user.User;
import com.app.lika.payload.pagination.SortBy;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class PaginationUtils {
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero");
        }

        if (size < 1) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Size number cannot be les than 1.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public static void sortColumnCheck (final String[] sortColumns,
                                        final String sortBy){
        if (sortBy != null && !Arrays.asList(sortColumns).contains(sortBy)) {
            throw new BadRequestException("Invalid sort column");
        }
    }
}
