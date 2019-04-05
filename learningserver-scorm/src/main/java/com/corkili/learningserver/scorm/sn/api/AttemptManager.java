package com.corkili.learningserver.scorm.sn.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.SCORM;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.api.LearnerAttempt;
import com.corkili.learningserver.scorm.rte.api.SCORMRuntimeManager;
import com.corkili.learningserver.scorm.sn.api.behavior.OverallSequencingBehavior;
import com.corkili.learningserver.scorm.sn.api.behavior.result.OverallSequencingResult;
import com.corkili.learningserver.scorm.sn.api.event.EventTranslator;
import com.corkili.learningserver.scorm.sn.api.event.EventType;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

@Slf4j
public class AttemptManager {

    private final ID managerID;

    private final SequencingSession sequencingSession;

    private final AtomicInteger sessionCount;

    private final ActivityTree targetActivityTree;

    private ActivityAttempt activityAttempt;

    private final Map<Activity, ActivityAttempt> containedAttempts;

    private final List<EventType> processedEventTypeSeries;

    public AttemptManager(ActivityTree targetActivityTree) {
        this.targetActivityTree = targetActivityTree;
        this.managerID = targetActivityTree.getId();
        this.sessionCount = new AtomicInteger(0);
        this.processedEventTypeSeries = new ArrayList<>();
        this.containedAttempts = new HashMap<>();
        this.sequencingSession = new SequencingSession();
    }

    public ProcessResult process(NavigationEvent event) {
        SCORM.getInstance().mapRuntimeDataToTrackingInfo(targetActivityTree.getGlobalStateInformation().getCurrentActivity());
        processedEventTypeSeries.add(event.getType());
        Activity targetActivity = null;
        if (StringUtils.isNotBlank(event.getTargetActivityID())) {
            targetActivity = targetActivityTree.findActivityByID(generateID(event.getTargetActivityID()));
        } else {
            if (event.getType() == EventType.Choose || event.getType() == EventType.Jump) {
                return new ProcessResult("choose or jump event expected target activity");
            }
        }
        NavigationRequest request = EventTranslator.translateEventToRequestType(event, targetActivityTree, targetActivity);
        Activity oldCurrentActivity = targetActivityTree.getGlobalStateInformation().getCurrentActivity();
        boolean oldIsActive = oldCurrentActivity != null && oldCurrentActivity.getActivityStateInformation().isActivityIsActive();
        OverallSequencingResult result = OverallSequencingBehavior.overallSequencing(request);
        if (!updateAttempt()) {
            log.error("update ActivityAttempt error");
        }
        if (!result.isSuccess()) {
            log.error("Exception: {} - {}", result.getException().getCode(), result.getException().getDescription());
            return new ProcessResult(result.getException());
        }
        if (result.isExit()) {
            Activity currentActivity = targetActivityTree.getGlobalStateInformation().getCurrentActivity();
            if (currentActivity != null && !currentActivity.getActivityStateInformation().isActivityIsActive()) {
                targetActivityTree.getGlobalStateInformation().setCurrentActivity(null);
            }
        }
        if (result.isExit() && result.getEndSequencingSession()) {
            if (event.getType() == EventType.SuspendAll) {
                sequencingSession.suspend();
            } else {
                sequencingSession.close();
            }
        }
        Activity currentActivity = targetActivityTree.getGlobalStateInformation().getCurrentActivity();
        if (currentActivity != null && currentActivity.isLeaf() && currentActivity.getActivityStateInformation().isActivityIsActive()
                && (!currentActivity.equals(oldCurrentActivity) || !oldIsActive)) {
            // has a new delivery activity
            return new ProcessResult(currentActivity);
        } else {
            return new ProcessResult();
        }
    }

    private ID generateID(String id) {
        return new ID(id, managerID.getLmsContentPackageID(), managerID.getLmsLearnerID());
    }

    private boolean updateAttempt() {
        if (activityAttempt == null) {
            if (targetActivityTree.getRoot().getActivityStateInformation().isActivityIsActive()) {
                activityAttempt = createNewAttempt(targetActivityTree.getRoot(), null);
                containedAttempts.put(targetActivityTree.getRoot(), activityAttempt);
                sequencingSession.establish();
                sessionCount.incrementAndGet();
            }
        }
        if (activityAttempt == null) {
            return false;
        }
        List<Activity> shouldUpdateOrCreate = new ArrayList<>();
        List<Activity> shouldDelete = new ArrayList<>();
        checkAttemptState(shouldUpdateOrCreate, shouldDelete);
        syncAttempt(shouldUpdateOrCreate, shouldDelete);
        if (containedAttempts.isEmpty()) {
           activityAttempt = null;
           sequencingSession.close();
        }
        return true;
    }

    private void syncAttempt(List<Activity> shouldUpdateOrCreate, List<Activity> shouldDelete) {
        for (Activity activity : shouldUpdateOrCreate) {
            if (containedAttempts.containsKey(activity)) {
                updateAttemptState(containedAttempts.get(activity), activity);
            } else {
                containedAttempts.put(activity,
                        createNewAttempt(activity, containedAttempts.get(activity.getParentActivity())));
            }
        }
        for (int i = shouldDelete.size() - 1; i >= 0; i--) {
            Activity activity = shouldDelete.get(i);
            containedAttempts.remove(activity);
            LearnerAttempt learnerAttempt = SCORMRuntimeManager.getInstance().getLearnerAttempt(activity.getId());
            if (learnerAttempt != null) {
                learnerAttempt.terminate("");
//                learnerAttempt.closeLearnerSession(true);
            }
        }
    }

    private void checkAttemptState(List<Activity> shouldUpdateOrCreate, List<Activity> shouldDelete) {
        List<Activity> activities = targetActivityTree.preorder();
        for (Activity child : activities) {
            if (child.getActivityStateInformation().isActivityIsActive()
                    || child.getActivityStateInformation().isActivityIsSuspended()) { // update or create
                shouldUpdateOrCreate.add(child);
            } else { // delete
                shouldDelete.add(child);
            }
        }
    }

    private ActivityAttempt createNewAttempt(Activity activity, ActivityAttempt parent) {
        ActivityAttempt attempt = new ActivityAttempt(activity, parent);
        updateAttemptState(attempt, activity);
        return attempt;
    }

    private void updateAttemptState(ActivityAttempt attempt, Activity activity) {
        if (activity.getActivityStateInformation().isActivityIsActive()) {
            attempt.active();
        } else if (activity.getActivityStateInformation().isActivityIsSuspended()) {
            attempt.suspend();
        }
    }

    public ID getManagerID() {
        return managerID;
    }

    public SequencingSession getSequencingSession() {
        return sequencingSession;
    }

    public int getSessionCount() {
        return sessionCount.get();
    }

    public ActivityTree getTargetActivityTree() {
        return targetActivityTree;
    }

    public ActivityAttempt getActivityAttempt() {
        return activityAttempt;
    }

    public EventType getLastProcessedEventType() {
        if (processedEventTypeSeries.isEmpty()) {
            return null;
        }
        return processedEventTypeSeries.get(processedEventTypeSeries.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AttemptManager that = (AttemptManager) o;

        return new EqualsBuilder()
                .append(managerID, that.managerID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(managerID)
                .toHashCode();
    }
}
