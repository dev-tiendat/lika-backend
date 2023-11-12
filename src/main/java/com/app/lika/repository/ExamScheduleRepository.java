package com.app.lika.repository;

import com.app.lika.model.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule,Long>, JpaSpecificationExecutor<ExamSchedule> {

}
