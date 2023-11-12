package com.app.lika.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonPropertyOrder({
        "code",
        "success",
        "error",
        "message"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIMessageResponse {
    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("error")
    private String error;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonIgnore
    private HttpStatus status;

    public APIMessageResponse() {

    }

    public APIMessageResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public APIMessageResponse(int code, String error, Boolean success, String message) {
        this.code = code;
        this.error = error;
        this.success = success;
        this.message = message;
    }

    public APIMessageResponse(int code, Boolean success, String message, HttpStatus status) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.status = status;
    }

    public APIMessageResponse(Boolean success, String message, HttpStatus status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }
}
