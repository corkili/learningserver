package com.corkili.learningserver.scorm.rte.api;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.Objective;
import com.corkili.learningserver.scorm.cam.model.Objectives;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.util.CPUtils;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.model.Data;
import com.corkili.learningserver.scorm.rte.model.Objectives.Instance;
import com.corkili.learningserver.scorm.rte.model.RuntimeData;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

@Slf4j
public class LearnerAttempt {

    private ID attemptID;

    private State state;

    private RuntimeData runtimeData;

    private Map<String, LearnerSession> learnerSessionMap;

    private LearnerSession currentLearnerSession;

    private Reason lastLearnerClosedReason;

    private int lastErrorCode;

    private String lastErrorString;

    private String lastDiagnostic;

    LearnerAttempt(ID attemptID) {
        this.attemptID = attemptID;
        state = State.INITIALIZED;
        learnerSessionMap = new ConcurrentHashMap<>();
        this.lastLearnerClosedReason = Reason.NONE;
        lastErrorCode = ScormError.E_0.getCode();
        lastErrorString = ScormError.E_0.getMsg();
        lastDiagnostic = ScormError.E_0.getMsg();
    }

    ID getAttemptID() {
        return attemptID;
    }

    boolean isJustCreate() {
        return state.equals(State.JUST_CREATE);
    }

    boolean isInitialized() {
        return state.equals(State.INITIALIZED);
    }

    boolean isOpen() {
        return state.equals(State.OPEN);
    }

    boolean isSuspended() {
        return state.equals(State.SUSPENDED);
    }

    boolean isClosed() {
        return state.equals(State.CLOSED);
    }

    public RuntimeData getRuntimeData() {
        return runtimeData;
    }

    String initialize(String parameter) {
        if (currentLearnerSession == null || currentLearnerSession.isNotOpen()
                || currentLearnerSession.isClosed() || !currentLearnerSession.isOpen()) {
            setError(ScormError.E_102);
            return "false";
        }
        if (StringUtils.isNotBlank(parameter)) {
            setError(ScormError.E_201);
            return "false";
        }
        ScormResult scormResult = currentLearnerSession.initialize();
        setError(scormResult.getError(), scormResult.getDiagnostic());
        return scormResult.getReturnValue();
    }

    public String terminate(String parameter) {
        if (currentLearnerSession == null || currentLearnerSession.isNotOpen()
                || currentLearnerSession.isClosed() || !currentLearnerSession.isOpen()) {
            setError(ScormError.E_111);
            return "false";
        }
        if (StringUtils.isNotBlank(parameter)) {
            setError(ScormError.E_201);
            return "false";
        }
        ScormResult scormResult = currentLearnerSession.terminate();
        setError(scormResult.getError(), scormResult.getDiagnostic());
        return scormResult.getReturnValue();
    }

    String getValue(String elementName) {
        if (currentLearnerSession == null || currentLearnerSession.isNotOpen()
                || currentLearnerSession.isClosed() || !currentLearnerSession.isOpen()) {
            setError(ScormError.E_301);
            return "";
        }
        if (currentLearnerSession.isCommunicationNotInitialized()) {
            setError(ScormError.E_122);
            return "";
        }
        if (currentLearnerSession.isCommunicationTerminated()) {
            setError(ScormError.E_123);
            return "";
        }
        ScormResult scormResult = ElementParser.parseGet(runtimeData, elementName);
        setError(scormResult.getError(), scormResult.getDiagnostic());
        return scormResult.getReturnValue();
    }

    String setValue(String elementName, String value) {
        if (currentLearnerSession == null || currentLearnerSession.isNotOpen()
                || currentLearnerSession.isClosed() || !currentLearnerSession.isOpen()) {
            setError(ScormError.E_351);
            return "";
        }
        if (currentLearnerSession.isCommunicationNotInitialized()) {
            setError(ScormError.E_132);
            return "";
        }
        if (currentLearnerSession.isCommunicationTerminated()) {
            setError(ScormError.E_133);
            return "";
        }
        ScormResult scormResult = ElementParser.parseSet(runtimeData, elementName, value);
        setError(scormResult.getError(), scormResult.getDiagnostic());
        return scormResult.getReturnValue();
    }

    String commit(String parameter) {
        if (currentLearnerSession == null || currentLearnerSession.isNotOpen()
                || currentLearnerSession.isClosed() || !currentLearnerSession.isOpen()) {
            setError(ScormError.E_391);
            return "false";
        }
        if (currentLearnerSession.isCommunicationNotInitialized()) {
            setError(ScormError.E_142);
            return "false";
        }
        if (currentLearnerSession.isCommunicationTerminated()) {
            setError(ScormError.E_143);
            return "false";
        }
        if (StringUtils.isNotBlank(parameter)) {
            setError(ScormError.E_201);
            return "false";
        }
        setError(ScormError.E_0);
        return "true";
    }

    String getLastErrorCode() {
        return String.valueOf(lastErrorCode);
    }

    String getLastErrorString() {
        return lastErrorString;
    }

    String getLastDiagnostic() {
        return lastDiagnostic;
    }

    boolean openLearnerSession() {
        if (isJustCreate()) {
            log.error("creat learner session before initialize run-time data");
            return false;
        }
        if (isClosed()) {
            log.error("learner attempt already closed");
            return false;
        }
        if (currentLearnerSession != null) {
            log.error("learner session already exist");
            return false;
        }
        currentLearnerSession = new LearnerSession();
        learnerSessionMap.put(currentLearnerSession.getSessionID(), currentLearnerSession);
        currentLearnerSession.open();
        clearExit();
        clearSessionTime();
        updateEntry();
        state = State.OPEN;
        return true;
    }

    public boolean closeLearnerSession(boolean isSuspendAllNavigationRequest) {
        if (isJustCreate()) {
            log.error("close learner session before initialize run-time data");
            return false;
        }
        if (isClosed()) {
            log.error("learner attempt already closed");
            return false;
        }
        if (currentLearnerSession == null) {
            log.error("learner session not exist");
            return false;
        }
        currentLearnerSession.close();
        currentLearnerSession = null;
        accumulateTotalTime();
        updateExitReason(isSuspendAllNavigationRequest);
        if (lastLearnerClosedReason == Reason.EXIT_BY_SUSPEND || lastLearnerClosedReason == Reason.SUSPEND_ALL_NAVIGATION_REQUEST) {
            state = State.SUSPENDED;
        } else {
            state = State.CLOSED;
        }
        return true;
    }

    // invoke when a new learner session created
    private void clearExit() {
        runtimeData.getCmi().getExit().getExit().setValue("");
    }

    // invoke when a new learner session created
    private void clearSessionTime() {
        runtimeData.getCmi().getSessionTime().getSessionTime().setValue(null);
    }

    // invoke when a new learner session created
    private void updateEntry() {
        if (learnerSessionMap.size() == 1) {
            runtimeData.getCmi().getEntry().getEntry().setValue("ab-initio");
        } else if (lastLearnerClosedReason == Reason.EXIT_BY_SUSPEND) {
            runtimeData.getCmi().getEntry().getEntry().setValue("resume");
        } else if (lastLearnerClosedReason == Reason.EXIT_BY_LOGOUT) {
            // a new learner attempt on the SCO is being beginning after a learner session
            runtimeData.getCmi().getEntry().getEntry().setValue("ab-initio");
        } else if (lastLearnerClosedReason == Reason.SUSPEND_ALL_NAVIGATION_REQUEST) {
            runtimeData.getCmi().getEntry().getEntry().setValue("resume");
        } else {
            runtimeData.getCmi().getEntry().getEntry().setValue("");
        }
    }

    private void updateExitReason(boolean isSuspendAllNavigationRequest) {
        if (isSuspendAllNavigationRequest) {
            lastLearnerClosedReason = Reason.SUSPEND_ALL_NAVIGATION_REQUEST;
        } else {
            String exit = runtimeData.getCmi().getExit().getExit().getValue();
            if ("suspend".equals(exit)) {
                lastLearnerClosedReason = Reason.EXIT_BY_SUSPEND;
            } else if ("logout".equals(exit)) {
                lastLearnerClosedReason = Reason.EXIT_BY_LOGOUT;
            } else {
                lastLearnerClosedReason = Reason.EXIT_BY_OTHERS;
            }
        }
    }

    // invoke when a learner session closed
    private void accumulateTotalTime() {
        String currentTotalTime = runtimeData.getCmi().getTotalTime().getTotalTime().getValue();
        String currentSessionTime = runtimeData.getCmi().getSessionTime().getSessionTime().getValue();
        if (currentSessionTime != null) {
            runtimeData.getCmi().getTotalTime().getTotalTime().setValue(
                    Duration.parse(currentTotalTime).plus(Duration.parse(currentSessionTime)).toString());
        }
    }

    boolean initRuntimeData(ContentPackage contentPackage, Item item, LMSLearnerInfo lmsLearnerInfo) {
        if (runtimeData != null) {
            return false;
        }
        if (contentPackage == null || item == null) {
            return false;
        }

        runtimeData = new RuntimeData();

        initCompletionThreshold(item);

        initLaunchData(item);

        initLearnerID(lmsLearnerInfo);

        initLearnerName(lmsLearnerInfo);

        initLearnerPreference();

        initMaximumTimeAllowed(item);

        initObjectives(contentPackage, item);

        initScaledPassingScore(contentPackage, item);

        initTimeLimitAction(item);

        initADLData(item);

        state = State.INITIALIZED;

        return true;
    }

    private void initCompletionThreshold(Item item) {
        if (item.getCompletionThreshold() != null && item.getCompletionThreshold().isCompletedByMeasure()) {
            runtimeData.getCmi().getCompletionThreshold().getCompletionThreshold().setValue(
                    new BigDecimal(item.getCompletionThreshold().getMinProgressMeasure().getDecimalValue().doubleValue())
                            .setScale(7, BigDecimal.ROUND_HALF_UP));
        } else if (item.parentIsItem()){
            initCompletionThreshold(item.getParentItem());
        } else if (item.parentIsOrganization() && item.getParentOrganization().getCompletionThreshold() != null
                && item.getParentOrganization().getCompletionThreshold().isCompletedByMeasure()) {
            runtimeData.getCmi().getCompletionThreshold().getCompletionThreshold().setValue(
                    new BigDecimal(item.getParentOrganization().getCompletionThreshold().getMinProgressMeasure().getDecimalValue().doubleValue())
                            .setScale(7, BigDecimal.ROUND_HALF_UP));
        }
    }

    private void initLaunchData(Item item) {
        if (StringUtils.isNotBlank(item.getDataFromLMS())) {
            runtimeData.getCmi().getLaunchData().getLaunchData().setValue(item.getDataFromLMS());
        }
    }

    private void initLearnerID(LMSLearnerInfo lmsLearnerInfo) {
        runtimeData.getCmi().getLearnerId().getLearnerId().setValue(lmsLearnerInfo.getLearnerID());
    }

    private void initLearnerName(LMSLearnerInfo lmsLearnerInfo) {
        runtimeData.getCmi().getLearnerName().getLearnerName().setValue(lmsLearnerInfo.getLearnerName());
    }

    private void initLearnerPreference() {
        runtimeData.getCmi().getLearnerPreference().getAudioLevel().getAudioLevel().setValue(
                new BigDecimal(1).setScale(7, BigDecimal.ROUND_HALF_UP));
        runtimeData.getCmi().getLearnerPreference().getLanguage().setLanguage("");
        runtimeData.getCmi().getLearnerPreference().getDeliverySpeed().setValue(
                new BigDecimal(1).setScale(7, BigDecimal.ROUND_HALF_UP));
        runtimeData.getCmi().getLearnerPreference().getAudioCaptioning().setValue("0");
    }

    private void initMaximumTimeAllowed(Item item) {
        if (item.getSequencing() != null && item.getSequencing().getLimitConditions() != null
                && StringUtils.isNotBlank(item.getSequencing().getLimitConditions().getAttemptAbsoluteDurationLimit())) {
            String attemptAbsoluteDurationLimit = item.getSequencing().getLimitConditions().getAttemptAbsoluteDurationLimit();
            runtimeData.getCmi().getMaximumTimeAllowed().getMaximumTimeAllowed().setValue(attemptAbsoluteDurationLimit);
        }
    }

    // TODO: SN RTE-4-96
    private void initObjectives(ContentPackage contentPackage, Item item) {
        if (item.getSequencing() == null) {
            return;
        }
        initObjectives(item.getSequencing());
        // TODO: 是否有必要
//        if (item.getSequencing().getIdRef() != null && StringUtils.isNotBlank(item.getSequencing().getIdRef().getValue())) {
//            Sequencing sequencing = CPUtils.findSequencingByID(contentPackage.getManifest().getSequencingCollection(),
//                    item.getSequencing().getIdRef().getValue());
//            if (sequencing != null) {
//                initObjectives(sequencing);
//            }
//        }
        runtimeData.getCmi().getObjectives().getCount().setValue(
                runtimeData.getCmi().getObjectives().getInstances().size());
    }

    private void initObjectives(Sequencing sequencing) {
        if (sequencing.getObjectives() == null) {
            return;
        }
        Objectives objectives = sequencing.getObjectives();
        Objective primaryObjective = objectives.getPrimaryObjective();
        if (primaryObjective.getObjectiveID() != null) {
            Instance primaryInstance = new Instance(runtimeData.getCmi().getObjectives());
            primaryInstance.getId().setValue(primaryObjective.getObjectiveID().getValue());
            runtimeData.getCmi().getObjectives().getInstances().add(primaryInstance);
        }
        for (Objective objective : objectives.getObjectiveList()) {
            Instance instance = new Instance(runtimeData.getCmi().getObjectives());
            instance.getId().setValue(objective.getObjectiveID().getValue());
            runtimeData.getCmi().getObjectives().getInstances().add(instance);
        }
    }

    private void initScaledPassingScore(ContentPackage contentPackage, Item item) {
        if (item.getSequencing() == null) {
            return;
        }
        Objectives objectives = null;
        if (item.getSequencing().getObjectives() != null) {
            objectives = item.getSequencing().getObjectives();
        } else {
            if (item.getSequencing().getIdRef() != null && StringUtils.isNotBlank(item.getSequencing().getIdRef().getValue())) {
                Sequencing sequencing = CPUtils.findSequencingByID(contentPackage.getManifest().getSequencingCollection(),
                        item.getSequencing().getIdRef().getValue());
                if (sequencing != null) {
                    objectives = sequencing.getObjectives();
                }
            }
        }
        if (objectives == null) {
            return;
        }
        Objective primaryObjective = objectives.getPrimaryObjective();
        if (primaryObjective == null) {
            return;
        }
        if (primaryObjective.isSatisfiedByMeasure()) {
            double value;
            if (primaryObjective.getMinNormalizedMeasure() != null
                    && primaryObjective.getMinNormalizedMeasure().getDecimalValue() != null) {
                value = primaryObjective.getMinNormalizedMeasure().getDecimalValue().doubleValue();
            } else {
                value = 1.0;
            }
            runtimeData.getCmi().getScaledPassingScore().getScaledPassingScore().setValue(
                    new BigDecimal(value).setScale(7, BigDecimal.ROUND_HALF_UP));
        }
    }

    private void initTimeLimitAction(Item item) {
        if (StringUtils.isNotBlank(item.getTimeLimitAction())) {
            runtimeData.getCmi().getTimeLimitAction().getTimeLimitAction().setValue(item.getTimeLimitAction());
        }
    }

    private void initADLData(Item item) {
        if (item.getData() != null) {
            for (com.corkili.learningserver.scorm.cam.model.Map map : item.getData().getMapList()) {
                Data.Instance instance = new Data.Instance();
                instance.getId().setValue(map.getTargetID().getValue());
                runtimeData.getAdl().getData().getInstances().add(instance);
            }
            runtimeData.getAdl().getData().getCount().setValue(runtimeData.getAdl().getData().getInstances().size());
        }
    }

    private void setError(ScormError scormError) {
        setError(scormError, scormError.getMsg());
    }

    private void setError(ScormError scormError, String diagnostic) {
        lastErrorCode = scormError.getCode();
        lastErrorString = scormError.getMsg();
        lastDiagnostic = diagnostic;
    }

    enum State {
        JUST_CREATE,
        INITIALIZED,
        OPEN,
        SUSPENDED,
        CLOSED
    }

    enum Reason {
        NONE,
        EXIT_BY_SUSPEND,
        EXIT_BY_LOGOUT,
        EXIT_BY_OTHERS,
        SUSPEND_ALL_NAVIGATION_REQUEST
    }

    static void main(String[] args) {
        System.out.println(Duration.ZERO.toString());
    }

}
