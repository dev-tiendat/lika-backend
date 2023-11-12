package com.app.lika.service;

import com.app.lika.model.Chapter;

import java.util.List;

public interface ChapterService {
    List<Chapter> getAllChapterBySubjectId(String id);
}
