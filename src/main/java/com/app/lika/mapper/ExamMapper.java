package com.app.lika.mapper;

import com.app.lika.payload.DTO.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {QuestionMapper.class})
public interface ExamMapper {
    ExamDTO entityToExamDto(ExamDTO examDTO);
}
