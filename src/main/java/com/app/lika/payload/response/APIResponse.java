package com.app.lika.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@JsonPropertyOrder
public class APIResponse<T> implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120341L;

    @JsonProperty("errorCode")
    private Integer errorCode;

    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonIgnore
    private HttpStatus status;

    public APIResponse() {
        this.errorCode = 0;
        this.message = null;
        this.data = null;
    }

    public APIResponse(String message) {
        this.message = message;
    }

    public APIResponse(int errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    public APIResponse(String message, T data) {
        this();
        this.message = message;
        this.data = data;
    }

    public APIResponse(int errorCode, String message, HttpStatus status) {
        this(errorCode, message);
        this.status = status;
    }

}
