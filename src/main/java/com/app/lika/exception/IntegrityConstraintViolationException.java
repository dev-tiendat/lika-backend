package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class IntegrityConstraintViolationException extends RuntimeException {
    private APIResponse apiResponse;

    private String resourceName;

    public IntegrityConstraintViolationException(String resourceName) {
        this.resourceName = resourceName;
        this.setApiResponse();
    }

    void setApiResponse() {
        String message = String.format("Data of %s is constrained and cannot be deleted", this.resourceName);

        apiResponse = new APIResponse(HttpStatus.CONFLICT.value(), message);
    }

}
