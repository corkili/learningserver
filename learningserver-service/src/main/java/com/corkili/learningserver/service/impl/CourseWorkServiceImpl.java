package com.corkili.learningserver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.common.ServiceResult;
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
}
