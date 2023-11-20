package com.app.lika.mapper;

import com.app.lika.model.examResult.ExamResult;
import com.app.lika.payload.response.ExamResultGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExamResultMapper {
    @Mapping(source = "examSchedule.title", target = "examName")
    ExamResultGrade entityToExamResultPoint(ExamResult examResult);
}
