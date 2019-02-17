package com.corkili.learningserver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.ExamQuestionRepository;
import com.corkili.learningserver.repo.ExamRepository;
import com.corkili.learningserver.repo.SubmittedExamRepository;
import com.corkili.learningserver.service.ExamQuestionService;
import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.service.SubmittedExamService;

@Slf4j
@Service
public class ExamServiceImpl extends ServiceImpl<Exam, com.corkili.learningserver.po.Exam> implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private SubmittedExamRepository submittedExamRepository;

    @Autowired
    private SubmittedExamService submittedExamService;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Exam, Long> repository() {
        return examRepository;
    }

    @Override
    protected String entityName() {
        return "exam";
    }

    @Override
    protected Exam newBusinessObject() {
        return new Exam();
    }

    @Override
    protected com.corkili.learningserver.po.Exam newPersistObject() {
        return new com.corkili.learningserver.po.Exam();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteExam(Long examId) {
        ServiceResult serviceResult;
        if (!delete(examId)) {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete exam success");
        } else {
            serviceResult = ServiceResult.successResultWithMesage("delete exam success");
        }
        // delete associated exam question
        List<Long> examQuestionIdList = examQuestionRepository.findAllExamQuestionIdByBelongExamId(examId);
        for (Long id : examQuestionIdList) {
            serviceResult = serviceResult.mergeFrom(examQuestionService.deleteExamQuestion(id), true);
        }
        // delete associated submitted exam
        List<Long> submittedExamIdList = submittedExamRepository.findAllSubmittedExamIdByBelongExam(examId);
        for (Long id : submittedExamIdList) {
            serviceResult = serviceResult.mergeFrom(submittedExamService.deleteSubmittedExam(id), true);
        }
        return serviceResult;
    }
}
