package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ServiceResult;

public interface WorkQuestionService extends Service<WorkQuestion, com.corkili.learningserver.po.WorkQuestion> {

    ServiceResult deleteWorkQuestion(Long workQuestionId);

}
