package com.app.lika.mapper;

import com.app.lika.model.answer.Answer;
import com.app.lika.payload.DTO.AnswerDTO;
import com.app.lika.payload.request.AnswerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnswerMapper {
    @Mapping(target = "id", ignore = true)
    Answer answerRequestToEntity(AnswerRequest answerRequest);

    AnswerDTO entityToAnswerDto(Answer answer);
}
