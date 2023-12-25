package com.app.lika.payload.DTO;

import com.app.lika.model.Status;
import com.app.lika.model.question.Level;
import com.app.lika.model.question.QuestionType;
import com.app.lika.payload.response.SubjectNameResponse;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;

    private String content;

    private String image;

    private Status status;

    private Level level;

    private QuestionType type;

    private ChapterDTO chapter;

    private UserSummary teacher;

    private SubjectNameResponse subject;

    private List<AnswerDTO> answers;
}
