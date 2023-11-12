package com.app.lika.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChapterRequest {
    @NotNull
    private Short chapterNumber;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 255)
    private String chapterName;

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName == null ? null : chapterName.trim();
    }
}
