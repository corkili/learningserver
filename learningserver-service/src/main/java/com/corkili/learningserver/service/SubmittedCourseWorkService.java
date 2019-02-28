package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Optional;

public interface SubmittedCourseWorkService extends Service<SubmittedCourseWork, com.corkili.learningserver.po.SubmittedCourseWork> {

    ServiceResult deleteSubmittedCourseWork(Long submittedCourseWorkId);

    ServiceResult deleteSubmittedCourseWorkByBelongCourseWorkId(Long belongCourseWorkId);

    ServiceResult createSubmittedCourseWork(SubmittedCourseWork submittedCourseWork);

    ServiceResult updateSubmittedCourseWork(SubmittedCourseWork submittedCourseWork, boolean oldFinished, Long operatorId);

    ServiceResult findAllSubmittedCourseWork(Long belongCourseWorkId);

    Optional<SubmittedCourseWork> retrieveByBelongCourseWorkIdAndSubmitterId(Long belongCourseWorkId, Long submitterId);

}
