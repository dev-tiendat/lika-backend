package com.app.lika.payload.DTO;

import lombok.Data;

import java.util.List;

@Data
public class SubjectDTO {

    private String id;

    private String subjectName;

    private Short creditHours;

    private List<ChapterDTO> chapters;
}
