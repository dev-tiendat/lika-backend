package com.app.lika.mapper;

import com.app.lika.model.question.Question;
import com.app.lika.payload.DTO.QuestionDTO;
import com.app.lika.payload.request.QuestionRequest;
import com.app.lika.payload.response.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AnswerMapper.class, UserMapper.class})
public interface QuestionMapper {
    @Mapping(target = "image", ignore = true)
    Question questionRequestToEntity(QuestionRequest questionRequest);

    @Mapping(source = "teacher", target = "teacher")
    QuestionDTO entityToQuestionDto(Question question);

    QuestionResponse entityToQuestionResponse(QuestionResponse questionResponse);
}
