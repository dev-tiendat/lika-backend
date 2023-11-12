package com.app.lika.mapper;

import com.app.lika.model.Chapter;
import com.app.lika.payload.DTO.ChapterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChapterMapper {
    Chapter chapterDtoToEntity(ChapterDTO chapterDTO);
}
