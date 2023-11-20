package com.app.lika.service.impl;

import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.model.Chapter;
import com.app.lika.model.Status;
import com.app.lika.model.Subject;
import com.app.lika.model.question.Level;
import com.app.lika.model.role.RoleName;
import com.app.lika.payload.response.statistic.*;
import com.app.lika.repository.*;
import com.app.lika.service.StatisticService;
import com.app.lika.utils.AppConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final ExamScheduleRepository examScheduleRepository;

    private final SubjectRepository subjectRepository;

    private final QuestionRepository questionRepository;

    private final ExamSetRepository examSetRepository;

    private final UserRepository userRepository;

    public StatisticServiceImpl(ExamScheduleRepository examScheduleRepository, SubjectRepository subjectRepository, QuestionRepository questionRepository, ExamSetRepository examSetRepository, UserRepository userRepository) {
        this.examScheduleRepository = examScheduleRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.examSetRepository = examSetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TotalAll getTotalAll() {
        Date now = new Date();
        long countExamSchedule = examScheduleRepository.count();
        long countExamScheduleCompleted = examScheduleRepository.countByClosedAtBeforeAndStatus(now, Status.ENABLE);
        long countExamScheduleIncomplete = examScheduleRepository.countByClosedAtAfterAndStatus(now, Status.ENABLE);
        long countExamScheduleCancel = examScheduleRepository.countByStatus(Status.DISABLE);
        TotalExamSchedule totalExamSchedule = new TotalExamSchedule(countExamSchedule, countExamScheduleCompleted, countExamScheduleIncomplete, countExamScheduleCancel);

        long countSubject = subjectRepository.count();
        TotalSubject totalSubject = new TotalSubject(countSubject);

        long countQuestion = questionRepository.count();
        long countDisableQuestion = questionRepository.countByStatus(Status.DISABLE);
        long countEnableQuestion = questionRepository.countByStatus(Status.ENABLE);
        TotalQuestion totalQuestion = new TotalQuestion(countQuestion, countDisableQuestion, countEnableQuestion);

        long countExamSet = examSetRepository.count();
        long countUsedExamSet = examSetRepository.countByStatus(com.app.lika.model.examSet.Status.USED);
        long countRemainingExamSet = examSetRepository.countByStatus(com.app.lika.model.examSet.Status.UNUSED);
        TotalExamSet totalExamSet = new TotalExamSet(countExamSet, countUsedExamSet, countRemainingExamSet);

        long countUser = userRepository.count();
        long countStudentUser = userRepository.countByRoleName(RoleName.ROLE_STUDENT.name());
        long countAdminUser = userRepository.countByRoleName(RoleName.ROLE_ADMIN.toString());
        long countTeacherUser = userRepository.countByRoleName(RoleName.ROLE_TEACHER.toString());
        TotalUser totalUser = new TotalUser(countUser, countAdminUser, countTeacherUser, countStudentUser);

        return new TotalAll(totalExamSchedule, totalSubject, totalQuestion, totalExamSet, totalUser);
    }

    @Override
    public StatisticQuestionBySubject getStatisticQuestionBySubject(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.SUBJECT_ID, id));
        Long totalEasyQuestions = questionRepository.countByChapter_Subject_SubjectIdAndLevel(id, Level.EASY);
        Long totalMediumQuestions = questionRepository.countByChapter_Subject_SubjectIdAndLevel(id, Level.MEDIUM);
        Long totalHardQuestions = questionRepository.countByChapter_Subject_SubjectIdAndLevel(id, Level.HARD);
        List<TotalQuestionLevelsByChapter> totalQuestionLevelsByChapters = new ArrayList<>();

        for (Chapter chapter : subject.getChapters()) {
            Long chapterId = chapter.getId();
            String chapterName = chapter.getChapterName();
            Long numberOfEasyQuestion = questionRepository.countByChapter_IdAndLevel(chapterId, Level.EASY);
            Long numberOfMediumQuestion = questionRepository.countByChapter_IdAndLevel(chapterId, Level.MEDIUM);
            Long numberOfHardQuestion = questionRepository.countByChapter_IdAndLevel(chapterId, Level.HARD);

            TotalQuestionLevelsByChapter totalQuestionLevelsByChapter = new TotalQuestionLevelsByChapter(chapterName, numberOfEasyQuestion, numberOfMediumQuestion, numberOfHardQuestion);
            totalQuestionLevelsByChapters.add(totalQuestionLevelsByChapter);
        }

        return new StatisticQuestionBySubject(totalEasyQuestions, totalMediumQuestions, totalHardQuestions, totalQuestionLevelsByChapters);
    }

    @Override
    public List<TotalQuestionOfSubject> getStatisticQuestions() {
        List<Subject> subjects = subjectRepository.findAll();
        List<TotalQuestionOfSubject> totalQuestionOfSubjects = new ArrayList<>();

        for (Subject subject : subjects) {
            String subjectName = subject.getSubjectName();
            Long numberOfQuestions = questionRepository.countByChapter_Subject_SubjectId(subject.getSubjectId());
            TotalQuestionOfSubject totalQuestionOfSubject = new TotalQuestionOfSubject(subjectName, numberOfQuestions);
            totalQuestionOfSubjects.add(totalQuestionOfSubject);
        }

        return totalQuestionOfSubjects;
    }
}
