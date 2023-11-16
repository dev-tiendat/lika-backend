package com.app.lika.service;

import com.app.lika.payload.DTO.ChapterDTO;

import java.util.List;

public interface ChapterService {
    List<ChapterDTO> getAllChapterBySubjectId(String id);
}
