package com.app.lika.repository;

import com.app.lika.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    Integer countChapterBySubject_SubjectId(String subjectId);

    List<Chapter> findChapterBySubject_SubjectId(String subjectId);

    Boolean existsChapterBySubject_SubjectId(String subjectId);
}
