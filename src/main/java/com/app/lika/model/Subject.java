package com.app.lika.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "subjects")
public class Subject {
    @Id
    @Size(max = 12)
    @Column(name = "subject_id")
    private String subjectId;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(name = "subject_name")
    private String subjectName;

    @NotNull
    @Column(name = "credit_hours")
    private Short creditHours;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Chapter> chapters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId != null && Objects.equals(subjectId, subject.subjectId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
