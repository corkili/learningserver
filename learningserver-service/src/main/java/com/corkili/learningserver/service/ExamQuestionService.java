package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ServiceResult;

public interface ExamQuestionService extends Service<ExamQuestion, com.corkili.learningserver.po.ExamQuestion> {

    ServiceResult deleteExamQuestion(Long examQuestionId);

    ServiceResult deleteExamQuestionByBelongExamId(Long belongExamId);

}
