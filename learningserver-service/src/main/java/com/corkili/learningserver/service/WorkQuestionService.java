package com.corkili.learningserver.service;

import java.util.Collection;
import java.util.Optional;

import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ServiceResult;

public interface WorkQuestionService extends Service<WorkQuestion, com.corkili.learningserver.po.WorkQuestion> {

    ServiceResult deleteWorkQuestion(Long workQuestionId);

    ServiceResult deleteWorkQuestionByBelongCourseWorkId(Long belongCourseWorkId);

    Optional<WorkQuestion> retrieveWorkQuestionByBelongCourseIdAndIndex(Long belongCourseId, int index);

    ServiceResult createOrUpdateWorkQuestionsForCourseWork(Collection<WorkQuestion> workQuestions, Long courseWorkId);

}
