package com.app.lika.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder
public class APIResponse<T> {
    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120341L;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;
}
