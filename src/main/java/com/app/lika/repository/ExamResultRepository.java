package com.app.lika.repository;

import com.app.lika.model.examResult.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByStudent_IdAndExamSchedule_Id(long studentId,long examScheduleId);
}
