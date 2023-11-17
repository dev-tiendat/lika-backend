package com.app.lika.model.examSet;

import com.app.lika.model.Criteria;
import com.app.lika.model.Exam;
import com.app.lika.model.Subject;
import com.app.lika.model.audit.DateAudit;
import com.app.lika.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exam_sets")
public class ExamSet extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "examSet",cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Exam> exams;

    @OneToMany(mappedBy = "examSet", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private List<Criteria> criteria;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    public ExamSet(String name, Status status, Subject subject, User createdBy, User updatedBy) {
        this.name = name;
        this.status = status;
        this.subject = subject;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

}
