package com.corkili.learningserver.service.impl;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.bo.User.Type;
import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.QuestionUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseWorkRepository;
import com.corkili.learningserver.repo.SubmittedCourseWorkRepository;
import com.corkili.learningserver.repo.WorkQuestionRepository;
import com.corkili.learningserver.service.QuestionService;
import com.corkili.learningserver.service.SubmittedCourseWorkService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.service.WorkQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SubmittedCourseWorkServiceImpl extends ServiceImpl<SubmittedCourseWork, com.corkili.learningserver.po.SubmittedCourseWork> implements SubmittedCourseWorkService {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WorkQuestionService workQuestionService;

    @Autowired
    private SubmittedCourseWorkRepository submittedCourseWorkRepository;

    @Autowired
    private WorkQuestionRepository workQuestionRepository;

    @Autowired
    private CourseWorkRepository courseWorkRepository;

    @Override
    public Optional<SubmittedCourseWork> po2bo(com.corkili.learningserver.po.SubmittedCourseWork submittedCourseWorkPO) {
        Optional<SubmittedCourseWork> superOptional = super.po2bo(submittedCourseWorkPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        SubmittedCourseWork submittedCourseWork = superOptional.get();
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        List<com.corkili.learningserver.po.WorkQuestion> workQuestionList =
                workQuestionRepository.findWorkQuestionsByBelongCourseWork(submittedCourseWorkPO.getBelongCourseWork());
        for (com.corkili.learningserver.po.WorkQuestion workQuestion : workQuestionList) {
            questionTypeMap.put(workQuestion.getIndex(), QuestionType.valueOf(workQuestion.getQuestion().getQuestionType().name()));
        }
        submittedCourseWork.setSubmittedAnswers(submittedCourseWorkPO.getSubmittedAnswers(), questionTypeMap);
        return Optional.of(submittedCourseWork);
    }

    @Override
    public Optional<com.corkili.learningserver.po.SubmittedCourseWork> bo2po(SubmittedCourseWork submittedCourseWork) {
        Optional<com.corkili.learningserver.po.SubmittedCourseWork> superOptional = super.bo2po(submittedCourseWork);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.SubmittedCourseWork submittedCourseWorkPO = superOptional.get();
        submittedCourseWorkPO.setSubmittedAnswers(submittedCourseWork.getSubmittedAnswersStr());
        return Optional.of(submittedCourseWorkPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.SubmittedCourseWork, Long> repository() {
        return submittedCourseWorkRepository;
    }

    @Override
    protected String entityName() {
        return "submittedCourseWork";
    }

    @Override
    protected SubmittedCourseWork newBusinessObject() {
        return new SubmittedCourseWork();
    }

    @Override
    protected com.corkili.learningserver.po.SubmittedCourseWork newPersistObject() {
        return new com.corkili.learningserver.po.SubmittedCourseWork();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteSubmittedCourseWork(Long submittedCourseWorkId) {
        if (!delete(submittedCourseWorkId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete submitted course work success");
        }
        return ServiceResult.successResultWithMesage("delete submitted course work success");
    }

    @Override
    public ServiceResult deleteSubmittedCourseWorkByBelongCourseWorkId(Long belongCourseWorkId) {
        List<Long> submittedCourseWorkIdList = submittedCourseWorkRepository.findAllSubmittedCourseWorkIdByBelongCourseWorkId(belongCourseWorkId);
        submittedCourseWorkRepository.deleteAllByBelongCourseWorkId(belongCourseWorkId);
        for (Long id : submittedCourseWorkIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete submitted course work success");
    }

    @Override
    public ServiceResult createSubmittedCourseWork(SubmittedCourseWork submittedCourseWork) {
        if (submittedCourseWork.getBelongCourseWorkId() == null ||
                !courseWorkRepository.existsById(submittedCourseWork.getBelongCourseWorkId())) {
            return recordErrorAndCreateFailResultWithMessage("create submitted course work error: belong course work not exists");
        }
        Optional<User> userOptional = userService.retrieve(submittedCourseWork.getSubmitterId());
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create submitted course work error: submitter not exists");
        }
        User submitter = userOptional.get();
        if (submittedCourseWorkRepository.existsByBelongCourseWorkIdAndSubmitterId(
                submittedCourseWork.getBelongCourseWorkId(), submittedCourseWork.getSubmitterId())) {
            return recordErrorAndCreateFailResultWithMessage("create submitted course work error: this submitter " +
                    "already submit the course work");
        }
        if (submittedCourseWork.isFinished()) {
            checkCourseWork(submittedCourseWork);
        }
        Optional<SubmittedCourseWork> submittedCourseWorkOptional = create(submittedCourseWork);
        if (!submittedCourseWorkOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create submitted course work error: store in db failed");
        }
        submittedCourseWork = submittedCourseWorkOptional.get();
        return ServiceResult.successResult("create submitted course work success",
                SubmittedCourseWork.class, submittedCourseWork, User.class, submitter);
    }

    @Override
    public ServiceResult updateSubmittedCourseWork(SubmittedCourseWork submittedCourseWork, boolean oldFinished, Long operatorId) {
        Optional<User> userOptional = userService.retrieve(operatorId);
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage(
                    "update submitted course work error: get operator user [{}] failed", operatorId);
        }
        User operator = userOptional.get();
        if (operator.getUserType() == Type.Teacher && !oldFinished) {
            return recordErrorAndCreateFailResultWithMessage("update submitted course work error: " +
                    "teacher can't update when finished already is false");
        }
        if (operator.getUserType() == Type.Student && oldFinished) {
            return recordErrorAndCreateFailResultWithMessage("update submitted course work error: " +
                    "student can't update when finished already is true");
        }
        if (submittedCourseWork.getBelongCourseWorkId() == null ||
                !courseWorkRepository.existsById(submittedCourseWork.getBelongCourseWorkId())) {
            return recordErrorAndCreateFailResultWithMessage("update submitted course work error: belong course work not exists");
        }
        userOptional = userService.retrieve(submittedCourseWork.getSubmitterId());
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update submitted course work error: submitter not exists");
        }
        User submitter = userOptional.get();
        if (submittedCourseWork.isFinished()) {
            checkCourseWork(submittedCourseWork);
        }
        Optional<SubmittedCourseWork> submittedCourseWorkOptional = update(submittedCourseWork);
        if (!submittedCourseWorkOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("update submitted course work error: store in db failed");
        }
        submittedCourseWork = submittedCourseWorkOptional.get();
        return ServiceResult.successResult("update submitted course work success",
                SubmittedCourseWork.class, submittedCourseWork, User.class, submitter);
    }

    @Override
    public ServiceResult findAllSubmittedCourseWork(Long belongCourseWorkId) {
        List<SubmittedCourseWork> submittedCourseWorkList = new LinkedList<>();
        if (belongCourseWorkId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all submitted course work warn: " +
                    "belongCourseWorkId is null").merge(ServiceResult.successResultWithExtra(
                            List.class, submittedCourseWorkList), true);
        }
        List<com.corkili.learningserver.po.SubmittedCourseWork> allSubmittedCourseWorkPO =
                submittedCourseWorkRepository.findAllByBelongCourseWorkId(belongCourseWorkId);
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.SubmittedCourseWork submittedCourseWorkPO : allSubmittedCourseWorkPO) {
            Optional<SubmittedCourseWork> submittedCourseWorkOptional = po2bo(submittedCourseWorkPO);
            i++;
            if (!submittedCourseWorkOptional.isPresent()) {
                errId.append(submittedCourseWorkPO.getId());
                if (i != allSubmittedCourseWorkPO.size()) {
                    errId.append(",");
                }
            } else {
                SubmittedCourseWork submittedCourseWork = submittedCourseWorkOptional.get();
                submittedCourseWorkList.add(submittedCourseWork);
                // cache
                putToCache(entityName() + submittedCourseWork.getId(), submittedCourseWork);
            }
        }
        String msg = "find all course work success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all course work warn: transfer course po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, submittedCourseWorkList);
    }

    @Override
    public Optional<SubmittedCourseWork> retrieveByBelongCourseWorkIdAndSubmitterId(Long belongCourseWorkId, Long submitterId) {
        Optional<com.corkili.learningserver.po.SubmittedCourseWork> optional = submittedCourseWorkRepository
                .findByBelongCourseWorkIdAndSubmitterId(belongCourseWorkId, submitterId);
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        Optional<SubmittedCourseWork> submittedCourseWorkOptional = po2bo(optional.get());
        if (!submittedCourseWorkOptional.isPresent()) {
            return Optional.empty();
        }
        // cache
        putToCache(entityName() + submittedCourseWorkOptional.get().getId(), submittedCourseWorkOptional.get());
        return submittedCourseWorkOptional;
    }

    private void checkCourseWork(SubmittedCourseWork submittedCourseWork) {
        Map<Integer, WorkQuestion> workQuestionMap = new HashMap<>();
        Map<Long, Question> questionMap = new HashMap<>();
        List<WorkQuestion> workQuestionList = (List<WorkQuestion>) workQuestionService.findAllWorkQuestionByBelongCourseWorkId(
                submittedCourseWork.getBelongCourseWorkId()).extra(List.class);
        if (workQuestionList.size() != submittedCourseWork.getSubmittedAnswers().size()) {
            logger().warn("check course work warn: some work question not found");
        }
        for (WorkQuestion workQuestion : workQuestionList) {
            workQuestionMap.put(workQuestion.getIndex(), workQuestion);
            Optional<Question> questionOptional = questionService.retrieve(workQuestion.getQuestionId());
            if (!questionOptional.isPresent()) {
                logger().warn("check course work warn: question [{}] not found", workQuestion.getQuestionId());
            } else {
                Question question = questionOptional.get();
                questionMap.put(question.getId(), question);
            }
        }
        QuestionUtils.checkCourseWork(questionMap, workQuestionMap, submittedCourseWork);
    }
}
