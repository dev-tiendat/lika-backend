package com.app.lika.service.impl;

import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.IntegrityConstraintViolationException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.exception.UnauthorizedException;
import com.app.lika.mapper.AnswerMapper;
import com.app.lika.mapper.QuestionMapper;
import com.app.lika.model.answer.Answer;
import com.app.lika.model.answer.Correct;
import com.app.lika.model.question.Question;
import com.app.lika.model.question.QuestionType;
import com.app.lika.model.Status;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.QuestionDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.pagination.SortBy;
import com.app.lika.payload.pagination.SortOrder;
import com.app.lika.payload.request.AnswerRequest;
import com.app.lika.payload.request.QuestionRequest;
import com.app.lika.repository.AnswerRepository;
import com.app.lika.repository.ChapterRepository;
import com.app.lika.repository.QuestionRepository;
import com.app.lika.repository.UserRepository;
import com.app.lika.repository.specification.QuestionSpecification;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.QuestionService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.TextUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final ChapterRepository chapterRepository;

    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;

    private final AnswerMapper answerMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, UserRepository userRepository,
                               ChapterRepository chapterRepository, AnswerMapper answerMapper, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.userRepository = userRepository;
        this.chapterRepository = chapterRepository;
        this.answerMapper = answerMapper;
        this.answerRepository = answerRepository;
    }

    @Override
    public PagedResponse<QuestionDTO> getAllQuestions(PaginationCriteria paginationCriteria) {
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

        Page<Question> questions;
        if (!paginationCriteria.isFilterByEmpty()
                || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            QuestionSpecification questionSpecification = new QuestionSpecification(paginationCriteria.getFilters() == null
                    ? null : paginationCriteria.getFilters().getMapOfFilters(),
                    paginationCriteria.getQuery());
            questions = questionRepository.findAll(questionSpecification, pageRequest);
        } else {
            questions = questionRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                questions.getContent().stream().map(questionMapper::entityToQuestionDto).toList(),
                questions.getNumber(),
                questions.getSize(),
                questions.getTotalElements(),
                questions.getTotalPages(),
                questions.isLast()
        );
    }

    @Override
    @Transactional
    public QuestionDTO addQuestion(QuestionRequest questionRequest, UserPrincipal currentUser) {
        if (checkQuestionRequestValid(questionRequest)) {

            Question question = questionMapper.questionRequestToEntity(questionRequest);
            question.setStatus(Status.DISABLE);
            question.setTeacher(userRepository.getUser(currentUser));
            question.setChapter(chapterRepository.findById(questionRequest.getChapterId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.CHAPTER, AppConstants.ID, questionRequest.getChapterId())));

            List<Answer> answers = question.getAnswers();
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                answer.setId(null);
                answer.setOptionLetter(TextUtils.LIST_CHAR[i]);
                answer.setQuestion(question);
            }

            return questionMapper.entityToQuestionDto(questionRepository.save(question));
        }

        throw new BadRequestException("Request is invalid !");
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(QuestionRequest questionRequest, UserPrincipal currentUser, Long id) {
        if (checkQuestionRequestValid(questionRequest)) {
            User user = userRepository.getUser(currentUser);
            Question question = questionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.QUESTION, AppConstants.ID, id));
            if (user.getId().equals(question.getTeacher().getId())
                    || currentUser.getAuthorities()
                    .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))
            ) {
                if (!question.getExams().isEmpty()) {
                    throw new BadRequestException("This question is already available for the exam, you cannot update it !");
                }
                question.setChapter(chapterRepository.findById(questionRequest.getChapterId())
                        .orElseThrow(() -> new BadRequestException("ChapterId not found !")));
                question.setContent(questionRequest.getContent());
                question.setLevel(questionRequest.getLevel());
                question.setType(questionRequest.getType());

                List<Answer> updatedAnswers = new ArrayList<>();
                List<AnswerRequest> answersRequest = questionRequest.getAnswers();
                for (int i = 0; i < answersRequest.size(); i++) {
                    Answer answer;

                    AnswerRequest answerRequest = answersRequest.get(i);
                    if (answerRequest.getId() == null) {
                        answer = answerMapper.answerRequestToEntity(answerRequest);
                        answer.setQuestion(question);
                    } else {
                        answer = answerRepository.findById(answerRequest.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.ANSWER, AppConstants.ID, answerRequest.getId()));

                        answer.setContent(answerRequest.getContent());
                        answer.setIsCorrect(answerRequest.getIsCorrect());
                    }
                    answer.setOptionLetter(TextUtils.LIST_CHAR[i]);
                    updatedAnswers.add(answer);
                }

                question.getAnswers().clear();
                question.getAnswers().addAll(updatedAnswers);

                return questionMapper.entityToQuestionDto(questionRepository.save(question));
            }

            throw new UnauthorizedException("You don't have permission to update question of : " + id);
        }

        throw new BadRequestException("Request is invalid !");
    }

    @Override
    public QuestionDTO deleteQuestion(Long id, UserPrincipal currentUser) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.QUESTION, AppConstants.ID, id));

        if (question.getTeacher().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            try {
                questionRepository.delete(question);

                return questionMapper.entityToQuestionDto(question);
            } catch (Exception ex) {
                throw new IntegrityConstraintViolationException(AppConstants.QUESTION);
            }
        }

        throw new UnauthorizedException("You don't have permission to question of :" + id);
    }

    @Override
    public QuestionDTO enableOrDisableQuestion(Long id, UserPrincipal currentUser, Status status) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.QUESTION, AppConstants.ID, id));

        if (question.getTeacher().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            question.setStatus(status);

            return questionMapper.entityToQuestionDto(questionRepository.save(question));
        }

        throw new UnauthorizedException("You don't have permission to question of : " + id);
    }

    private boolean checkQuestionRequestValid(QuestionRequest questionRequest) {
        List<AnswerRequest> answers = questionRequest.getAnswers();
        int countAnswerCorrect = (int) answers.stream()
                .filter(answer -> answer.getIsCorrect() == Correct.TRUE)
                .count();

        boolean isMultipleChoice = questionRequest.getType() == QuestionType.MULTIPLE;
        if (isMultipleChoice) {
            return countAnswerCorrect > 1 && countAnswerCorrect < answers.size();
        } else {
            return countAnswerCorrect == 1;
        }
    }
}
