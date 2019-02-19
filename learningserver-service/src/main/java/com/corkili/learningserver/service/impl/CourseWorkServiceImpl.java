package com.corkili.learningserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.repo.CourseWorkRepository;
import com.corkili.learningserver.service.CourseWorkService;
import com.corkili.learningserver.service.SubmittedCourseWorkService;
import com.corkili.learningserver.service.WorkQuestionService;

@Slf4j
@Service
public class CourseWorkServiceImpl extends ServiceImpl<CourseWork, com.corkili.learningserver.po.CourseWork> implements CourseWorkService {

    @Autowired
    private CourseWorkRepository courseWorkRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WorkQuestionService workQuestionService;

    @Autowired
    private SubmittedCourseWorkService submittedCourseWorkService;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.CourseWork, Long> repository() {
        return courseWorkRepository;
    }

    @Override
    protected String entityName() {
        return "courseWork";
    }

    @Override
    protected CourseWork newBusinessObject() {
        return new CourseWork();
    }

    @Override
    protected com.corkili.learningserver.po.CourseWork newPersistObject() {
        return new com.corkili.learningserver.po.CourseWork();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteCourseWork(Long courseWorkId) {
        ServiceResult serviceResult;
        // delete course work
        if (delete(courseWorkId)) {
            serviceResult = ServiceResult.successResultWithMesage("delete course work success");
        } else {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete course work success");
        }
        // delete associated work question
        serviceResult = serviceResult.mergeFrom(workQuestionService.deleteWorkQuestionByBelongCourseWorkId(courseWorkId), true);
        // delete associated submitted course work
        serviceResult = serviceResult.mergeFrom(submittedCourseWorkService.deleteSubmittedCourseWorkByBelongCourseWorkId(courseWorkId), true);
        return serviceResult;
    }

    @Override
    public ServiceResult deleteCourseWorkByBelongCourseId(Long belongCourseId) {
        List<Long> courseWorkIdList = courseWorkRepository.findAllCourseWorkIdByBelongCourseId(belongCourseId);
        courseWorkRepository.deleteAllByBelongCourseId(belongCourseId);
        for (Long id : courseWorkIdList) {
            // delete associated work question
            workQuestionService.deleteWorkQuestionByBelongCourseWorkId(id);
            // delete associated submitted course work
            submittedCourseWorkService.deleteSubmittedCourseWorkByBelongCourseWorkId(id);
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete course work success");
    }

    @Override
    public ServiceResult createCourseWork(CourseWork courseWork, Collection<WorkQuestion> workQuestions) {
        if (StringUtils.isBlank(courseWork.getWorkName())) {
            return recordErrorAndCreateFailResultWithMessage("create course work error: workName is empty");
        }
        if (courseWork.getWorkName().length() > 100) {
            return recordErrorAndCreateFailResultWithMessage("create course work error: length of workName > 100");
        }
        if (courseWork.getDeadline() != null && courseWork.getDeadline().before(new Date())) {
            return recordErrorAndCreateFailResultWithMessage("create course work error: deadline is before now");
        }
        if (courseWork.getBelongCourseId() == null || !courseRepository.existsById(courseWork.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create course work error: belong course [{}] not exist",
                    courseWork.getBelongCourseId() == null ? "" : courseWork.getBelongCourseId());
        }
        Optional<CourseWork> courseWorkOptional = create(courseWork);
        if (!courseWorkOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create course work error: save CourseWork failed");
        }
        courseWork = courseWorkOptional.get();
        ServiceResult serviceResult = workQuestionService.createOrUpdateWorkQuestionsForCourseWork(workQuestions, courseWork.getId());
        String msg = "create course work success";
        if (serviceResult.isFail()) {
            msg += ", but " + serviceResult.msg();
        } else {
            msg += ", and " + serviceResult.msg();
        }
        List<WorkQuestion> workQuestionList = (List<WorkQuestion>) serviceResult.extra(List.class);
        if (workQuestionList == null) {
            workQuestionList = new ArrayList<>();
        }
        return ServiceResult.successResult(msg, CourseWork.class, courseWork, List.class, workQuestionList);
    }

    @Override
    public ServiceResult updateCourseWork(CourseWork courseWork, Collection<WorkQuestion> workQuestions) {
        if (StringUtils.isBlank(courseWork.getWorkName())) {
            return recordErrorAndCreateFailResultWithMessage("update course work error: workName is empty");
        }
        if (courseWork.getWorkName().length() > 100) {
            return recordErrorAndCreateFailResultWithMessage("update course work error: length of workName > 100");
        }
        if (courseWork.getDeadline() != null && courseWork.getDeadline().before(new Date())) {
            return recordErrorAndCreateFailResultWithMessage("update course work error: deadline is before now");
        }
        if (courseWork.getBelongCourseId() == null || !courseRepository.existsById(courseWork.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("update course work error: belong course [{}] not exist",
                    courseWork.getBelongCourseId() == null ? "" : courseWork.getBelongCourseId());
        }
        Optional<CourseWork> courseWorkOptional = update(courseWork);
        if (!courseWorkOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update course work error: save CourseWork failed");
        }
        courseWork = courseWorkOptional.get();
        String msg = "create course work success";
        List<WorkQuestion> workQuestionList = null;
        if (workQuestions != null) {
            ServiceResult serviceResult = workQuestionService.createOrUpdateWorkQuestionsForCourseWork(workQuestions, courseWork.getId());
            if (serviceResult.isFail()) {
                msg += ", but " + serviceResult.msg();
            } else {
                msg += ", and " + serviceResult.msg();
            }
            workQuestionList = (List<WorkQuestion>) serviceResult.extra(List.class);
        }
        if (workQuestionList == null) {
            workQuestionList = (List<WorkQuestion>) workQuestionService.findAllWorkQuestionByBelongCourseWorkId(
                    courseWork.getId()).extra(List.class);
        }
        return ServiceResult.successResult(msg, CourseWork.class, courseWork, List.class, workQuestionList);
    }
}
