package com.app.lika.payload.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class SubmitExamRequest {
    List<Long> answers;

    public List<Long> getAnswers() {
        return answers == null ? new ArrayList<>() : new ArrayList<>(answers);
    }
}
