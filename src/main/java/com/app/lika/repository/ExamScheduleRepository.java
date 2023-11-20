package com.app.lika.repository;

import com.app.lika.model.ExamSchedule;
import com.app.lika.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule,Long>, JpaSpecificationExecutor<ExamSchedule> {
    Integer countByClosedAtBeforeAndStatus(Date date, Status status);

    Integer countByClosedAtAfterAndStatus(Date date, Status status);

    Integer countByStatus(Status status);
}
