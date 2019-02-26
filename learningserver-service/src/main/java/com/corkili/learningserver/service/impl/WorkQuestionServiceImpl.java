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

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseWorkRepository;
import com.corkili.learningserver.repo.QuestionRepository;
import com.corkili.learningserver.repo.WorkQuestionRepository;
import com.corkili.learningserver.service.WorkQuestionService;

@Slf4j
@Service
public class WorkQuestionServiceImpl extends ServiceImpl<WorkQuestion, com.corkili.learningserver.po.WorkQuestion> implements WorkQuestionService {

    private static final String CACHE_NAME = "memoryCache";

    @Autowired
    private WorkQuestionRepository workQuestionRepository;

    @Autowired
    private CourseWorkRepository courseWorkRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.WorkQuestion, Long> repository() {
        return workQuestionRepository;
    }

    @Override
    protected String entityName() {
        return "workQuestion";
    }

    @Override
    protected WorkQuestion newBusinessObject() {
        return new WorkQuestion();
    }

    @Override
    protected com.corkili.learningserver.po.WorkQuestion newPersistObject() {
        return new com.corkili.learningserver.po.WorkQuestion();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteWorkQuestion(Long workQuestionId) {
        if (!delete(workQuestionId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete work question success");
        }
        return ServiceResult.successResultWithMesage("delete work question success");
    }

    @Override
    public ServiceResult deleteWorkQuestionByBelongCourseWorkId(Long belongCourseWorkId) {
        List<Long> workQuestionIdList = workQuestionRepository.findAllWorkQuestionIdByBelongCourseWorkId(belongCourseWorkId);
        workQuestionRepository.deleteAllByBelongCourseWorkId(belongCourseWorkId);
        for (Long id : workQuestionIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete work question success");
    }

    @Override
    public Optional<WorkQuestion> retrieveWorkQuestionByBelongCourseWorkIdAndIndex(Long belongCourseWorkId, int index) {
        return Optional.ofNullable(retrieveWorkQuestionByBelongCourseIdAndIndex(belongCourseWorkId, index, entityName()));
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#entityName + #result.id", unless = "#result == null or #result.id == null")
    public WorkQuestion retrieveWorkQuestionByBelongCourseIdAndIndex(Long belongCourseWorkId, int index, String entityName) {
        if (belongCourseWorkId == null) {
            log.error("belong course id is null");
            return null;
        }
        Optional<com.corkili.learningserver.po.WorkQuestion> workQuestionPOOptional = workQuestionRepository
                .findWorkQuestionByBelongCourseWorkIdAndIndex(belongCourseWorkId, index);
        if (!workQuestionPOOptional.isPresent()) {
            log.error("{} with belongCourseWork [{}] and index [{}] not exists in db", entityName, belongCourseWorkId, index);
            return null;
        }
        return po2bo(workQuestionPOOptional.get()).orElse(null);
    }

    @Override
    public ServiceResult createOrUpdateWorkQuestionsForCourseWork(Collection<WorkQuestion> workQuestions, Long courseWorkId) {
        if (workQuestions == null || workQuestions.isEmpty()) {
            return recordWarnAndCreateSuccessResultWithMessage("create or update work question warn: no work question")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<>()), true);
        }
        if (courseWorkId == null || courseWorkRepository.existsById(courseWorkId)) {
            return recordErrorAndCreateFailResultWithMessage("create or update work question error: course work id is null");
        }
        List<WorkQuestion> workQuestionList = new ArrayList<>(workQuestions);
        workQuestionList.forEach(workQuestion -> workQuestion.setBelongCourseWorkId(courseWorkId));
        workQuestionList.sort(Comparator.comparingInt(WorkQuestion::getIndex));
        for (int i = 0; i < workQuestionList.size(); i++) {
            workQuestionList.get(i).setIndex(i + 1);
        }
        Set<Long> questionIdSet = new HashSet<>();
        workQuestionList.forEach(workQuestion -> questionIdSet.add(workQuestion.getQuestionId()));
        if (questionIdSet.contains(null)) {
            return recordErrorAndCreateFailResultWithMessage("create or update work question error: has null question id");
        }
        if (questionRepository.countByIdIn(questionIdSet) != (long) questionIdSet.size()) {
            return recordErrorAndCreateFailResultWithMessage("create or update work question error: some question not exists");
        }
        List<WorkQuestion> successList = new ArrayList<>();
        int indexDiff = 0;
        for (WorkQuestion workQuestion : workQuestionList) {
            workQuestion.setIndex(workQuestion.getIndex() - indexDiff);
            Optional<WorkQuestion> optional = workQuestion.getId() != null ? retrieve(workQuestion.getId()) :
                    retrieveWorkQuestionByBelongCourseWorkIdAndIndex(courseWorkId, workQuestion.getIndex());
            Optional<WorkQuestion> workQuestionOptional;
            if (optional.isPresent()) {
                workQuestion.setId(optional.get().getId());
                workQuestion.setCreateTime(optional.get().getCreateTime());
                workQuestion.setUpdateTime(optional.get().getUpdateTime());
                workQuestionOptional = update(workQuestion);
            } else {
                workQuestionOptional = create(workQuestion);
            }
            if (workQuestionOptional.isPresent()) {
                successList.add(workQuestionOptional.get());
            } else {
                indexDiff++;
            }
        }
        // delete
        if (!successList.isEmpty()) {
            List<Long> deleteId = workQuestionRepository.findAllWorkQuestionIdByBelongCourseWorkIdAndIndexGreaterThan(
                    courseWorkId, successList.get(successList.size() - 1).getIndex());
            workQuestionRepository.deleteAllByBelongCourseWorkIdAndIndexGreaterThan(
                    courseWorkId, successList.get(successList.size() - 1).getIndex());
            for (Long id : deleteId) {
                evictFromCache(entityName() + id);
            }
            if (indexDiff > 0) {
                return recordWarnAndCreateSuccessResultWithMessage("create or update work question warn: some " +
                        "work question create or update failed").merge(ServiceResult.successResultWithExtra(
                                List.class, successList), true);
            } else {
                return ServiceResult.successResult("create or update work question success", List.class, successList);
            }
        } else {
            return recordErrorAndCreateFailResultWithMessage("create or update work question error: " +
                    "no work question create or update successfully");
        }
    }

    @Override
    public ServiceResult findAllWorkQuestionByBelongCourseWorkId(Long belongCourseWorkId) {
        if (belongCourseWorkId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("belongCourseId is null").merge(
                    ServiceResult.successResultWithExtra(List.class, new LinkedList<WorkQuestion>()), true);
        }
        String msg = "find work questions success";
        List<com.corkili.learningserver.po.WorkQuestion> workQuestionPOList = workQuestionRepository
                .findAllByBelongCourseWorkId(belongCourseWorkId);
        List<WorkQuestion> workQuestionList = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.WorkQuestion workQuestionPO : workQuestionPOList) {
            Optional<WorkQuestion> workQuestionOptional = po2bo(workQuestionPO);
            i++;
            if (!workQuestionOptional.isPresent()) {
                errId.append(workQuestionPO.getId());
                if (i != workQuestionPOList.size()) {
                    errId.append(",");
                }
            } else {
                WorkQuestion workQuestion = workQuestionOptional.get();
                workQuestionList.add(workQuestion);
                // cache
                putToCache(entityName() + workQuestion.getId(), workQuestion);
            }
        }
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find work question warn: transfer work question po [{}] to question bo failed", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, workQuestionList);
    }
}
