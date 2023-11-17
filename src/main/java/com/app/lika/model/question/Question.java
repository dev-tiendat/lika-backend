package com.app.lika.model.question;

import com.app.lika.model.Exam;
import com.app.lika.model.Status;
import com.app.lika.model.answer.Answer;
import com.app.lika.model.Chapter;
import com.app.lika.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.codehaus.jackson.annotate.JsonManagedReference;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions", uniqueConstraints = {@UniqueConstraint(columnNames = {"question_content"})})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "question_content")
    private String content;

    @Column(name = "image")
    private String image;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "level")
    private Level level;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "question_type")
    private QuestionType type;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonBackReference
    private List<Answer> answers;

    @ManyToMany(mappedBy = "questions", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Exam> exams;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
