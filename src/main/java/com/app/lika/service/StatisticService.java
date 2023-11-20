package com.app.lika.service;

import com.app.lika.payload.response.statistic.StatisticQuestion;
import com.app.lika.payload.response.statistic.StatisticQuestionBySubject;
import com.app.lika.payload.response.statistic.TotalAll;
import com.app.lika.payload.response.statistic.TotalQuestionOfSubject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticService {
    TotalAll getTotalAll();

    StatisticQuestionBySubject getStatisticQuestionBySubject(String id);

    List<TotalQuestionOfSubject> getStatisticQuestions();
}
