package com.app.lika.model;

import com.app.lika.model.answer.Answer;
import com.app.lika.model.examResult.ExamResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "exam_result_details")
public class ExamResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_result_id")
    private ExamResult examResult;

    public ExamResultDetail(Answer answer, ExamResult examResult) {
        this.answer = answer;
        this.examResult = examResult;
    }
}
