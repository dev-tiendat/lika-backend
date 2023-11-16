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
import com.app.lika.model.user.User;
import com.app.lika.payload.request.CriteriaRequest;
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
    public ExamSetDTO addExamSetService(CreateExamSetRequest request, UserPrincipal currentUser) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.SUBJECT_ID, request.getSubjectId()));
        Short quantityOfExams = request.getQuantityOfExam();
        checkQuantityOfExam(quantityOfExams);

        ExamSet examSet = new ExamSet();
        List<Criteria> criteriaList = new ArrayList<>();
        List<List<Question>> filteredQuestionsList = new ArrayList<>();

        for (CriteriaRequest criteriaRequest : request.getCriteria()) {
            Chapter chapter = getChapterByIdOrThrow(criteriaRequest.getChapterId());
            Level level = criteriaRequest.getLevel();
            Short quantity = criteriaRequest.getQuantity();

            List<Question> questionOptionsList = questionRepository.findByChapterAndLevel(chapter, level);
            Collections.shuffle(questionOptionsList);
            List<Question> selectedQuestions = questionOptionsList.subList(0, (int) (quantity * Math.ceil((quantityOfExams + 1) / 2.0)));
            filteredQuestionsList.add(selectedQuestions);

            Criteria newCriteria = createNewCriteria(examSet, chapter, level, quantity);
            criteriaList.add(newCriteria);
        }

        examSet.setName(request.getName());
        examSet.setStatus(Status.UNUSED);
        examSet.setCriteria(criteriaList);
        examSet.setSubject(subject);
        User user = userRepository.getUser(currentUser);
        examSet.setCreatedBy(user);
        examSet.setUpdatedBy(user);

        List<Exam> exams = new ArrayList<>();
        List<Integer> codeList = RandomUtils.getRandomArrayInt(100, 500, quantityOfExams);
        for (int i = 0; i < quantityOfExams; i++) {
            List<Question> questions = new ArrayList<>();
            for (int j = 0; j < filteredQuestionsList.size(); j++) {
                List<Question> filteredQuestion = filteredQuestionsList.get(j);
                Collections.shuffle(filteredQuestion);
                questions.addAll(filteredQuestion.subList(0, criteriaList.get(j).getQuantity()));
            }

            Exam exam = new Exam();
            exam.setExamCode(codeList.get(i));
            exam.setExamSet(examSet);
            exam.setQuestions(questions);
            exams.add(exam);
        }
        examSet.setExams(exams);

        return examSetMapper.entityToExamSetDto(examSetRepository.save(examSet));
    }


    private Chapter getChapterByIdOrThrow(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CHAPTER, AppConstants.ID, chapterId));
    }

    private Criteria createNewCriteria(ExamSet examSet, Chapter chapter, Level level, Short quantity) {
        Criteria newCriteria = new Criteria();
        newCriteria.setChapter(chapter);
        newCriteria.setLevel(level);
        newCriteria.setExamSet(examSet);
        newCriteria.setQuantity(quantity);
        return newCriteria;
    }

    public void checkQuantityOfExam(Short quantity) {
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Number of exam questions must be between 1 and 10 !");
        }
    }

}
