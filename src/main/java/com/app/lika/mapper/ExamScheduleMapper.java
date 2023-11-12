package com.app.lika.mapper;

import com.app.lika.model.ExamSchedule;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface ExamScheduleMapper {
    ExamScheduleDTO entityToExamScheduleDto(ExamSchedule examSchedule);
}
