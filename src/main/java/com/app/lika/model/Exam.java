package com.app.lika.model;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "exam_code")
    private Integer examCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExamSet examSet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "exam_question", joinColumns = @JoinColumn(name = "exam_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"))
    private List<Question> questions;
}
