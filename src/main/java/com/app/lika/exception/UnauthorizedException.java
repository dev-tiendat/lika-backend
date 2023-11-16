package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private APIResponse apiResponse;

    private String message;

    public UnauthorizedException(APIResponse apiResponse) {
        super();
        apiResponse.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        this.apiResponse = apiResponse;
    }

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
        this.apiResponse = new APIResponse(HttpStatus.UNAUTHORIZED.value(), message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
