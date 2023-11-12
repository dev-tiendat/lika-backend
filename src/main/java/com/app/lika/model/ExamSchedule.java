package com.app.lika.model;

import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "exam_schedule")
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @NotBlank
    @Column(name = "sumary")
    private String summary;

    @NotNull
    @Column(name = "published_at")
    private Date publishedAt;

    @NotNull
    @Column(name = "closed_at")
    private Date closedAt;

    @NotNull
    @Column(name = "time_allowance")
    private Integer timeAllowance;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_set_id")
    private ExamSet examSet;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "student_exam_schedule", joinColumns = @JoinColumn(name = "exam_schedule_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
    @JsonBackReference
    private List<User> students;

    public ExamSchedule(String title, String summary, Date publishedAt, Date closedAt, Integer timeAllowance, Status status, ExamSet examSet, List<User> students) {
        this.title = title;
        this.summary = summary;
        this.publishedAt = publishedAt;
        this.closedAt = closedAt;
        this.timeAllowance = timeAllowance;
        this.status = status;
        this.examSet = examSet;
        this.students = students;
    }
}
