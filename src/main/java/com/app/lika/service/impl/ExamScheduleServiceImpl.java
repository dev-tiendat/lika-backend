package com.app.lika.service.impl;

import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.mapper.ExamScheduleMapper;
import com.app.lika.model.ExamSchedule;
import com.app.lika.model.Status;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.question.Question;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.pagination.SortBy;
import com.app.lika.payload.pagination.SortOrder;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.repository.ExamScheduleRepository;
import com.app.lika.repository.ExamSetRepository;
import com.app.lika.repository.RoleRepository;
import com.app.lika.repository.UserRepository;
import com.app.lika.repository.specification.ExamScheduleSpecification;
import com.app.lika.repository.specification.QuestionSpecification;
import com.app.lika.service.ExamScheduleService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.TimeUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {

    private final ExamSetRepository examSetRepository;

    private final ExamScheduleRepository examScheduleRepository;

    private final UserRepository userRepository;

    private final ExamScheduleMapper examScheduleMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public ExamScheduleServiceImpl(ExamSetRepository examSetRepository, ExamScheduleRepository examScheduleRepository, UserRepository userRepository, ExamScheduleMapper examScheduleMapper,
                                   RoleRepository roleRepository) {
        this.examSetRepository = examSetRepository;
        this.examScheduleRepository = examScheduleRepository;
        this.userRepository = userRepository;
        this.examScheduleMapper = examScheduleMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public PagedResponse<ExamScheduleDTO> getAllExamSchedule(PaginationCriteria paginationCriteria) {
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

        Page<ExamSchedule> examSchedule;
        if (!paginationCriteria.isFilterByEmpty()
                || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            ExamScheduleSpecification questionSpecification = new ExamScheduleSpecification(paginationCriteria.getFilters() == null
                    ? null : paginationCriteria.getFilters().getMapOfFilters(),
                    paginationCriteria.getQuery());
            examSchedule = examScheduleRepository.findAll(questionSpecification, pageRequest);
        } else {
            examSchedule = examScheduleRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                examSchedule.getContent().stream().map(examScheduleMapper::entityToExamScheduleDto).toList(),
                examSchedule.getNumber(),
                examSchedule.getSize(),
                examSchedule.getTotalElements(),
                examSchedule.getTotalPages(),
                examSchedule.isLast()
        );
    }

    @Override
    public ExamScheduleDTO addExamSchedule(ExamScheduleRequest examScheduleRequest) {
        ExamSet examSet = examScheduleRequest.getExamSetId() == null ? null
                : examSetRepository.findById(examScheduleRequest.getExamSetId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, examScheduleRequest.getExamSetId()));
        User teacher = userRepository.getUserByUsername(examScheduleRequest.getTeacherUsername());
        if (!teacher.getRoles().contains(roleRepository.findByName(RoleName.ROLE_TEACHER).get()))
            throw new BadRequestException(examScheduleRequest.getTeacherUsername() + " is not a teacher !");

        List<User> students = examScheduleRequest.getStudentsUsernames()
                .stream().map(username -> {
                    User student = userRepository.getUserByUsername(username);

                    if (student.getStatus() == com.app.lika.model.user.Status.INACTIVE
//                            || !student.getRoles().equals(RoleName.ROLE_STUDENT)
                    ) {
                        throw new BadRequestException("You cannot add teachers or admins to the exam list");
                    }

                    return student;
                })
                .toList();

        ExamSchedule examSchedule = getExamSchedule(examScheduleRequest, examSet, teacher, students);

        return examScheduleMapper.entityToExamScheduleDto(examScheduleRepository.save(examSchedule));
    }

    @Override
    public ExamScheduleDTO updateExamSchedule(ExamScheduleRequest examScheduleRequest, long id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE, AppConstants.ID, id));
        User teacher = userRepository.getUserByUsername(examScheduleRequest.getTeacherUsername());
        if (!teacher.getRoles().contains(roleRepository.findByName(RoleName.ROLE_TEACHER).get()))
            throw new BadRequestException(examScheduleRequest.getTeacherUsername() + " is not a teacher !");

        if (examSchedule.getPublishedAt().before(new Date())) {
            ExamSet examSet = examScheduleRequest.getExamSetId() == null ? null
                    : examSetRepository.findById(examScheduleRequest.getExamSetId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, examScheduleRequest.getExamSetId()));
            List<User> students = examScheduleRequest.getStudentsUsernames()
                    .stream().map(username -> {
                        User student = userRepository.getUserByUsername(username);

                        if (student.getStatus() == com.app.lika.model.user.Status.INACTIVE
                                || !student.getRoles().contains(RoleName.ROLE_STUDENT)
                        ) {
                            throw new BadRequestException("You cannot add teachers or admins to the exam list");
                        }

                        return student;
                    })
                    .toList();
            Date publishedAt = new Date(examScheduleRequest.getPublishedAt());
            if (publishedAt.after(new Date()))
                throw new BadRequestException("You cannot schedule your exam before this time");
            Date closedAt = new Date(publishedAt.getTime() + TimeUtils.convertMinutesToMilliseconds(examScheduleRequest.getTimeAllowance()));

            examSchedule.getStudents().clear();
            examSchedule.getStudents().addAll(students);
            examSchedule.setExamSet(examSet);
            examSchedule.setTeacher(teacher);
            examSchedule.setTitle(examScheduleRequest.getTitle());
            examSchedule.setSummary(examScheduleRequest.getSummary());
            examSchedule.setTimeAllowance(examScheduleRequest.getTimeAllowance());
            examSchedule.setPublishedAt(publishedAt);
            examSchedule.setClosedAt(closedAt);

            return examScheduleMapper.entityToExamScheduleDto(examScheduleRepository.save(examSchedule));
        }

        throw new BadRequestException("The current exam schedule has expired and cannot be updated !");
    }

    @Override
    public ExamScheduleDTO deleteExamSchedule(Long id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE, AppConstants.ID, id));

        if (examSchedule.getPublishedAt().after(new Date())) {
            examScheduleRepository.delete(examSchedule);

            return examScheduleMapper.entityToExamScheduleDto(examSchedule);
        }

        throw new BadRequestException("The current exam schedule has expired and cannot be deleted !");
    }

    @Override
    public ExamScheduleDTO enableAndCancelExamSchedule(long id, Status status) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE, AppConstants.ID, id));
        if (examSchedule.getPublishedAt().after(new Date())) {
            examSchedule.setStatus(status);

            return examScheduleMapper.entityToExamScheduleDto(examScheduleRepository.save(examSchedule));
        }

        throw new BadRequestException("The current exam schedule has expired and cannot be updated !");
    }

    private ExamSchedule getExamSchedule(ExamScheduleRequest examScheduleRequest, ExamSet examSet, User teacher, List<User> students) {
        Date publishedAt = new Date(examScheduleRequest.getPublishedAt());
        if (publishedAt.before(new Date()))
            throw new BadRequestException("You cannot schedule your exam before this time");

        Date closedAt = new Date(publishedAt.getTime() + TimeUtils.convertMinutesToMilliseconds(examScheduleRequest.getTimeAllowance()));
        return new ExamSchedule(
                examScheduleRequest.getTitle(),
                examScheduleRequest.getSummary(),
                publishedAt,
                closedAt,
                examScheduleRequest.getTimeAllowance(),
                Status.ENABLE,
                examSet,
                teacher,
                students
        );
    }
}
