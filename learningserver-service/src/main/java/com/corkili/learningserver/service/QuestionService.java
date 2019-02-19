package com.corkili.learningserver.service;

import java.util.List;
import java.util.Map;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.common.ServiceResult;

public interface QuestionService extends Service<Question, com.corkili.learningserver.po.Question> {

    ServiceResult importQuestion(Question question, Map<String, byte[]> questionImages, Map<String, byte[]> essayImages);

    ServiceResult findAllQuestion(Long authorId, boolean all, List<String> keywordList, List<QuestionType> questionTypeList);

    ServiceResult updateQuestion(Question question, Map<String, byte[]> questionImages, Map<String, byte[]> essayImages);

    ServiceResult deleteQuestion(Long questionId);

}
