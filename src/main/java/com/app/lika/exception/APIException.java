package com.app.lika.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class APIException extends RuntimeException {

    private static final long serialVersionUID = -6593330219878485669L;

    private final HttpStatus status;

    private final String message;

    private final Integer code;

    public APIException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
        this.code = status.value();
    }

    public APIException(HttpStatus status, String message, Integer code) {
        super();
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public APIException(HttpStatus status, String message, Throwable exception) {
        super(exception);
        this.status = status;
        this.message = message;
        this.code = status.value();
    }
}
