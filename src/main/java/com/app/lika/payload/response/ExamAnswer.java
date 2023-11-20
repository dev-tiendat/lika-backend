package com.app.lika.payload.response;

import lombok.Data;

@Data
public class ExamAnswer {
    private Long id;
    private String content;
    private String optionLetter;
}
