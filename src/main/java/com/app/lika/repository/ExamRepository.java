package com.app.lika.repository;

import com.app.lika.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}
