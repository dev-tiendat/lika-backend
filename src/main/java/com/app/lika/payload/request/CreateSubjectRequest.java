package com.app.lika.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class CreateSubjectRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max= 50)
    private String subjectId;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 50)
    private String subjectName;

    @NotNull
    private Short creditHours;

    @NotNull
    private List<String> chapterNames;

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName.trim();
    }

    public void setChapterNames(List<String> chapterNames) {
        if(chapterNames == null){
            this.chapterNames = null;
            return;
        }

        this.chapterNames = Collections.unmodifiableList(chapterNames);
    }

    public List<String> getChapterNames() {
        return new ArrayList<>(chapterNames);
    }
}
