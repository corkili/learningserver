package com.corkili.learningserver.service;

import java.util.Map;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.common.ServiceResult;

public interface QuestionService extends Service<Question, com.corkili.learningserver.po.Question> {

    ServiceResult importQuestion(Question question, Map<String, byte[]> questionImages, Map<String, byte[]> essayImages);

}
