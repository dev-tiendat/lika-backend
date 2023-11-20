package com.app.lika.mapper;

import com.app.lika.model.ExamSchedule;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.response.ExamInfo;
import com.app.lika.payload.response.StudentExamSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, ExamSetMapper.class})
public interface ExamScheduleMapper {
    ExamScheduleDTO entityToExamScheduleDto(ExamSchedule examSchedule);

    @Mapping(target = "subjectName", source = "examSet.subject.subjectName")
    ExamInfo entityToExamInfo(ExamSchedule examSchedule);

    @Mapping(target = "subjectName", source = "examSet.subject.subjectName")
    @Mapping(target = "subjectId",source = "examSet.subject.subjectId")
    @Mapping(target = "examScheduleName", source = "title")
    StudentExamSchedule entityToStudentExamSchedule(ExamSchedule examSchedule);
}
