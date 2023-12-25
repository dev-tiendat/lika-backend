package com.app.lika.mapper;

import com.app.lika.model.question.Question;
import com.app.lika.payload.DTO.QuestionDTO;
import com.app.lika.payload.request.QuestionRequest;
import com.app.lika.payload.response.ExamQuestion;
import com.app.lika.payload.response.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AnswerMapper.class, UserMapper.class, SubjectMapper.class})
public interface QuestionMapper {
    @Mapping(target = "image", ignore = true)
    Question questionRequestToEntity(QuestionRequest questionRequest);

    @Mapping(source = "teacher", target = "teacher")
    @Mapping(target = "subject", source= "chapter.subject" )
    @Mapping(target = "status", source = "status")
    QuestionDTO entityToQuestionDto(Question question);

    QuestionResponse entityToQuestionResponse(QuestionResponse questionResponse);

    ExamQuestion entityToExamQuestion(Question question);

}
