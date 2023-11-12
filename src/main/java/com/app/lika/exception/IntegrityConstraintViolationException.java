package com.app.lika.exception;

import com.app.lika.payload.response.APIMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(HttpStatus.CONFLICT)
public class IntegrityConstraintViolationException extends RuntimeException {
    private APIMessageResponse apiResponse;

    private String resourceName;

    public IntegrityConstraintViolationException(String resourceName) {
        this.resourceName = resourceName;
        this.setApiResponse();
    }

    String getResourceName() {
        return resourceName;
    }

    APIMessageResponse getApiResponse(){
        return apiResponse;
    }

    void setApiResponse() {
        String message = String.format("Data of %s is constrained and cannot be deleted", this.resourceName);

        apiResponse = new APIMessageResponse(Boolean.FALSE, message);
    }

}
