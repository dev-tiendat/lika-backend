package com.app.lika.service.impl;

import com.app.lika.exception.*;
import com.app.lika.mapper.ExamSetMapper;
import com.app.lika.model.*;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.examSet.Status;
import com.app.lika.model.question.Level;
import com.app.lika.model.question.Question;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.User;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.pagination.SortBy;
import com.app.lika.payload.pagination.SortOrder;
import com.app.lika.payload.request.CriteriaRequest;
import com.app.lika.payload.DTO.ExamSetDTO;
import com.app.lika.payload.request.CreateExamSetRequest;
import com.app.lika.repository.*;
import com.app.lika.repository.specification.ExamScheduleSpecification;
import com.app.lika.repository.specification.ExamSetSpecification;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.ExamSetService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.RandomUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public PagedResponse<ExamSetDTO> getAllExamSets(PaginationCriteria paginationCriteria) {
        Pageable pageRequest;
        SortBy sort = paginationCriteria.getSortBy();
        if (sort.getSortOrder().equals(SortOrder.ASC)) {
            pageRequest = PageRequest.of(paginationCriteria.getPageNumber(),
                    paginationCriteria.getPageSize(),
                    Sort.by(sort.getField()).ascending());
        } else {
            pageRequest = PageRequest.of(paginationCriteria.getPageNumber(),
                    paginationCriteria.getPageSize(),
                    Sort.by(sort.getField()).descending());
        }

        Page<ExamSet> examSchedule;
        if (!paginationCriteria.isFilterByEmpty()
                || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            ExamSetSpecification examSetSpecification = new ExamSetSpecification(paginationCriteria.getFilters() == null
                    ? null : paginationCriteria.getFilters().getMapOfFilters(),
                    paginationCriteria.getQuery());
            examSchedule = examSetRepository.findAll(examSetSpecification, pageRequest);
        } else {
            examSchedule = examSetRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                examSchedule.getContent().stream().map(examSetMapper::entityToExamSetDto).toList(),
                examSchedule.getNumber(),
                examSchedule.getSize(),
                examSchedule.getTotalElements(),
                examSchedule.getTotalPages(),
                examSchedule.isLast()
        );
    }

    @Override
    @Transactional
    public ExamSetDTO addExamSet(CreateExamSetRequest request, UserPrincipal currentUser) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.SUBJECT_ID, request.getSubjectId()));
        Short quantityOfExams = request.getQuantityOfExam();
        checkQuantityOfExam(quantityOfExams);
        User user = userRepository.getUser(currentUser);
        ExamSet examSet = new ExamSet(
                request.getName(),
                Status.UNUSED,
                subject,
                user,
                user
        );

        List<Criteria> criteriaList = new ArrayList<>();
        List<List<Question>> filteredQuestionsList = new ArrayList<>();
        for (CriteriaRequest criteriaRequest : request.getCriteria()) {
            Chapter chapter = getChapterByIdOrThrow(criteriaRequest.getChapterId());
            if (!subject.getChapters().contains(chapter)) {
                throw new BadRequestException(String.format("ChapterId %s does not exist in the %s subject", chapter.getId(), subject.getSubjectName()));
            }

            Level level = criteriaRequest.getLevel();
            Short quantity = criteriaRequest.getQuantity();
            List<Question> questionOptionsList = questionRepository.findByChapterAndLevel(chapter, level);

            int minimumQuestions = (int) (quantity * Math.ceil((quantityOfExams + 1) / 2.0));
            if (questionOptionsList.size() < minimumQuestions)
                throw new AppException("Questions bank is not enough to create exam questions !");

            Collections.shuffle(questionOptionsList);
            List<Question> selectedQuestions = questionOptionsList.subList(0, minimumQuestions);
            filteredQuestionsList.add(selectedQuestions);

            criteriaList.add(new Criteria(level, quantity, examSet, chapter));
        }
        examSet.setCriteria(criteriaList);
        List<Exam> exams = generateExam(quantityOfExams, filteredQuestionsList, criteriaList, examSet);
        examSet.setExams(exams);

        return examSetMapper.entityToExamSetDto(examSetRepository.save(examSet));
    }

    @Override
    public ExamSetDTO updateExamSet(Long id, CreateExamSetRequest request, UserPrincipal currentUser) {
        ExamSet examSet = examSetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, id));
        if (examSet.getCreatedBy().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.SUBJECT_ID, request.getSubjectId()));
            Short quantityOfExams = request.getQuantityOfExam();
            checkQuantityOfExam(quantityOfExams);
            User user = userRepository.getUser(currentUser);
            examSet.setName(request.getName());
            examSet.setSubject(subject);
            examSet.setUpdatedBy(user);

            List<Criteria> criteriaList = new ArrayList<>();
            List<List<Question>> filteredQuestionsList = new ArrayList<>();
            for (CriteriaRequest criteriaRequest : request.getCriteria()) {
                Chapter chapter = getChapterByIdOrThrow(criteriaRequest.getChapterId());
                if (!subject.getChapters().contains(chapter)) {
                    throw new BadRequestException(String.format("ChapterId %s does not exist in the %s subject", chapter.getId(), subject.getSubjectName()));
                }

                Level level = criteriaRequest.getLevel();
                Short quantity = criteriaRequest.getQuantity();
                List<Question> questionOptionsList = questionRepository.findByChapterAndLevel(chapter, level);

                int minimumQuestions = (int) (quantity * Math.ceil((quantityOfExams + 1) / 2.0));
                if (questionOptionsList.size() < minimumQuestions)
                    throw new AppException("Questions bank is not enough to create exam questions !");

                Collections.shuffle(questionOptionsList);
                List<Question> selectedQuestions = questionOptionsList.subList(0, minimumQuestions);
                filteredQuestionsList.add(selectedQuestions);

                criteriaList.add(new Criteria(level, quantity, examSet, chapter));
            }
            examSet.getCriteria().clear();
            examSet.getCriteria().addAll(criteriaList);
            List<Exam> exams = generateExam(quantityOfExams, filteredQuestionsList, criteriaList, examSet);
            examSet.getExams().clear();
            examSet.getExams().addAll(exams);

            return examSetMapper.entityToExamSetDto(examSetRepository.save(examSet));
        }

        throw new UnauthorizedException("You don't have permission update to Exam set of :" + id);
    }

    @Override
    public ExamSetDTO deleteExamSet(Long id, UserPrincipal currentUser) {
        ExamSet examSet = examSetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, id));

        if (examSet.getCreatedBy().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            try {
                examSetRepository.delete(examSet);

            } catch (Exception ex) {
                throw new IntegrityConstraintViolationException(AppConstants.EXAM_SET);
            }
            return examSetMapper.entityToExamSetDto(examSet);
        }

        throw new UnauthorizedException("You don't have permission delete to Exam set of :" + id);
    }

    private List<Exam> generateExam(int quantityOfExams, List<List<Question>> filteredQuestionsList, List<Criteria> criteriaList, ExamSet examSet) {
        List<Exam> exams = new ArrayList<>();
        List<Integer> codeList = RandomUtils.getRandomArrayInt(100, 500, quantityOfExams);
        for (int i = 0; i < quantityOfExams; i++) {
            List<Question> questions = new ArrayList<>();
            for (int j = 0; j < filteredQuestionsList.size(); j++) {
                List<Question> filteredQuestion = filteredQuestionsList.get(j);
                Collections.shuffle(filteredQuestion);
                questions.addAll(filteredQuestion.subList(0, criteriaList.get(j).getQuantity()));
            }
            exams.add(new Exam(codeList.get(i), examSet, questions));
        }

        return exams;
    }

    private Chapter getChapterByIdOrThrow(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CHAPTER, AppConstants.ID, chapterId));
    }

    public void checkQuantityOfExam(Short quantity) {
        if (quantity < 1 || quantity > 10) {
            throw new BadRequestException("Number of exam questions must be between 1 and 6 !");
        }
    }

}
