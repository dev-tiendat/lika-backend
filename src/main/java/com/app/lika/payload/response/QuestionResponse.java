package com.app.lika.payload.response;

import com.app.lika.model.question.Level;
import com.app.lika.model.question.QuestionType;
import com.app.lika.payload.DTO.AnswerDTO;
import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private Long id;

    private String content;

    private String image;

    private Level level;

    private QuestionType type;

    private List<AnswerDTO> answers;
}
