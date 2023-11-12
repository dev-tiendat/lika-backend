package com.app.lika.service.impl;

import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.mapper.ExamScheduleMapper;
import com.app.lika.model.ExamSchedule;
import com.app.lika.model.Status;
import com.app.lika.model.examSet.ExamSet;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.ExamScheduleDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.request.ExamScheduleRequest;
import com.app.lika.repository.ExamScheduleRepository;
import com.app.lika.repository.ExamSetRepository;
import com.app.lika.repository.UserRepository;
import com.app.lika.service.ExamScheduleService;
import com.app.lika.utils.AppConstants;
import com.app.lika.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExamScheduleServiceImpl implements ExamScheduleService {

    private final ExamSetRepository examSetRepository;

    private final ExamScheduleRepository examScheduleRepository;

    private final UserRepository userRepository;

    private final ExamScheduleMapper examScheduleMapper;

    @Autowired
    public ExamScheduleServiceImpl(ExamSetRepository examSetRepository, ExamScheduleRepository examScheduleRepository, UserRepository userRepository, ExamScheduleMapper examScheduleMapper) {
        this.examSetRepository = examSetRepository;
        this.examScheduleRepository = examScheduleRepository;
        this.userRepository = userRepository;
        this.examScheduleMapper = examScheduleMapper;
    }

    @Override
    public PagedResponse<ExamScheduleDTO> getAllExamSchedule(PaginationCriteria paginationCriteria) {

        return null;
    }

    @Override
    public ExamScheduleDTO addExamSchedule(ExamScheduleRequest examScheduleRequest) {
        ExamSet examSet = examScheduleRequest.getExamSetId() == null ? null
                : examSetRepository.findById(examScheduleRequest.getExamSetId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, examScheduleRequest.getExamSetId()));
        List<User> students = examScheduleRequest.getStudentsUsernames()
                .stream().map(username -> {
                    User student = userRepository.getUserByUsername(username);

                    if (student.getStatus() == com.app.lika.model.user.Status.INACTIVE
                            || !student.getRoles().equals(RoleName.ROLE_STUDENT)
                    ) {
                        throw new BadRequestException("Bad Request");
                    }

                    return student;
                })
                .toList();

        ExamSchedule examSchedule = getExamSchedule(examScheduleRequest, examSet, students);

        return examScheduleMapper.entityToExamScheduleDto(examScheduleRepository.save(examSchedule));
    }

    @Override
    public ExamScheduleDTO updateExamSchedule(ExamScheduleRequest examScheduleRequest, long id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE,AppConstants.ID,id));

        if(examSchedule.getPublishedAt().before(new Date())){
            ExamSet examSet = examScheduleRequest.getExamSetId() == null ? null
                    : examSetRepository.findById(examScheduleRequest.getExamSetId())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SET, AppConstants.ID, examScheduleRequest.getExamSetId()));
            List<User> students = examScheduleRequest.getStudentsUsernames()
                    .stream().map(username -> {
                        User student = userRepository.getUserByUsername(username);

                        if (student.getStatus() == com.app.lika.model.user.Status.INACTIVE
                                || !student.getRoles().equals(RoleName.ROLE_STUDENT)
                        ) {
                            throw new BadRequestException("Bad Request");
                        }

                        return student;
                    })
                    .toList();
            Date publishedAt = new Date(examScheduleRequest.getPublishedAt());
            Date closedAt = new Date(publishedAt.getTime() + TimeUtils.convertMinutesToMilliseconds(examScheduleRequest.getTimeAllowance()));

            examSchedule.getStudents().clear();
            examSchedule.getStudents().addAll(students);
            examSchedule.setExamSet(examSet);
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
    public void deleteExamSchedule(Long id) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE,AppConstants.ID,id));

        if(examSchedule.getPublishedAt().before(new Date())){
            examScheduleRepository.delete(examSchedule);
        }

        throw new BadRequestException("The current exam schedule has expired and cannot be updated !");
    }

    @Override
    public void enableAndCancelExamSchedule(long id, Status status) {
        ExamSchedule examSchedule = examScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_SCHEDULE,AppConstants.ID,id));

        if(examSchedule.getPublishedAt().before(new Date())){
            examSchedule.setStatus(status);
            examScheduleRepository.save(examSchedule);
        }

        throw new BadRequestException("The current exam schedule has expired and cannot be updated !");
    }

    private ExamSchedule getExamSchedule(ExamScheduleRequest examScheduleRequest, ExamSet examSet, List<User> students) {
        Date publishedAt = new Date(examScheduleRequest.getPublishedAt());
        Date closedAt = new Date(publishedAt.getTime() + TimeUtils.convertMinutesToMilliseconds(examScheduleRequest.getTimeAllowance()));
        return new ExamSchedule(
                examScheduleRequest.getTitle(),
                examScheduleRequest.getSummary(),
                publishedAt,
                closedAt,
                examScheduleRequest.getTimeAllowance(),
                Status.ENABLE,
                examSet,
                students
        );
    }
}
