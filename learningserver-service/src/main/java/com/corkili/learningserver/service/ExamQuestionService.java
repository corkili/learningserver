package com.corkili.learningserver.service;

import java.util.Collection;
import java.util.Optional;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ServiceResult;

public interface ExamQuestionService extends Service<ExamQuestion, com.corkili.learningserver.po.ExamQuestion> {

    ServiceResult deleteExamQuestion(Long examQuestionId);

    ServiceResult deleteExamQuestionByBelongExamId(Long belongExamId);

    Optional<ExamQuestion> retrieveExamQuestionByBelongExamIdAndIndex(Long belongExamId, int index);

    ServiceResult createOrUpdateExamQuestionForExam(Collection<ExamQuestion> examQuestions, Long examId);

}
