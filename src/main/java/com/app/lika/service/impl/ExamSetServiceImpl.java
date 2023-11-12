package com.app.lika.service.impl;

import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.mapper.ExamSetMapper;
import com.app.lika.model.Chapter;
import com.app.lika.model.Criteria;
import com.app.lika.model.Exam;
import com.app.lika.model.Subject;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.examSet.Status;
import com.app.lika.model.question.Level;
import com.app.lika.model.question.Question;
import com.app.lika.payload.DTO.CriteriaDTO;
import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.repository.*;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.ExamSetService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.RandomUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExamSetServiceImpl implements ExamSetService {

    private final SubjectRepository subjectRepository;
    private final ExamSetRepository examSetRepository;

    private final ChapterRepository chapterRepository;

    private final UserRepository userRepository;
    private final ExamSetMapper examSetMapper;
    private final QuestionRepository questionRepository;

    @Autowired
    public ExamSetServiceImpl(ExamSetMapper examSetMapper, ExamSetRepository examSetRepository, SubjectRepository subjectRepository, ChapterRepository chapterRepository,
                              QuestionRepository questionRepository, UserRepository userRepository) {
        this.examSetMapper = examSetMapper;
        this.examSetRepository = examSetRepository;
        this.subjectRepository = subjectRepository;
        this.chapterRepository = chapterRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ExamSet addExamSetService(CreateExamSetRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.SUBJECT_ID, request.getSubjectId()));
        Short quantityOfExams = request.getQuantityOfExam();
        checkQuantityOfExam(quantityOfExams);

        ExamSet examSet = new ExamSet();
        List<Criteria> criteriaList = new ArrayList<>();
        List<List<Question>> filteredQuestionsList = new ArrayList<>();

        for (CriteriaDTO criteriaRequest : request.getCriteria()) {
            Chapter chapter = getChapterByIdOrThrow(criteriaRequest.getChapterId());
            Level level = criteriaRequest.getLevel();
            Short quantity = criteriaRequest.getQuantity();

            List<Question> questionOptionsList = getQuestionsByChapterAndLevel(chapter, level, quantityOfExams);
            validateQuestionOptions(quantityOfExams, quantity, questionOptionsList.size());

            Collections.shuffle(questionOptionsList);
            List<Question> selectedQuestions = questionOptionsList.subList(0, quantityOfExams > 1 ? quantity : getMinimumOptionQuestion(quantity));
            filteredQuestionsList.add(selectedQuestions);

            Criteria newCriteria = createNewCriteria(examSet, chapter, level, quantity);
            criteriaList.add(newCriteria);
        }

        initializeExamSet(examSet, request.getName(), subject, criteriaList, quantityOfExams, filteredQuestionsList);

        return examSetRepository.save(examSet);
    }


    private Chapter getChapterByIdOrThrow(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CHAPTER, AppConstants.ID, chapterId));
    }

    private List<Question> getQuestionsByChapterAndLevel(Chapter chapter, Level level, Short quantityOfExams) {
        List<Question> questionOptionsList = questionRepository.findByChapterAndLevel(chapter, level);
        return questionOptionsList.subList(0, quantityOfExams > 1 ? quantityOfExams : getMinimumOptionQuestion(quantityOfExams));
    }

    private void validateQuestionOptions(Short quantityOfExams, Short quantity, int questionOptionsSize) {
        int minimumOptionQuestion = getMinimumOptionQuestion(quantityOfExams);
        if ((quantityOfExams > 1 && questionOptionsSize < minimumOptionQuestion * 2) || questionOptionsSize < quantity) {
            throw new BadRequestException("Insufficient %s level questions to generate exam questions!");
        }
    }

    private Criteria createNewCriteria(ExamSet examSet, Chapter chapter, Level level, Short quantity) {
        Criteria newCriteria = new Criteria();
        newCriteria.setChapter(chapter);
        newCriteria.setLevel(level);
        newCriteria.setExamSet(examSet);
        newCriteria.setQuantity(quantity);
        return newCriteria;
    }

    private void initializeExamSet(ExamSet examSet, String name, Subject subject, List<Criteria> criteriaList, Short quantityOfExams, List<List<Question>> filteredQuestionsList) {
        examSet.setName(name);
        examSet.setStatus(Status.UNUSED);
        examSet.setCriteria(criteriaList);
        examSet.setSubject(subject);

        List<Exam> exams = new ArrayList<>();
        List<Integer> codeList = RandomUtils.getRandomArrayInt(100, 300, quantityOfExams);
        for (int i = 0; i < quantityOfExams; i++) {
            Exam exam = new Exam();
            exam.setExamCode(codeList.get(i));
            exam.setExamSet(examSet);
            exam.setQuestions(filteredQuestionsList.get(i).subList(0, criteriaList.get(i).getQuantity()));
            exams.add(exam);
        }
        examSet.setExams(exams);
    }

    private int getMinimumOptionQuestion(Short quantityOfExams) {
        return (int) Math.ceil((double) quantityOfExams / 2);
    }

    public void checkQuantityOfExam(Short quantity) {
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Number of exam questions must be between 1 and 10!");
        }
    }

}
