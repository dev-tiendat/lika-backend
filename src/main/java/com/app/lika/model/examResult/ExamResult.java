package com.app.lika.model.examResult;

import com.app.lika.model.Exam;
import com.app.lika.model.ExamResultDetail;
import com.app.lika.model.ExamSchedule;
import com.app.lika.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "exam_results")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "grade")
    private Float grade;

    @NotNull
    @Column(name = "number_of_right_answer")
    private Integer numberOfRightAnswer;

    @NotNull
    @Column(name = "is_completed")
    private Status status;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "exam_schedule_id")
    private ExamSchedule examSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "student_id")
    private User student;

    @OneToMany(mappedBy = "examResult", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonBackReference
    private List<ExamResultDetail> examResultDetails;

    @Transient
    private Integer numberOfQuestions;

    public ExamResult(Float grade, Exam exam, ExamSchedule examSchedule, User student) {
        this.grade = grade;
        this.status = Status.INCOMPLETE;
        this.numberOfRightAnswer = 0;
        this.numberOfQuestions = 0;
        this.exam = exam;
        this.examSchedule = examSchedule;
        this.student = student;
    }

    public ExamResult(Float grade, Status status, Exam exam, ExamSchedule examSchedule, User student, List<ExamResultDetail> examResultDetails) {
        this.grade = grade;
        this.status = status;
        this.exam = exam;
        this.examSchedule = examSchedule;
        this.student = student;
        this.examResultDetails = examResultDetails;
    }

    public void setExamResultDetails(List<ExamResultDetail> examResultDetails) {
        this.examResultDetails = new ArrayList<>(examResultDetails);
    }
}
