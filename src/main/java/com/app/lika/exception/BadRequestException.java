package com.app.lika.exception;

import com.app.lika.payload.response.APIMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private APIMessageResponse apiMessageResponse;

    public BadRequestException(APIMessageResponse apiMessageResponse) {
        super();
        this.apiMessageResponse = apiMessageResponse;
    }

    public BadRequestException(String message) {
        super(message);
        this.apiMessageResponse = new APIMessageResponse(Boolean.FALSE, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIMessageResponse getApiMessageResponse() {
        return this.apiMessageResponse;
    }
}
