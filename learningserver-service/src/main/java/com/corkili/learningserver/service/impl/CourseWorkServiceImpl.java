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
import com.corkili.learningserver.repo.SubmittedCourseWorkRepository;
import com.corkili.learningserver.repo.WorkQuestionRepository;
import com.corkili.learningserver.service.CourseWorkService;
import com.corkili.learningserver.service.SubmittedCourseWorkService;
import com.corkili.learningserver.service.WorkQuestionService;

@Slf4j
@Service
public class CourseWorkServiceImpl extends ServiceImpl<CourseWork, com.corkili.learningserver.po.CourseWork> implements CourseWorkService {

    @Autowired
    private CourseWorkRepository courseWorkRepository;

    @Autowired
    private WorkQuestionRepository workQuestionRepository;

    @Autowired
    private SubmittedCourseWorkRepository submittedCourseWorkRepository;

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
        List<Long> workQuestionIdList = workQuestionRepository.findAllWorkQuestionIdByBelongCourseWorkId(courseWorkId);
        for (Long id : workQuestionIdList) {
            serviceResult = serviceResult.mergeFrom(workQuestionService.deleteWorkQuestion(id), true);
        }
        // delete associated submitted course work
        List<Long> submittedCourseWorkIdList = submittedCourseWorkRepository.findAllSubmittedCourseWorkIdByBelongCourseWork(courseWorkId);
        for (Long id : submittedCourseWorkIdList) {
            serviceResult = serviceResult.mergeFrom(submittedCourseWorkService.deleteSubmittedCourseWork(id), true);
        }
        return serviceResult;
    }
}
