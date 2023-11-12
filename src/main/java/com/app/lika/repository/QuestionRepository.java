package com.app.lika.repository;

import com.app.lika.model.Chapter;
import com.app.lika.model.question.Level;
import com.app.lika.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
//    Integer countByChapter_IdAndLevel(Long chapterId, Level level);

    List<Question> findByChapterAndLevel(Chapter chapter, Level level);
}
