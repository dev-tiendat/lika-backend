package com.app.lika.mapper;

import com.app.lika.model.Subject;
import com.app.lika.payload.DTO.SubjectDTO;
import com.app.lika.payload.request.CreateSubjectRequest;
import com.app.lika.payload.response.SubjectNameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubjectMapper {
    @Mapping(target = "chapters", ignore = true)
    Subject createSubjectRequestToEntity(CreateSubjectRequest createSubjectRequest);

    @Mapping(source = "subjectId", target = "id")
    SubjectDTO entityToSubjectDto(Subject subject);

    @Mapping(source = "subjectId", target = "id")
    SubjectNameResponse entityToSubjectNameResponse(Subject subject);
}
