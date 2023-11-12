package com.app.lika.repository;

import com.app.lika.model.examSet.ExamSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSetRepository extends JpaRepository<ExamSet, Long>, JpaSpecificationExecutor<ExamSet> {

}
