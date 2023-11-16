package com.app.lika.mapper;

import com.app.lika.model.Chapter;
import com.app.lika.model.Criteria;
import com.app.lika.payload.DTO.CriteriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {Chapter.class})
public interface CriteriaMapper {
    CriteriaDTO entityToCriteriaDto(Criteria criteria);
}
