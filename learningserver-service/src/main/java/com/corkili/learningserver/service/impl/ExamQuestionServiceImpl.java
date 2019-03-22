package com.corkili.learningserver.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.ExamQuestionRepository;
import com.corkili.learningserver.repo.ExamRepository;
import com.corkili.learningserver.repo.QuestionRepository;
import com.corkili.learningserver.service.ExamQuestionService;

@Slf4j
@Service
@Transactional
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestion, com.corkili.learningserver.po.ExamQuestion> implements ExamQuestionService {

    private static final String CACHE_NAME = "memoryCache";

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Optional<ExamQuestion> po2bo(com.corkili.learningserver.po.ExamQuestion examQuestionPO) {
        Optional<ExamQuestion> superOptional = super.po2bo(examQuestionPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        ExamQuestion examQuestion = superOptional.get();
        examQuestion.setScore(examQuestionPO.getScore());
        return Optional.of(examQuestion);
    }

    @Override
    public Optional<com.corkili.learningserver.po.ExamQuestion> bo2po(ExamQuestion examQuestion) {
        Optional<com.corkili.learningserver.po.ExamQuestion> superOptional = super.bo2po(examQuestion);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.ExamQuestion examQuestionPO = superOptional.get();
        examQuestionPO.setScore(examQuestion.getScoreStr());
        return Optional.of(examQuestionPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.ExamQuestion, Long> repository() {
        return examQuestionRepository;
    }

    @Override
    protected String entityName() {
        return "examQuestion";
    }

    @Override
    protected ExamQuestion newBusinessObject() {
        return new ExamQuestion();
    }

    @Override
    protected com.corkili.learningserver.po.ExamQuestion newPersistObject() {
        return new com.corkili.learningserver.po.ExamQuestion();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteExamQuestion(Long examQuestionId) {
        if (!delete(examQuestionId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete exam question success");
        }
        return ServiceResult.successResultWithMesage("delete exam question success");
    }

    @Override
    public ServiceResult deleteExamQuestionByBelongExamId(Long belongExamId) {
        List<Long> examQuestionIdList = examQuestionRepository.findAllExamQuestionIdByBelongExamId(belongExamId);
        examQuestionRepository.deleteAllByBelongExamId(belongExamId);
        for (Long id : examQuestionIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete exam question success");
    }

    @Override
    public Optional<ExamQuestion> retrieveExamQuestionByBelongExamIdAndIndex(Long belongExamId, int index) {
        return Optional.ofNullable(retrieveExamQuestionByBelongExamIdAndIndex(belongExamId, index, entityName()));
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#entityName + #result.id", unless = "#result == null or #result.id == null")
    public ExamQuestion retrieveExamQuestionByBelongExamIdAndIndex(Long belongExamId, int index, String entityName) {
        if (belongExamId == null) {
            log.error("belong course id is null");
            return null;
        }
        Optional<com.corkili.learningserver.po.ExamQuestion> examQuestionPOOptional = examQuestionRepository
                .findExamQuestionByBelongExamIdAndIndex(belongExamId, index);
        if (!examQuestionPOOptional.isPresent()) {
            log.error("{} with belongExam [{}] and index [{}] not exists in db", entityName, belongExamId, index);
            return null;
        }
        return po2bo(examQuestionPOOptional.get()).orElse(null);
    }

    @Override
    public ServiceResult createOrUpdateExamQuestionForExam(Collection<ExamQuestion> examQuestions, Long examId) {
        if (examQuestions == null || examQuestions.isEmpty()) {
            return recordWarnAndCreateSuccessResultWithMessage("create or update exam question warn: no exam question")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<>()), true);
        }
        if (examId == null || !examRepository.existsById(examId)) {
            return recordErrorAndCreateFailResultWithMessage("create or update exam question error: course work id is null");
        }
        List<ExamQuestion> examQuestionList = new ArrayList<>(examQuestions);
        examQuestionList.forEach(examQuestion -> examQuestion.setBelongExamId(examId));
        examQuestionList.sort(Comparator.comparingInt(ExamQuestion::getIndex));
        for (int i = 0; i < examQuestionList.size(); i++) {
            examQuestionList.get(i).setIndex(i + 1);
        }
        Set<Long> questionIdSet = new HashSet<>();
        examQuestionList.forEach(examQuestion -> questionIdSet.add(examQuestion.getQuestionId()));
        if (questionIdSet.contains(null)) {
            return recordErrorAndCreateFailResultWithMessage("create or update exam question error: has null question id");
        }
        if (questionRepository.countByIdIn(questionIdSet) != (long) questionIdSet.size()) {
            return recordErrorAndCreateFailResultWithMessage("create or update exam question error: some question not exists");
        }
        List<ExamQuestion> successList = new ArrayList<>();
        int indexDiff = 0;
        for (ExamQuestion examQuestion : examQuestionList) {
            examQuestion.setIndex(examQuestion.getIndex() - indexDiff);
            Optional<ExamQuestion> optional = examQuestion.getId() != null ? retrieve(examQuestion.getId()) :
                    retrieveExamQuestionByBelongExamIdAndIndex(examId, examQuestion.getIndex());
            Optional<ExamQuestion> examQuestionOptional;
            if (optional.isPresent()) {
                examQuestion.setId(optional.get().getId());
                examQuestion.setCreateTime(optional.get().getCreateTime());
                examQuestion.setUpdateTime(optional.get().getUpdateTime());
                examQuestionOptional = update(examQuestion);
            } else {
                examQuestionOptional = create(examQuestion);
            }
            if (examQuestionOptional.isPresent()) {
                successList.add(examQuestionOptional.get());
            } else {
                indexDiff++;
            }
        }
        // delete
        if (!successList.isEmpty()) {
            List<Long> deleteId = examQuestionRepository.findAllExamQuestionIdByBelongExamIdAndIndexGreaterThan(
                    examId, successList.get(successList.size() - 1).getIndex());
            examQuestionRepository.deleteAllByBelongExamIdAndIndexGreaterThan(
                    examId, successList.get(successList.size() - 1).getIndex());
            for (Long id : deleteId) {
                evictFromCache(entityName() + id);
            }
            if (indexDiff > 0) {
                return recordWarnAndCreateSuccessResultWithMessage("create or update exam question warn: some " +
                        "exam question create or update failed").merge(ServiceResult.successResultWithExtra(
                        List.class, successList), true);
            } else {
                return ServiceResult.successResult("create or update exam question success", List.class, successList);
            }
        } else {
            return recordErrorAndCreateFailResultWithMessage("create or update exam question error: " +
                    "no exam question create or update successfully");
        }
    }

    @Override
    public ServiceResult findAllExamQuestionByBelongExamId(Long belongExamId) {
        if (belongExamId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("belongExamId is null").merge(
                    ServiceResult.successResultWithExtra(List.class, new LinkedList<ExamQuestion>()), true);
        }
        String msg = "find exam questions success";
        List<com.corkili.learningserver.po.ExamQuestion> examQuestionPOList = examQuestionRepository
                .findAllByBelongExamId(belongExamId);
        List<ExamQuestion> examQuestionList = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.ExamQuestion examQuestionPO : examQuestionPOList) {
            Optional<ExamQuestion> examQuestionOptional = po2bo(examQuestionPO);
            i++;
            if (!examQuestionOptional.isPresent()) {
                errId.append(examQuestionPO.getId());
                if (i != examQuestionPOList.size()) {
                    errId.append(",");
                }
            } else {
                ExamQuestion examQuestion = examQuestionOptional.get();
                examQuestionList.add(examQuestion);
                // cache
                putToCache(entityName() + examQuestion.getId(), examQuestion);
            }
        }
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find exam question warn: transfer work question po [{}] to question bo failed", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, examQuestionList);
    }
}
