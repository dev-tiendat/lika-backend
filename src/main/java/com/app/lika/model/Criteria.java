package com.app.lika.model;

import com.app.lika.model.question.Level;
import com.app.lika.model.examSet.ExamSet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "criteria")
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "level")
    private Level level;

    @NotNull
    @Column(name = "quantity")
    private Short quantity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamSet examSet;

    @NotNull
    @OneToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Criteria criteria = (Criteria) o;
        return Objects.equals(id, criteria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
