package com.app.lika.controller;

import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.statistic.StatisticQuestion;
import com.app.lika.payload.response.statistic.StatisticQuestionBySubject;
import com.app.lika.payload.response.statistic.TotalAll;
import com.app.lika.payload.response.statistic.TotalQuestionOfSubject;
import com.app.lika.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/statistics")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/totalAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<TotalAll>> getTotalAll(){
        TotalAll data = statisticService.getTotalAll();
        APIResponse<TotalAll> response = new APIResponse("Get all total statistic successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/totalQuestion/subject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StatisticQuestionBySubject>> getTotalSubject(@PathVariable(name = "id") String id){
        StatisticQuestionBySubject data = statisticService.getStatisticQuestionBySubject(id);
        APIResponse<StatisticQuestionBySubject> response = new APIResponse("Get all total statistic by SubjectId successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/totalQuestion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<TotalQuestionOfSubject>>> getTotalSubject(){
        List<TotalQuestionOfSubject> data = statisticService.getStatisticQuestions();
        APIResponse<List<TotalQuestionOfSubject>> response = new APIResponse("Get all total statistic Question successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
