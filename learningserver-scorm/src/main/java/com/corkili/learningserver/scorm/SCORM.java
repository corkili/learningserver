package com.corkili.learningserver.scorm;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.api.LearnerAttempt;
import com.corkili.learningserver.scorm.rte.api.SCORMRuntimeManager;
import com.corkili.learningserver.scorm.rte.model.Objectives.Instance;
import com.corkili.learningserver.scorm.rte.model.RuntimeData;
import com.corkili.learningserver.scorm.sn.api.AttemptManager;
import com.corkili.learningserver.scorm.sn.api.ProcessResult;
import com.corkili.learningserver.scorm.sn.api.SCORMSeqNavManager;
import com.corkili.learningserver.scorm.sn.api.event.EventType;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ObjectiveProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

@Slf4j
public class SCORM {

    private static SCORM instance;

    private SCORMPackageManager packageManager;

    private SCORMRuntimeManager runtimeManager;

    private SCORMSeqNavManager snManager;

    private SCORM() {
        packageManager = SCORMPackageManager.getInstance();
        runtimeManager = SCORMRuntimeManager.getInstance();
        snManager = SCORMSeqNavManager.getInstance();
    }

    public static SCORM getInstance() {
        if (instance == null) {
            synchronized (SCORM.class) {
                if (instance == null) {
                    instance = new SCORM();
                }
            }
        }
        return instance;
    }

    public SCORMResult process(NavigationEvent event, ID activityTreeID) {
        if (event == null || activityTreeID == null) {
            return new SCORMResult("Illegal Arguments");
        }
        ProcessResult processResult = snManager.process(activityTreeID, event);
        if (!processResult.isSuccess()) {
            return new SCORMResult(processResult.getErrorMsg());
        }
        if (processResult.getDeliveryActivity() == null) {
            return new SCORMResult();
        }
        Activity deliveryActivity = processResult.getDeliveryActivity();
        ContentPackage contentPackage = packageManager.launch(activityTreeID.getLmsContentPackageID());
        if (contentPackage == null) {
            return new SCORMResult(CommonUtils.format("launch content package \"{}\" failed",
                    activityTreeID.getLmsContentPackageID()));
        }
        DeliveryContent deliveryContent = contentPackage.getDeliveryContent(deliveryActivity.getId().getIdentifier());
        if (deliveryContent == null) {
            return new SCORMResult(CommonUtils.format("obtain delivery content \"{}\" failed",
                    deliveryActivity.getId().getIdentifier()));
        }
//        if (!mapTrackingInfoToRuntimeData(deliveryActivity)) {
//            return new SCORMResult("sync Tracking Information and Run-time Data failed");
//        }
        return new SCORMResult(deliveryContent, deliveryActivity);
    }

    public boolean mapTrackingInfoToRuntimeData(Activity activity) {
        LearnerAttempt learnerAttempt = runtimeManager.getLearnerAttempt(activity.getId());
        if (learnerAttempt == null) {
            log.error("Learner Attempt \"{}\" not exist", activity.getId());
            return false;
        }
        RuntimeData runtimeData = learnerAttempt.getRuntimeData();
        for (Instance instance : runtimeData.getCmi().getObjectives().getInstances()) {
            ObjectiveDescription objectiveDescription = activity.getSequencingDefinition()
                    .findObjectiveDescriptionByID(instance.getId().getValue());
            ObjectiveProgressInformation information = objectiveDescription.getObjectiveProgressInformation();
            if (information.isObjectiveProgressStatus()) {
                instance.getSuccessStatus().setValue(information.isObjectiveSatisfiedStatus() ? "passed" : "failed");
            }
            if (information.isObjectiveMeasureStatus()) {
                instance.getScore().getScaled().setValue(information.getObjectiveNormalizedMeasure()
                        .getValue().setScale(7, BigDecimal.ROUND_HALF_UP));
            }
        }
        AttemptProgressInformation attemptProgressInformation = activity.getAttemptProgressInformation();
        if (attemptProgressInformation.isAttemptProgressStatus()) {
            runtimeData.getCmi().getCompletionStatus().getCompletionStatus().setValue(attemptProgressInformation
                    .isAttemptCompletionStatus() ? "completed" : "incomplete");
        }
        if (attemptProgressInformation.isAttemptCompletionAmountStatus()) {
            runtimeData.getCmi().getProgressMeasure().getProgressMeasure().setValue(attemptProgressInformation
                    .getAttemptCompletionAmount().getValue().setScale(7, BigDecimal.ROUND_HALF_UP));
        }
        return true;
    }

    public void mapRuntimeDataToTrackingInfo(Activity activity) {
        log.info("mapRuntimeDataToTrackingInfo: {}", activity.getId());
        LearnerAttempt learnerAttempt = runtimeManager.getLearnerAttempt(activity.getId());
        if (learnerAttempt == null) {
            log.error("mapRuntimeDataToTrackingInfo: Learner Attempt \"{}\" not exist", activity.getId());
            return;
        }
        ID attemptManagerID = activity.getBelongActivityTreeID();
        AttemptManager attemptManager = snManager.findAttemptManagerBy(attemptManagerID);
        if (attemptManager == null) {
            log.error("mapRuntimeDataToTrackingInfo: Attempt Manager \"{}\" not exist", attemptManagerID);
            return;
        }
        if (attemptManager.getLastProcessedEventType() == EventType.Abandon
                || attemptManager.getLastProcessedEventType() == EventType.AbandonAll) {
            return;
        }
        RuntimeData runtimeData = learnerAttempt.getRuntimeData();
        AttemptProgressInformation attemptProgressInformation = activity.getAttemptProgressInformation();
        for (Instance instance : runtimeData.getCmi().getObjectives().getInstances()) {
            ObjectiveDescription objectiveDescription = activity.getSequencingDefinition()
                    .findObjectiveDescriptionByID(instance.getId().getValue());

            if (objectiveDescription == null) {
                continue;
            }

            ObjectiveProgressInformation information = objectiveDescription.getObjectiveProgressInformation();

            if (instance.getSuccessStatus().getValue().equals("unknown")) {
                information.setObjectiveProgressStatus(false);
                information.setObjectiveSatisfiedStatus(false);
            } else {
                information.setObjectiveProgressStatus(true);
                information.setObjectiveSatisfiedStatus("passed".equals(instance.getSuccessStatus().getValue()));
            }

            if (instance.getScore().getScaled().getValue() == null) {
                information.setObjectiveMeasureStatus(false);
                information.setObjectiveNormalizedMeasure(0.0);
            } else {
                information.setObjectiveMeasureStatus(true);
                information.setObjectiveNormalizedMeasure(instance.getScore().getScaled().getValue().doubleValue());
            }

            if (instance.getCompletionStatus().getValue().equals("unknown")) {
                information.setObjectiveCompletionProgressStatus(false);
                information.setObjectiveCompletionStatus(false);
            } else {
                information.setObjectiveCompletionProgressStatus(true);
                information.setObjectiveCompletionStatus("completed".equals(instance.getCompletionStatus().getValue()));
            }

            if (instance.getProgressMeasure().getValue() == null) {
                information.setObjectiveCompletionAmountStatus(false);
                information.setObjectiveCompletionAmount(0.0);
            } else {
                information.setObjectiveCompletionAmountStatus(false);
                information.setObjectiveCompletionAmount(instance.getProgressMeasure().getValue().doubleValue());
            }

            if (objectiveDescription.isObjectiveContributesToRollup()) { // primary
                if (instance.getCompletionStatus().getValue().equals("unknown")) {
                    attemptProgressInformation.setAttemptProgressStatus(false);
                    attemptProgressInformation.setAttemptCompletionStatus(false);
                } else {
                    attemptProgressInformation.setAttemptProgressStatus(true);
                    attemptProgressInformation.setAttemptCompletionStatus(
                            "completed".equals(instance.getCompletionStatus().getValue()));
                }

                if (instance.getProgressMeasure().getValue() == null) {
                    attemptProgressInformation.setAttemptCompletionAmountStatus(false);
                    attemptProgressInformation.getAttemptCompletionAmount().setValue(0.0);
                } else {
                    attemptProgressInformation.setAttemptCompletionAmountStatus(true);
                    attemptProgressInformation.getAttemptCompletionAmount().setValue(
                            instance.getProgressMeasure().getValue().doubleValue());
                }
            }
        }

        if (runtimeData.getCmi().getCompletionStatus().getCompletionStatus().getValue().equals("unknown")) {
            attemptProgressInformation.setAttemptProgressStatus(false);
            attemptProgressInformation.setAttemptCompletionStatus(false);
        } else {
            attemptProgressInformation.setAttemptProgressStatus(true);
            attemptProgressInformation.setAttemptCompletionStatus(
                    "completed".equals(runtimeData.getCmi().getCompletionStatus().getCompletionStatus().getValue()));
        }
        if (runtimeData.getCmi().getProgressMeasure().getProgressMeasure().getValue() == null) {
            attemptProgressInformation.setAttemptCompletionAmountStatus(false);
            attemptProgressInformation.getAttemptCompletionAmount().setValue(0.0);
        } else {
            attemptProgressInformation.setAttemptCompletionAmountStatus(true);
            attemptProgressInformation.getAttemptCompletionAmount().setValue(
                    runtimeData.getCmi().getProgressMeasure().getProgressMeasure().getValue().doubleValue());
        }

        ObjectiveDescription objectiveDescription = activity.getSequencingDefinition().getPrimaryObjectiveDescription();

        if (objectiveDescription == null) {
            return;
        }

        ObjectiveProgressInformation information = objectiveDescription.getObjectiveProgressInformation();

        if (runtimeData.getCmi().getSuccessStatus().getSuccessStatus().getValue().equals("unknown")) {
            information.setObjectiveProgressStatus(false);
            information.setObjectiveSatisfiedStatus(false);
        } else {
            information.setObjectiveProgressStatus(true);
            information.setObjectiveSatisfiedStatus(
                    "passed".equals(runtimeData.getCmi().getSuccessStatus().getSuccessStatus().getValue()));
        }

        if (runtimeData.getCmi().getScore().getScaled().getValue() == null) {
            information.setObjectiveMeasureStatus(false);
            information.setObjectiveNormalizedMeasure(0.0);
        } else {
            information.setObjectiveMeasureStatus(true);
            information.setObjectiveNormalizedMeasure(runtimeData.getCmi().getScore().getScaled().getValue().doubleValue());
        }

        if (runtimeData.getCmi().getCompletionStatus().getCompletionStatus().getValue().equals("unknown")) {
            information.setObjectiveCompletionProgressStatus(false);
            information.setObjectiveCompletionStatus(false);
        } else {
            information.setObjectiveCompletionProgressStatus(true);
            information.setObjectiveCompletionStatus("completed".equals(runtimeData.getCmi().getCompletionStatus().getCompletionStatus().getValue()));
        }

        if (runtimeData.getCmi().getProgressMeasure().getProgressMeasure().getValue() == null) {
            information.setObjectiveCompletionAmountStatus(false);
            information.setObjectiveCompletionAmount(0.0);
        } else {
            information.setObjectiveCompletionAmountStatus(false);
            information.setObjectiveCompletionAmount(runtimeData.getCmi().getProgressMeasure().getProgressMeasure().getValue().doubleValue());
        }
    }

    public SCORMPackageManager getPackageManager() {
        return packageManager;
    }

    public SCORMRuntimeManager getRuntimeManager() {
        return runtimeManager;
    }

    public SCORMSeqNavManager getSnManager() {
        return snManager;
    }

}
