package com.app.lika.service.impl;

import com.app.lika.exception.AppException;
import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.mapper.*;
import com.app.lika.model.Exam;
import com.app.lika.model.ExamResultDetail;
import com.app.lika.model.ExamSchedule;
import com.app.lika.model.answer.Answer;
import com.app.lika.model.answer.Correct;
import com.app.lika.model.examResult.ExamResult;
import com.app.lika.model.examResult.Status;
import com.app.lika.model.question.Question;
import com.app.lika.model.question.QuestionType;
import com.app.lika.payload.DTO.UserSummary;
import com.app.lika.payload.response.*;
import com.app.lika.repository.AnswerRepository;
import com.app.lika.repository.ExamResultRepository;
import com.app.lika.repository.ExamScheduleRepository;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.TestService;
import com.app.lika.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    private final ExamResultRepository examResultRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final AnswerRepository answerRepository;
    private final UserMapper userMapper;
    private final ExamScheduleMapper examScheduleMapper;
    private final QuestionMapper questionMapper;

    private final ExamResultMapper examResultMapper;

    @Autowired
    public TestServiceImpl(ExamResultRepository examResultRepository, ExamScheduleRepository examScheduleRepository, UserMapper userMapper, ExamScheduleMapper examScheduleMapper, QuestionMapper questionMapper, ExamResultMapper examResultMapper, AnswerRepository answerRepository) {
        this.examResultRepository = examResultRepository;
        this.examScheduleRepository = examScheduleRepository;
        this.userMapper = userMapper;
        this.examScheduleMapper = examScheduleMapper;
        this.questionMapper = questionMapper;
        this.examResultMapper = examResultMapper;
        this.answerRepository = answerRepository;
    }

    @Override
    public TakeExamResponse takeExam(Long scheduleId, UserPrincipal currentUser) {
        ExamSchedule examSchedule = examScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE, AppConstants.ID, scheduleId));
        Date now = new Date();
        if (examSchedule.getPublishedAt().after(now)) {
            throw new BadRequestException("This exam hasn't started yet. Waiting for the scheduled time !");
        }

        if (examSchedule.getClosedAt().before(now)) {
            throw new BadRequestException("This exam has gone past the scheduled time !");
        }

        ExamResult examResult = examResultRepository.findByStudent_IdAndExamSchedule_Id(
                        currentUser.getId(),
                        examSchedule.getId())
                .orElseThrow(() -> new AppException("Your exam question is currently unavailable!"));
        UserSummary student = userMapper.userPrincipalToUserSummary(currentUser);
        List<ExamQuestion> questions = examResult.getExam().getQuestions().stream().map((questionMapper::entityToExamQuestion)).toList();
        ExamInfo examInfo = examScheduleMapper.entityToExamInfo(examSchedule);
        examInfo.setExamCode(examResult.getExam().getExamCode());

        return new TakeExamResponse(examInfo, student, questions);
    }

    @Override
    public ExamResultGrade submitExam(Long scheduleId, UserPrincipal currentUser, List<Long> studentAnswers) {
        ExamResult examResult = examResultRepository.findByStudent_IdAndExamSchedule_Id(currentUser.getId(), scheduleId)
                .orElseThrow(() -> new AppException("Your exam question is currently unavailable!"));
        List<Question> questions = getQuestions(examResult);
        int numberOfQuestions = questions.size();
        int numberOfRightAnswer = getNumberOfRightAnswer(studentAnswers, questions);
        float grade = (float) (numberOfRightAnswer * 10) / numberOfQuestions;
        List<ExamResultDetail> examResultDetails = studentAnswers.stream().map((answerId) -> {
            Answer answer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new BadRequestException("Bad request"));
            return new ExamResultDetail(answer, examResult);
        }).toList();
        examResult.setStatus(Status.COMPLETED);
        examResult.setGrade(grade);
        examResult.setNumberOfQuestions(numberOfQuestions);
        examResult.setNumberOfRightAnswer(numberOfRightAnswer);
        examResult.getExamResultDetails().clear();
        examResult.getExamResultDetails().addAll(examResultDetails);

        return examResultMapper.entityToExamResultPoint(examResultRepository.save(examResult));
    }

    @Override
    public ExamResultDetailResponse getOldExam(Long scheduleId, UserPrincipal currentUser) {
        ExamResult examResult = examResultRepository.findByStudent_IdAndExamSchedule_Id(currentUser.getId(), scheduleId)
                .orElseThrow(() -> new AppException("Your exam question is currently unavailable!"));
        ExamSchedule examSchedule = examResult.getExamSchedule();
        if (examSchedule.getPublishedAt().after(new Date())) {
            throw new BadRequestException("This exam hasn't started yet !");
        }
        UserSummary student = userMapper.userPrincipalToUserSummary(currentUser);
        List<ExamQuestion> questions = examResult.getExam().getQuestions().stream().map((questionMapper::entityToExamQuestion)).toList();
        ExamInfo examInfo = examScheduleMapper.entityToExamInfo(examSchedule);
        examInfo.setExamCode(examResult.getExam().getExamCode());
        List<Long> studentAnswers = examResult.getExamResultDetails().stream()
                .map(examResultDetail -> examResultDetail.getAnswer().getId()).toList();

        return new ExamResultDetailResponse(examInfo,student,questions,studentAnswers);
    }

    private List<Question> getQuestions(ExamResult examResult) {
        ExamSchedule examSchedule = examResult.getExamSchedule();
        if (examResult.getStatus().equals(Status.COMPLETED)) {
            throw new BadRequestException("This test has been completed!");
        }

        Date now = new Date();
        if (examSchedule.getPublishedAt().after(now)) {
            throw new BadRequestException("This exam hasn't started yet. Waiting for the scheduled time !");
        }

        if (examSchedule.getClosedAt().before(now)) {
            throw new BadRequestException("This exam has gone past the scheduled time !");
        }
        Exam exam = examResult.getExam();

        return exam.getQuestions();
    }

    private int getNumberOfRightAnswer(List<Long> studentAnswers, List<Question> questions) {
        int numberOfRightAnswer = 0;

        for (Question question : questions) {
            List<Answer> answers = question.getAnswers();

            if (question.getType().equals(QuestionType.SINGLE)) {
                numberOfRightAnswer += countSingleCorrectAnswers(answers, studentAnswers);
            } else {
                if (areAllMultipleAnswersCorrect(answers, studentAnswers)) {
                    numberOfRightAnswer++;
                }
            }
        }

        return numberOfRightAnswer;
    }

    private int countSingleCorrectAnswers(List<Answer> answers, List<Long> studentAnswers) {
        for (Answer answer : answers) {
            if (answer.getIsCorrect() == Correct.TRUE && studentAnswers.contains(answer.getId())) {
                return 1;
            }
        }
        return 0;
    }

    private boolean areAllMultipleAnswersCorrect(List<Answer> answers, List<Long> studentAnswers) {
        for (Answer answer : answers) {
            if ((answer.getIsCorrect() == Correct.FALSE && studentAnswers.contains(answer.getId()))
                    || (answer.getIsCorrect() == Correct.TRUE && !studentAnswers.contains(answer.getId()))) {
                return false;
            }
        }
        return true;
    }


}
