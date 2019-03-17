package com.corkili.learningserver.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.bo.User.Type;
import com.corkili.learningserver.common.QuestionUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.ExamQuestionRepository;
import com.corkili.learningserver.repo.ExamRepository;
import com.corkili.learningserver.repo.SubmittedExamRepository;
import com.corkili.learningserver.service.ExamQuestionService;
import com.corkili.learningserver.service.QuestionService;
import com.corkili.learningserver.service.SubmittedExamService;
import com.corkili.learningserver.service.UserService;

@Slf4j
@Service
@Transactional
public class SubmittedExamServiceImpl extends ServiceImpl<SubmittedExam, com.corkili.learningserver.po.SubmittedExam> implements SubmittedExamService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private ExamQuestionService examQuestionService;
    
    @Autowired
    private SubmittedExamRepository submittedExamRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;
    
    @Autowired
    private ExamRepository examRepository;

    @Override
    public Optional<SubmittedExam> po2bo(com.corkili.learningserver.po.SubmittedExam submittedExamPO) {
        Optional<SubmittedExam> superOptional = super.po2bo(submittedExamPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        SubmittedExam submittedExam = superOptional.get();
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        List<com.corkili.learningserver.po.ExamQuestion> examQuestionList = examQuestionRepository.findExamQuestionsByBelongExam(submittedExamPO.getBelongExam());
        for (com.corkili.learningserver.po.ExamQuestion examQuestion : examQuestionList) {
            questionTypeMap.put(examQuestion.getIndex(), QuestionType.valueOf(examQuestion.getQuestion().getQuestionType().name()));
        }
        submittedExam.setSubmittedAnswers(submittedExamPO.getSubmittedAnswers(), questionTypeMap);
        return Optional.of(submittedExam);
    }

    @Override
    public Optional<com.corkili.learningserver.po.SubmittedExam> bo2po(SubmittedExam submittedExam) {
        Optional<com.corkili.learningserver.po.SubmittedExam> superOptional = super.bo2po(submittedExam);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.SubmittedExam submittedExamPO = superOptional.get();
        submittedExamPO.setSubmittedAnswers(submittedExam.getSubmittedAnswersStr());
        return Optional.of(submittedExamPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.SubmittedExam, Long> repository() {
        return submittedExamRepository;
    }

    @Override
    protected String entityName() {
        return "submittedExam";
    }

    @Override
    protected SubmittedExam newBusinessObject() {
        return new SubmittedExam();
    }

    @Override
    protected com.corkili.learningserver.po.SubmittedExam newPersistObject() {
        return new com.corkili.learningserver.po.SubmittedExam();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteSubmittedExam(Long submittedExamId) {
        if (!delete(submittedExamId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete submitted exam success");
        }
        return ServiceResult.successResultWithMesage("delete submitted exam success");
    }

    @Override
    public ServiceResult deleteSubmittedExamByBelongExamId(Long belongExamId) {
        List<Long> submittedExamIdList = submittedExamRepository.findAllSubmittedExamIdByBelongExamId(belongExamId);
        submittedExamRepository.deleteAllByBelongExamId(belongExamId);
        for (Long id : submittedExamIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResult("delete submitted exam success");
    }

    @Override
    public ServiceResult createSubmittedExam(SubmittedExam submittedExam) {
        if (submittedExam.getBelongExamId() == null ||
                !examRepository.existsById(submittedExam.getBelongExamId())) {
            return recordErrorAndCreateFailResultWithMessage("create submitted exam error: belong exam not exists");
        }
        Optional<User> userOptional = userService.retrieve(submittedExam.getSubmitterId());
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create submitted exam error: submitter not exists");
        }
        User submitter = userOptional.get();
        if (submittedExamRepository.existsByBelongExamIdAndSubmitterId(
                submittedExam.getBelongExamId(), submittedExam.getSubmitterId())) {
            return recordErrorAndCreateFailResultWithMessage("create submitted exam error: this submitter " +
                    "already submit the exam");
        }
        if (submittedExam.isFinished()) {
            checkExam(submittedExam);
        }
        Optional<SubmittedExam> submittedExamOptional = create(submittedExam);
        if (!submittedExamOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create submitted exam error: store in db failed");
        }
        submittedExam = submittedExamOptional.get();
        return ServiceResult.successResult("create submitted exam success",
                SubmittedExam.class, submittedExam, User.class, submitter);
    }

    @Override
    public ServiceResult updateSubmittedExam(SubmittedExam submittedExam, boolean oldFinished, Long operatorId) {
        Optional<User> userOptional = userService.retrieve(operatorId);
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage(
                    "update submitted exam error: get operator user [{}] failed", operatorId);
        }
        User operator = userOptional.get();
        if (operator.getUserType() == Type.Teacher && !oldFinished) {
            return recordErrorAndCreateFailResultWithMessage("update submitted exam error: " +
                    "teacher can't update when finished already is false");
        }
        if (operator.getUserType() == Type.Student && oldFinished) {
            return recordErrorAndCreateFailResultWithMessage("update submitted exam error: " +
                    "student can't update when finished already is true");
        }
        if (submittedExam.getBelongExamId() == null ||
                !examRepository.existsById(submittedExam.getBelongExamId())) {
            return recordErrorAndCreateFailResultWithMessage("update submitted exam error: belong exam not exists");
        }
        userOptional = userService.retrieve(submittedExam.getSubmitterId());
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update submitted exam error: submitter not exists");
        }
        User submitter = userOptional.get();
        if (submittedExam.isFinished()) {
            checkExam(submittedExam);
        }
        Optional<SubmittedExam> submittedExamOptional = update(submittedExam);
        if (!submittedExamOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update submitted exam error: store in db failed");
        }
        submittedExam = submittedExamOptional.get();
        return ServiceResult.successResult("update submitted exam success",
                SubmittedExam.class, submittedExam, User.class, submitter);
    }

    @Override
    public ServiceResult findAllSubmittedExam(Long belongExamId) {
        List<SubmittedExam> submittedExamList = new LinkedList<>();
        if (belongExamId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all submitted exam warn: " +
                    "belongExamId is null").merge(ServiceResult.successResultWithExtra(
                    List.class, submittedExamList), true);
        }
        List<com.corkili.learningserver.po.SubmittedExam> allSubmittedExamPO =
                submittedExamRepository.findAllByBelongExamId(belongExamId);
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.SubmittedExam submittedExamPO : allSubmittedExamPO) {
            Optional<SubmittedExam> submittedExamOptional = po2bo(submittedExamPO);
            i++;
            if (!submittedExamOptional.isPresent()) {
                errId.append(submittedExamPO.getId());
                if (i != allSubmittedExamPO.size()) {
                    errId.append(",");
                }
            } else {
                SubmittedExam submittedExam = submittedExamOptional.get();
                submittedExamList.add(submittedExam);
                // cache
                putToCache(entityName() + submittedExam.getId(), submittedExam);
            }
        }
        String msg = "find all exam success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all exam warn: transfer course po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, submittedExamList);
    }

    @Override
    public Optional<SubmittedExam> retrieveByBelongExamIdAndSubmitterId(Long belongExamId, Long submitterId) {
        Optional<com.corkili.learningserver.po.SubmittedExam> optional = submittedExamRepository
                .findByBelongExamIdAndSubmitterId(belongExamId, submitterId);
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        Optional<SubmittedExam> submittedExamOptional = po2bo(optional.get());
        if (!submittedExamOptional.isPresent()) {
            return Optional.empty();
        }
        // cache
        putToCache(entityName() + submittedExamOptional.get().getId(), submittedExamOptional.get());
        return submittedExamOptional;
    }

    private void checkExam(SubmittedExam submittedExam) {
        Map<Integer, ExamQuestion> examQuestionMap = new HashMap<>();
        Map<Long, Question> questionMap = new HashMap<>();
        List<ExamQuestion> examQuestionList = (List<ExamQuestion>) examQuestionService.findAllExamQuestionByBelongExamId(
                submittedExam.getBelongExamId()).extra(List.class);
        if (examQuestionList.size() != submittedExam.getSubmittedAnswers().size()) {
            logger().warn("check exam warn: some work question not found");
        }
        for (ExamQuestion examQuestion : examQuestionList) {
            examQuestionMap.put(examQuestion.getIndex(), examQuestion);
            Optional<Question> questionOptional = questionService.retrieve(examQuestion.getQuestionId());
            if (!questionOptional.isPresent()) {
                logger().warn("check exam warn: question [{}] not found", examQuestion.getQuestionId());
            } else {
                Question question = questionOptional.get();
                questionMap.put(question.getId(), question);
            }
        }
        QuestionUtils.checkExam(questionMap, examQuestionMap, submittedExam);
    }
}
