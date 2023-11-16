package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private APIResponse apiResponse;

    public BadRequestException(APIResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    public BadRequestException(String message) {
        super(message);
        this.apiResponse = new APIResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
