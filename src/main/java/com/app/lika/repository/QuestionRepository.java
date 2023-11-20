package com.app.lika.repository;

import com.app.lika.model.Chapter;
import com.app.lika.model.Status;
import com.app.lika.model.question.Level;
import com.app.lika.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    Long countByChapter_IdAndLevel(Long chapterId, Level level);

    Long countByChapter_Subject_SubjectIdAndLevel(String subjectId, Level level);

    Long countByChapter_Subject_SubjectId(String subjectId);

    Long countByStatus(Status status);
    List<Question> findByChapterAndLevel(Chapter chapter, Level level);
}
