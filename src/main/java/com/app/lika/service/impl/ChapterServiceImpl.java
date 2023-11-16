package com.app.lika.service.impl;

import com.app.lika.mapper.ChapterMapper;
import com.app.lika.model.Chapter;
import com.app.lika.payload.DTO.ChapterDTO;
import com.app.lika.repository.ChapterRepository;
import com.app.lika.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;

    private final ChapterMapper chapterMapper;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository, ChapterMapper chapterMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterMapper = chapterMapper;
    }

    @Override
    public List<ChapterDTO> getAllChapterBySubjectId(String id) {
        return chapterRepository.findChapterBySubject_SubjectId(id).stream()
                .map(chapterMapper::entityToChapterDto)
                .collect(Collectors.toList());
    }
}
