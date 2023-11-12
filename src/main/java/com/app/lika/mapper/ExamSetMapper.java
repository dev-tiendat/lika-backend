package com.app.lika.mapper;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.payload.request.CreateExamSetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExamSetMapper {
    ExamSet createExamSetDtoToEntity(CreateExamSetRequest createExamSetRequest);
}
