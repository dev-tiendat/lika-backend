package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@Data
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private APIResponse apiResponse;

    public AppException(String message) {
        super(message);
        this.apiResponse = new APIResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
