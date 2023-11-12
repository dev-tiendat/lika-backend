package com.app.lika.repository;

import com.app.lika.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,String>, JpaSpecificationExecutor<Subject> {

    @Override
    Page<Subject> findAll(Specification specification, Pageable pageable);

    Optional<Subject> findById(String id);

    Boolean existsBySubjectId(String id);
}
