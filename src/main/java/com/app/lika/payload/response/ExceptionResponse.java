package com.app.lika.payload.response;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ExceptionResponse {
    private String error;
    private Integer status;
    private List<String> messages;
    private Instant timestamp;

    public ExceptionResponse(String error, Integer status, List<String> messages) {
        setMessage(messages);
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
    }

    public List<String> getMessages(){
        return this.messages == null ? null : new ArrayList<>(this.messages);
    }

    public final void setMessage(List<String> messages){
        if(messages == null){
            this.messages = null;
        }
        else{
            this.messages = Collections.unmodifiableList(messages);
        }
    }
}
