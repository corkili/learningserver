package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.common.ServiceResult;

public interface SubmittedExamService extends Service<SubmittedExam, com.corkili.learningserver.po.SubmittedExam> {

    ServiceResult deleteSubmittedExam(Long submittedExamId);

    ServiceResult deleteSubmittedExamByBelongExamId(Long belongExamId);
}
