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

    private List<ChapterRequest> chapters;

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName.trim();
    }

    public void setChapters(List<ChapterRequest> chapters) {
        if(chapters == null){
            this.chapters = null;
            return;
        }

        this.chapters = Collections.unmodifiableList(chapters);
    }

    public List<ChapterRequest> getChapters() {
        return chapters == null ? null : new ArrayList<>(chapters);
    }
}
