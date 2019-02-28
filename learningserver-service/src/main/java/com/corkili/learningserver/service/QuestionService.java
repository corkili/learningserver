package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.common.ServiceResult;

import java.util.List;
import java.util.Map;

public interface QuestionService extends Service<Question, com.corkili.learningserver.po.Question> {

    ServiceResult importQuestion(Question question, Map<String, byte[]> questionImages);

    ServiceResult findAllQuestion(Long authorId, boolean all, List<String> keywordList, List<QuestionType> questionTypeList);

    ServiceResult updateQuestion(Question question, Map<String, byte[]> questionImages);

    ServiceResult deleteQuestion(Long questionId);

}
