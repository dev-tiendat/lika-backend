package com.app.lika.exception;

import com.app.lika.payload.response.APIMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private APIMessageResponse apiMessageResponse;

    private String message;

    public AccessDeniedException(APIMessageResponse apiMessageResponse) {
        super();
        this.apiMessageResponse = apiMessageResponse;
    }

    public AccessDeniedException(String message) {
        super(message);
        this.message = message;
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIMessageResponse getApiMessageResponse() {
        return apiMessageResponse;
    }

    public void setApiMessageResponse(APIMessageResponse apiMessageResponse) {
        this.apiMessageResponse = apiMessageResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
