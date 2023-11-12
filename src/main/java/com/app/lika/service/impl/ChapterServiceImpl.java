package com.app.lika.service.impl;

import com.app.lika.model.Chapter;
import com.app.lika.repository.ChapterRepository;
import com.app.lika.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public List<Chapter> getAllChapterBySubjectId(String id) {
        return chapterRepository.findChapterBySubject_SubjectId(id);
    }
}
