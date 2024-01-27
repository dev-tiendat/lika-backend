package com.app.lika.mapper;

import com.app.lika.model.ExamSchedule;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.response.ExamInfo;
import com.app.lika.payload.response.StudentExamSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ExamSetMapper.class})
public interface ExamScheduleMapper {
    @Mapping(target = "publishedAt", expression = "java(toTimestamp(examSchedule.getPublishedAt()))")
    @Mapping(target = "closedAt", expression = "java(toTimestamp(examSchedule.getClosedAt()))")
    @Mapping(target = "examSet.subject.id",source = "examSet.subject.subjectId")
    ExamScheduleDTO entityToExamScheduleDto(ExamSchedule examSchedule);

    @Mapping(target = "subjectName", source = "examSet.subject.subjectName")
    @Mapping(target = "publishedAt", expression = "java(toTimestamp(examSchedule.getPublishedAt()))")
    @Mapping(target = "closedAt", expression = "java(toTimestamp(examSchedule.getClosedAt()))")
    ExamInfo entityToExamInfo(ExamSchedule examSchedule);

    @Mapping(target = "subjectName", source = "examSet.subject.subjectName")
    @Mapping(target = "subjectId",source = "examSet.subject.subjectId")
    @Mapping(target = "examScheduleName", source = "title")
    @Mapping(target = "publishedAt", expression = "java(toTimestamp(examSchedule.getPublishedAt()))")
    @Mapping(target = "closedAt", expression = "java(toTimestamp(examSchedule.getClosedAt()))")
    StudentExamSchedule entityToStudentExamSchedule(ExamSchedule examSchedule);

    default Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
