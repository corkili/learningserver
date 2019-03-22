package com.corkili.learningserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.repo.ExamRepository;
import com.corkili.learningserver.service.ExamQuestionService;
import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.service.SubmittedExamService;

@Slf4j
@Service
@Transactional
public class ExamServiceImpl extends ServiceImpl<Exam, com.corkili.learningserver.po.Exam> implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ExamQuestionService examQuestionService;

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
        // delete associated exam question
        examQuestionService.deleteExamQuestionByBelongExamId(examId);
        // delete associated submitted exam
        submittedExamService.deleteSubmittedExamByBelongExamId(examId);
        ServiceResult serviceResult;
        if (!delete(examId)) {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete exam success");
        } else {
            serviceResult = ServiceResult.successResultWithMesage("delete exam success");
        }
        return serviceResult;
    }

    @Override
    public ServiceResult deleteExamByBelongCourseId(Long belongCourseId) {
        List<Long> examIdList = examRepository.findAllExamIdByBelongCourseId(belongCourseId);
        for (Long id : examIdList) {
            // delete associated exam question
            examQuestionService.deleteExamQuestionByBelongExamId(id);
            // delete associated submitted exam
            submittedExamService.deleteSubmittedExamByBelongExamId(id);
            evictFromCache(entityName() + id);
        }
        examRepository.deleteAllByBelongCourseId(belongCourseId);
        return ServiceResult.successResultWithMesage("delete exam success");
    }

    @Override
    public ServiceResult createExam(Exam exam, Collection<ExamQuestion> examQuestions) {
        if (StringUtils.isBlank(exam.getExamName())) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: workName is empty");
        }
        if (exam.getExamName().length() > 100) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: length of workName > 100");
        }
        if (exam.getStartTime() == null) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: start time is null");
        }
//        if (exam.getStartTime().before(new Date())) {
//            return recordErrorAndCreateFailResultWithMessage("create exam error: start time is before now");
//        }
        if (exam.getEndTime() == null) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: end time is null");
        }
        if (exam.getEndTime().before(new Date())) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: end time is before now");
        }
        if (exam.getStartTime().after(exam.getEndTime())) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: start time is after end time");
        }
        if (exam.getDuration() < 1) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: duration < 1");
        }
        if (exam.getBelongCourseId() == null || !courseRepository.existsById(exam.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: belong course [{}] not exist",
                    exam.getBelongCourseId() == null ? "" : exam.getBelongCourseId());
        }
        Optional<Exam> examOptional = create(exam);
        if (!examOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create exam error: save Exam failed");
        }
        exam = examOptional.get();
        ServiceResult serviceResult = examQuestionService.createOrUpdateExamQuestionForExam(examQuestions, exam.getId());
        String msg = "create exam success";
        if (serviceResult.isFail()) {
            msg += ", but " + serviceResult.msg();
        } else {
            msg += ", and " + serviceResult.msg();
        }
        List<ExamQuestion> examQuestionList = (List<ExamQuestion>) serviceResult.extra(List.class);
        if (examQuestionList == null) {
            examQuestionList = new ArrayList<>();
        }
        return ServiceResult.successResult(msg, Exam.class, exam, List.class, examQuestionList);
    }

    @Override
    public ServiceResult updateExam(Exam exam, boolean updateStartTime, boolean updateEndTime, Collection<ExamQuestion> examQuestions) {
        if (StringUtils.isBlank(exam.getExamName())) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: workName is empty");
        }
        if (exam.getExamName().length() > 100) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: length of workName > 100");
        }
        if (updateStartTime && exam.getStartTime() == null) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: start time is null");
        }
//        if (exam.getStartTime().before(new Date())) {
//            return recordErrorAndCreateFailResultWithMessage("update exam error: start time is before now");
//        }
        if (updateEndTime && exam.getEndTime() == null) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: end time is null");
        }
        if (updateEndTime && exam.getEndTime().before(new Date())) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: end time is before now");
        }
        if ((updateStartTime || updateEndTime) && exam.getStartTime().after(exam.getEndTime())) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: start time is after end time");
        }
        if (exam.getDuration() < 1) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: duration < 1");
        }
        if (exam.getBelongCourseId() == null || !courseRepository.existsById(exam.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: belong course [{}] not exist",
                    exam.getBelongCourseId() == null ? "" : exam.getBelongCourseId());
        }
        Optional<Exam> examOptional = update(exam);
        if (!examOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update exam error: save Exam failed");
        }
        exam = examOptional.get();
        ServiceResult serviceResult = examQuestionService.createOrUpdateExamQuestionForExam(examQuestions, exam.getId());
        String msg = "update exam success";
        List<ExamQuestion> examQuestionList = null;
        if (examQuestions != null) {
            if (serviceResult.isFail()) {
                msg += ", but " + serviceResult.msg();
            } else {
                msg += ", and " + serviceResult.msg();
            }
            examQuestionList = (List<ExamQuestion>) serviceResult.extra(List.class);
        }
        if (examQuestionList == null) {
            examQuestionList = (List<ExamQuestion>) examQuestionService.findAllExamQuestionByBelongExamId(
                    exam.getId()).extra(List.class);
        }
        return ServiceResult.successResult(msg, Exam.class, exam, List.class, examQuestionList);
    }

    @Override
    public ServiceResult findAllExam(Long belongCourseId) {
        List<Exam> allExam = new LinkedList<>();
        if (belongCourseId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("belongCourseId is null")
                    .merge(ServiceResult.successResultWithExtra(List.class, allExam), true);
        }
        List<com.corkili.learningserver.po.Exam> allExamPO = examRepository
                .findAllByBelongCourseId(belongCourseId);
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.Exam examPO : allExamPO) {
            Optional<Exam> courseOptional = po2bo(examPO);
            i++;
            if (!courseOptional.isPresent()) {
                errId.append(examPO.getId());
                if (i != allExamPO.size()) {
                    errId.append(",");
                }
            } else {
                Exam exam = courseOptional.get();
                allExam.add(exam);
                // cache
                putToCache(entityName() + exam.getId(), exam);
            }
        }
        String msg = "find all exam success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all exam warn: transfer course po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allExam);
    }
}
