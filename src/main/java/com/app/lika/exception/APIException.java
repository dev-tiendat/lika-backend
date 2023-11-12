package com.app.lika.exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

    private static final long serialVersionUID = -6593330219878485669L;

    private final HttpStatus status;
    private Integer code;
    private final String message;

    public APIException(HttpStatus status, int code, String message) {
        super();
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public APIException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
        this.code = null;
    }

    public APIException(HttpStatus status, int code, String message, Throwable exception) {
        super(exception);
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

}
