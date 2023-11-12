package com.app.lika.payload.request;

import com.app.lika.payload.DTO.CriteriaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateExamSetRequest {
        @NotNull
        @NotBlank
        private String name;

        @NotNull
        @NotBlank
        private String subjectId;

        @NotNull
        private Short quantityOfExam;

//        private List<Long> questionIdList;

        private List<CriteriaDTO> criteria;

}
