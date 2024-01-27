package com.app.lika.mapper;

import com.app.lika.model.examResult.ExamResult;
import com.app.lika.payload.response.ExamGrade;
import com.app.lika.payload.response.ExamResultGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExamResultMapper {
    @Mapping(source = "examSchedule.title", target = "examName")
    ExamResultGrade entityToExamResultPoint(ExamResult examResult);

    @Mapping(target = "examScheduleName", source = "examSchedule.title")
    @Mapping(target = "timeAllowance", source = "examSchedule.timeAllowance")
    @Mapping(target = "publishedAt", expression = "java(toTimestamp(examResult.getExamSchedule().getPublishedAt()))")
    ExamGrade entityToExamGrade(ExamResult examResult);

    default Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
