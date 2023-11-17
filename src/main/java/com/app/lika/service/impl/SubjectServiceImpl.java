package com.app.lika.service.impl;

import com.app.lika.exception.BadRequestException;
import com.app.lika.exception.IntegrityConstraintViolationException;
import com.app.lika.exception.ResourceNotFoundException;
import com.app.lika.mapper.ChapterMapper;
import com.app.lika.mapper.SubjectMapper;
import com.app.lika.model.Chapter;
import com.app.lika.model.Subject;
import com.app.lika.payload.DTO.ChapterDTO;
import com.app.lika.payload.DTO.SubjectDTO;
import com.app.lika.payload.pagination.PagedResponse;
import com.app.lika.payload.pagination.PaginationCriteria;
import com.app.lika.payload.pagination.SortBy;
import com.app.lika.payload.pagination.SortOrder;
import com.app.lika.payload.request.CreateSubjectRequest;
import com.app.lika.payload.response.SubjectNameResponse;
import com.app.lika.repository.ChapterRepository;
import com.app.lika.repository.SubjectRepository;
import com.app.lika.repository.specification.SubjectSpecification;
import com.app.lika.service.SubjectService;
import com.app.lika.utils.AppConstants;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final ChapterRepository chapterRepository;

    private final SubjectMapper subjectMapper;

    private final ChapterMapper chapterMapper;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, ChapterRepository chapterRepository, SubjectMapper subjectMapper, ChapterMapper chapterMapper) {
        this.subjectRepository = subjectRepository;
        this.chapterRepository = chapterRepository;
        this.subjectMapper = subjectMapper;
        this.chapterMapper = chapterMapper;
    }

    @Override
    public PagedResponse<SubjectDTO> getAllSubjects(PaginationCriteria paginationCriteria) {
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

        Page<Subject> subjects;
        if (!paginationCriteria.isFilterByEmpty()
                || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            SubjectSpecification subjectSpecification = new SubjectSpecification(paginationCriteria.getFilters() == null
                    ? null : paginationCriteria.getFilters().getMapOfFilters(),
                    paginationCriteria.getQuery());
            subjects = subjectRepository.findAll(subjectSpecification, pageRequest);
        } else {
            subjects = subjectRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                subjects.getContent().stream().map(subjectMapper::entityToSubjectDto).toList(),
                subjects.getNumber(),
                subjects.getSize(),
                subjects.getTotalElements(),
                subjects.getTotalPages(),
                subjects.isLast()
        );
    }

    @Override
    public PagedResponse<SubjectNameResponse> getAllSubjectsHaveChapter(PaginationCriteria paginationCriteria) {
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

        Page<Subject> subjects;
        if (!paginationCriteria.isFilterByEmpty()
                || StringUtils.isNotEmpty(paginationCriteria.getQuery())) {
            SubjectSpecification subjectSpecification = new SubjectSpecification(paginationCriteria.getFilters() == null
                    ? null : paginationCriteria.getFilters().getMapOfFilters(),
                    paginationCriteria.getQuery());
            subjects = subjectRepository.findAll(subjectSpecification, pageRequest);
        } else {
            subjects = subjectRepository.findAll(pageRequest);
        }

        return new PagedResponse<>(
                subjects.getContent().stream().map(subjectMapper::entityToSubjectNameResponse).toList(),
                subjects.getNumber(),
                subjects.getSize(),
                subjects.getTotalElements(),
                subjects.getTotalPages(),
                subjects.isLast()
        );
    }

    @Override
    public Boolean checkSubjectIdAvailability(String id) {
        return !subjectRepository.existsBySubjectId(id);
    }

    @Override
    @Transactional
    public SubjectDTO addSubject(CreateSubjectRequest subjectRequest) {
        if (Boolean.TRUE.equals(subjectRepository.existsBySubjectId(subjectRequest.getSubjectId()))) {
            throw new BadRequestException("SubjectId is already taken!");
        }
        Subject subject = subjectMapper.createSubjectRequestToEntity(subjectRequest);

        List<Chapter> chapters = new ArrayList<>();
        short chapterNumber = 1;
        for (String chapterName : subjectRequest.getChapterNames()) {
            Chapter chapter = new Chapter();
            chapter.setChapterNumber(chapterNumber);
            chapter.setChapterName(chapterName);
            chapter.setSubject(subject);
            chapters.add(chapter);
            chapterNumber++;
        }
        subject.setChapters(chapters);

        return subjectMapper.entityToSubjectDto(subjectRepository.save(subject));
    }

    @Override
    @Transactional
    public SubjectDTO updateSubject(SubjectDTO subjectRequest, String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.ID, id));

        if (!subject.getSubjectName().equals(subjectRequest.getSubjectName()) ||
                !Objects.equals(subject.getCreditHours(), subjectRequest.getCreditHours())) {
            throw new BadRequestException("You cannot update subjects that already have chapters.");
        }

        List<Chapter> updatedChapters = new ArrayList<>();
        short chapterNumber = 1;
        for (ChapterDTO chapterDTO : subjectRequest.getChapters()) {
            if (chapterDTO.getId() == null) {
                Chapter newChapter = chapterMapper.chapterDtoToEntity(chapterDTO);
                newChapter.setChapterNumber(chapterNumber);
                newChapter.setSubject(subject);
                updatedChapters.add(newChapter);
            } else {
                Chapter existingChapter = chapterRepository.findById(chapterDTO.getId())
                        .orElseThrow(() -> new BadRequestException("Chapter not found!"));
                existingChapter.setChapterName(chapterDTO.getChapterName());
                existingChapter.setChapterNumber(chapterNumber);
                updatedChapters.add(existingChapter);
            }
            chapterNumber++;
        }

        subject.getChapters().clear();
        subject.getChapters().addAll(updatedChapters);

        subject.setSubjectName(subjectRequest.getSubjectName());
        subject.setCreditHours(subjectRequest.getCreditHours());

        return subjectMapper.entityToSubjectDto(subjectRepository.save(subject));
    }

    @Override
    public SubjectDTO deleteSubject(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.SUBJECT, AppConstants.ID, id));
        try {
            subjectRepository.delete(subject);

            return subjectMapper.entityToSubjectDto(subject);
        } catch (Exception e) {
            throw new IntegrityConstraintViolationException(AppConstants.SUBJECT);
        }
    }
}
