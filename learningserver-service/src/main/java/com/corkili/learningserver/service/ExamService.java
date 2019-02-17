package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.common.ServiceResult;

public interface ExamService extends Service<Exam, com.corkili.learningserver.po.Exam> {

    ServiceResult deleteExam(Long examId);

    ServiceResult deleteExamByBelongCourseId(Long belongCourseId);

}
