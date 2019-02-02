package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.LinkedList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.api.behavior.result.DeliveryBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingException;
import com.corkili.learningserver.scorm.sn.api.behavior.result.UtilityProcessResult;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;
import com.corkili.learningserver.scorm.sn.api.request.UtilityProcessRequest;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class DeliveryBehavior {

    /**
     * Delivery Request Process [DB.1.1]
     *
     * For a delivery request; returns the validity of the delivery request; may return an exception code.
     *
     * Reference:
     *   Check Activity Process UP.5
     *
     * @see UtilityProcess#processCheckActivity(UtilityProcessRequest) UP.5
     */
    public static DeliveryBehaviorResult processDeliveryRequest(DeliveryRequest deliveryRequest) {
        ActivityTree activityTree = deliveryRequest.getTargetActivityTree();
        Activity targetActivity = deliveryRequest.getTargetActivity();
        // 1
        // Can only deliver leaf activities.
        if (!targetActivity.isLeaf()) {
            // 1.1
            return new DeliveryBehaviorResult()
                    .setValidDeliveryRequest(false)
                    .setException(SequencingException.DB111);
        }
        // 2
        // from the root of the activity tree to the activity specified in the delivery request, inclusive
        LinkedList<Activity> activityPath = new LinkedList<>();
        Activity tmp = targetActivity;
        while (tmp != null) {
            activityPath.addFirst(tmp);
            tmp = tmp.getParentActivity();
        }
        // 3
        // Nothing to deliver.
        if (activityPath.isEmpty()) {
            // 3.1
            return new DeliveryBehaviorResult()
                    .setValidDeliveryRequest(false)
                    .setException(SequencingException.DB112);
        }
        // 4
        // Make sure each activity along the path is allowed.
        for (Activity activity : activityPath) {
            // 4.1
            UtilityProcessResult result = UtilityProcess
                    .processCheckActivity(new UtilityProcessRequest(activityTree, activity));
            // 4.2
            if (result.getResult()) {
                // 4.2.1
                return new DeliveryBehaviorResult()
                        .setValidDeliveryRequest(false)
                        .setException(SequencingException.DB113);
            }
        }
        return new DeliveryBehaviorResult().setValidDeliveryRequest(true);
    }

    /**
     * Content Delivery Environment Process [DB.2]
     *
     * For a delivery request; may return an exception code.
     *
     * Reference:
     *   Activity Progress Status TM.1.2.1
     *   Activity Attempt Count TM.1.2.1
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Attempt Absolute Duration TM.1.2.2
     *   Attempt Experienced Duration TM.1.2.2
     *   Attempt Progress Information TM.1.2.2
     *   Clear Suspended Activity Subprocess DB.2.1
     *   Current Activity AM.1.2
     *   Objective Progress Information TM.1.1
     *   Suspended Activity AM.1.2
     *   Terminate Descendent Attempts Process UP.4
     *   Tracked SM.11
     * @see DeliveryBehavior#processClearSuspendedActivity(DeliveryRequest) DB.2.1
     * @see UtilityProcess#processTerminateDescendentAttempts(UtilityProcessRequest) UP.4
     */
    public static DeliveryBehaviorResult processContentDeliveryEnvironment(DeliveryRequest deliveryRequest) {
        ActivityTree activityTree = deliveryRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        Activity suspendedActivity = activityTree.getGlobalStateInformation().getSuspendedActivity();
        Activity identifiedActivity = deliveryRequest.getTargetActivity();
        // 1
        // If the attempt on the current activity has not been terminated, we cannot deliver new content.
        if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
            // 1.1
            // Delivery request is invalid - The Current Activity has not been terminated.
            return new DeliveryBehaviorResult().setException(SequencingException.DB21);
        }
        // 2
        // Content is about to be delivered, clear any existing suspend all state.
        if (!identifiedActivity.equals(suspendedActivity)) {
            // 2.1
            processClearSuspendedActivity(deliveryRequest);
        }
        // 3
        // Make sure that all attempts that should end are terminated.
        UtilityProcess.processTerminateDescendentAttempts(new UtilityProcessRequest(activityTree, identifiedActivity));
        // 4
        // Begin all attempts required to deliver the identified activity.
        LinkedList<Activity> activityPath = new LinkedList<>();
        Activity tmp = identifiedActivity;
        while (tmp != null) {
            activityPath.addFirst(tmp);
            tmp = tmp.getParentActivity();
        }
        // 5
        for (Activity activity : activityPath) {
            // 5.1
            if (!activity.getActivityStateInformation().isActivityIsActive()) {
                // 5.1.1
                if (activity.getSequencingDefinition().getDeliveryControls().isTracked()) {
                    // 5.1.1.1
                    // If the previous attempt on the activity ended due to a suspension,
                    // clear the suspended state; do not start a new attempt.
                    if (activity.getActivityStateInformation().isActivityIsSuspended()) {
                        // 5.1.1.1.1
                        activity.getActivityStateInformation().setActivityIsSuspended(false);
                    } else { // 5.1.1.2
                        // 5.1.1.2.1
                        // Begin a new attempt on the activity.
                        activity.getActivityProgressInformation().getActivityAttemptCount().setValue(
                                activity.getActivityProgressInformation().getActivityAttemptCount().getValue() + 1);
                        // 5.1.1.2.2
                        // Is this the first attempt on the activity?
                        if (activity.getActivityProgressInformation().getActivityAttemptCount().getValue() == 1) {
                            // 5.1.1.2.2.1
                            activity.getActivityProgressInformation().setActivityProgressStatus(true);
                        }
                        // 5.1.1.2.3
                        // Initialize tracking information for the new attempt.
                        activity.getAttemptProgressInformation().reinit();
                        for (ObjectiveDescription objectiveDescription :
                                activity.getSequencingDefinition().getObjectiveDescriptions()) {
                            objectiveDescription.getObjectiveProgressInformation().reinit();
                        }
                    }
                }
                // 5.1.2
                activity.getActivityStateInformation().setActivityIsActive(true);
            }
        }
        // 6
        // The activity identified for delivery becomes the current activity.
        activityTree.getGlobalStateInformation().setCurrentActivity(identifiedActivity);
        // 7
        activityTree.getGlobalStateInformation().setSuspendedActivity(null);
        // 8
        // The delivery environment is assumed to deliver the content resources associated with the
        // identified activity. While the activity is assumed to be active, the sequencer may track learner status.
        // Once the delivery of the activity’s content resources and auxiliary resources begins
        // 8.1
//      if (!identifiedActivity.getSequencingDefinition().getDeliveryControls().isTracked()) {
            // 8.1.1
            // The Objective and Attempt Progress information for the activity should not be recorded during delivery
            // 8.1.2
            // The delivery environment begins tracking the Attempt Absolute Duration and the Attempt Experienced Duration
//      }
        return new DeliveryBehaviorResult();
    }

    /**
     * Clear Suspended Activity Subprocess [DB.2.1]
     *
     * For an activity; may change the Suspended Activity.
     *
     * Reference:
     *   Activity is Suspended AM.1.1
     *   Suspended Activity AM.1.2
     */
    public static void processClearSuspendedActivity(DeliveryRequest deliveryRequest) {
        ActivityTree activityTree = deliveryRequest.getTargetActivityTree();
        Activity suspendedActivity = activityTree.getGlobalStateInformation().getSuspendedActivity();
        Activity identifiedActivity = deliveryRequest.getTargetActivity();
        // 1
        // Make sure there is something to clear.
        if (suspendedActivity != null) {
            // 1.1
            Activity commonAncestor = activityTree.findCommonAncestorFor(identifiedActivity, suspendedActivity);
            // 1.2
            // from the Suspended Activity to the common ancestor, inclusive
            List<Activity> activityPath = new LinkedList<>();
            Activity tmp = suspendedActivity;
            while (tmp != null) {
                activityPath.add(tmp);
                if (tmp.equals(commonAncestor)) {
                    break;
                }
                tmp = tmp.getParentActivity();
            }
            // 1.3
            if (!activityPath.isEmpty()) {
                // 1.3.1
                // Walk down the tree setting each of the identified activities to 'not suspended'
                for (Activity activity : activityPath) {
                    // 1.3.1.1
                    if (activity.isLeaf()) {
                        // 1.3.1.1.1
                        activity.getActivityStateInformation().setActivityIsSuspended(false);
                    } else { // 1.3.1.2
                        // 1.3.1.2.1
                        if (activity.getChildren().stream()
                                .noneMatch(a -> a.getActivityStateInformation().isActivityIsSuspended())) {
                            // 1.3.1.2.1.1
                            activity.getActivityStateInformation().setActivityIsSuspended(false);
                        }
                    }
                }
            }
            // 1.4
            // Clear the suspended activity attribute.
            activityTree.getGlobalStateInformation().setSuspendedActivity(null);
        }
        // 2
        // exit
    }

}
