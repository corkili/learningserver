package com.corkili.learningserver.service;

import java.util.Collection;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ServiceResult;

public interface ExamService extends Service<Exam, com.corkili.learningserver.po.Exam> {

    ServiceResult deleteExam(Long examId);

    ServiceResult deleteExamByBelongCourseId(Long belongCourseId);

    ServiceResult createExam(Exam exam, Collection<ExamQuestion> examQuestions);

    ServiceResult updateExam(Exam exam, boolean updateStartTime, boolean updateEndTime, Collection<ExamQuestion> examQuestions);

    ServiceResult findAllExam(Long belongCourseId);

}
