package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Optional;

public interface SubmittedExamService extends Service<SubmittedExam, com.corkili.learningserver.po.SubmittedExam> {

    ServiceResult deleteSubmittedExam(Long submittedExamId);

    ServiceResult deleteSubmittedExamByBelongExamId(Long belongExamId);

    ServiceResult createSubmittedExam(SubmittedExam submittedExam);

    ServiceResult updateSubmittedExam(SubmittedExam submittedExam, boolean oldFinished, Long operatorId);

    ServiceResult findAllSubmittedExam(Long belongExamId);

    Optional<SubmittedExam> retrieveByBelongExamIdAndSubmitterId(Long belongExamId, Long submitterId);

}
