package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.api.behavior.result.NavigationBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingException;
import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest;
import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest.Type;
import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class NavigationBehavior {

    /**
     * Navigation Request Process [NB.2.1]
     *
     * For a navigation request and possibly a specified activity, returns the validity of the navigation request;
     * may return a termination request, a sequencing request, and/or a target activity; may return an exception code.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   Sequencing Control Choice SM.1
     *   Sequencing Control Choice Exit SM.1
     *   Sequencing Control Flow SM.1
     *   Sequencing Control Forward Only SM.1
     *   Available Children AM.1.1
     *   Suspended Activity AM.1.2
     */
    public static NavigationBehaviorResult processNavigationRequest(@NotNull NavigationRequest navigationRequest) {
        NavigationRequest.Type type = navigationRequest.getRequestType();
        ActivityTree activityTree = navigationRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        Activity suspendedActivity = activityTree.getGlobalStateInformation().getSuspendedActivity();
        if (type == Type.Start) { // 1
            // 1.1
            // Make sure the sequencing session has not already begun.
            if (currentActivity == null) {
                // 1.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(true)
                        .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Start, activityTree, null));
            } else { // 1.2
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB211);
            }
        } else if (type == Type.ResumeAll) { // 2
            // 2.1
            // Make sure the sequencing session has not already begun.
            if (currentActivity == null) {
                // 2.1.1
                // Make sure the previous sequencing ended with a suspend all request.
                if (suspendedActivity != null) {
                    // 2.1.1.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(true)
                            .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.ResumeAll, activityTree, null));
                } else { // 2.1.2
                    // 2.1.2.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(false)
                            .setException(SequencingException.NB213);
                }
            } else { // 2.2
                // 2.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB211);
            }
        } else if (type == Type.Continue) { // 3
            // 3.1
            // Make sure the sequencing session has already begun.
            if (currentActivity == null) {
                // 3.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
            // 3.2
            // Validate that a 'flow' sequencing request can be processed from the current activity.
            if (!activityTree.isRoot(currentActivity) && currentActivity.getParentActivity()
                    .getSequencingDefinition().getSequencingControlMode().isSequencingControlFlow()) {
                // 3.2.1
                // If the current activity has not been terminated, terminate the current the activity.
                if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                    // 3.2.1.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(true)
                            .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Exit, activityTree, null))
                            .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Continue, activityTree, null));
                } else { // 3.2.2
                    // 3.2.2.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(true)
                            .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Continue, activityTree, null));
                }
            } else { // 3.3
                // 3.3.1
                // Flow is not enabled or the current activity is the root of the activity tree.
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB214);
            }
        } else if (type == Type.Previous) { // 4
            // 4.1
            // Make sure the sequencing session has already begun.
            if (currentActivity == null) {
                // 4.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
            // 4.2
            // There is no activity logically 'previous' to the root of the activity tree.
            if (!activityTree.isRoot(currentActivity)) {
                // 4.2.1
                // Validate the a 'flow' sequencing request can be processed from the current activity.
                if (currentActivity.getParentActivity().getSequencingDefinition()
                        .getSequencingControlMode().isSequencingControlFlow()
                        && !currentActivity.getParentActivity().getSequencingDefinition()
                        .getSequencingControlMode().isSequencingControlForwardOnly()) {
                    // 4.2.1.1
                    // If the current activity has not been terminated, terminate the current the activity.
                    if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                        // 4.2.1.1.1
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(true)
                                .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Exit, activityTree, null))
                                .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Previous, activityTree, null));
                    } else { // 4.2.1.2
                        // 4.2.1.2.1
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(true)
                                .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Previous, activityTree, null));
                    }
                } else { // 4.2.2
                    // 4.2.2.1
                    // Violates control mode.
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(false)
                            .setException(SequencingException.NB215);
                }
            } else { // 4.3
                // 4.3.1
                // Cannot move backward from the root of the activity tree.
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB216);
            }
        } else if (type == Type.Forward) { // 5 Behavior not defined.
            // 5.1
            return new NavigationBehaviorResult()
                    .setValidNavigationRequest(false)
                    .setException(SequencingException.NB217);
        } else if (type == Type.Backward) { // 6 Behavior not defined.
            // 6.1
            return new NavigationBehaviorResult()
                    .setValidNavigationRequest(false)
                    .setException(SequencingException.NB217);
        } else if (type == Type.Choice) { // 7
            // 7.1
            // Make sure the target activity exists in the activity tree.
            Activity targetActivity = navigationRequest.getTargetActivity();
            if (activityTree.existActivity(targetActivity)) {
                // 7.1.1
                // Validate that a 'choice' sequencing request can be processed on the target activity.
                if (activityTree.isRoot(targetActivity) || targetActivity.getParentActivity()
                        .getSequencingDefinition().getSequencingControlMode().isSequencingControlChoice()) {
                    // 7.1.1.1
                    // Attempt to start the sequencing session through choice
                    if (currentActivity == null) {
                        // 7.1.1.1.1
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(true)
                                .setSequencingRequest(
                                        new SequencingRequest(SequencingRequest.Type.Choice, activityTree, targetActivity));
                    }
                    // 7.1.1.2
                    if (!currentActivity.isSiblingActivity(targetActivity)) {
                        // 7.1.1.2.1
                        Activity commonAncestor = activityTree.findCommonAncestorFor(currentActivity, targetActivity);
                        // 7.1.1.2.2
                        // The common ancestor will not terminate as a result of processing the choice sequencing
                        // request, unless the common ancestor is the Current Activity - the current activity should
                        // always be included in the activity path.
                        // From current activity to common ancestor
                        List<Activity> activityPath = new LinkedList<>();
                        if (commonAncestor != null) {
                            Activity tmp = currentActivity;
                            while (tmp != null) {
                                activityPath.add(tmp);
                                if (tmp.equals(commonAncestor)) {
                                    break;
                                }
                                tmp = tmp.getParentActivity();
                            }
                        }
                        // 7.1.1.2.3
                        if (!activityPath.isEmpty()) {
                            // 7.1.1.2.3.1
                            // Make sure that 'choosing' the target will not force an active activity to terminate,
                            // if that activity does not allow choice to terminate it.
                            for (Activity activity : activityPath) {
                                // 7.1.1.2.3.1.1
                                if (activity.getActivityStateInformation().isActivityIsActive()
                                        && !activity.getSequencingDefinition().getSequencingControlMode()
                                        .isSequencingControlChoiceExit()) {
                                    // 7.1.1.2.3.1.1.1
                                    // Violates control mode.
                                    return new NavigationBehaviorResult()
                                            .setValidNavigationRequest(false)
                                            .setException(SequencingException.NB218);
                                }
                            }
                        } else { // 7.1.1.2.4
                            // 7.1.1.2.4.1
                            return new NavigationBehaviorResult()
                                    .setValidNavigationRequest(false)
                                    .setException(SequencingException.NB219);
                        }
                    }
                    // 7.1.1.3
                    // The Choice target is a sibling to the Current Activity, check if the Current Activity
                    // is allowed to exit.
                    if (currentActivity.getActivityStateInformation().isActivityIsActive()
                            && !currentActivity.getSequencingDefinition().getSequencingControlMode()
                            .isSequencingControlChoiceExit()) {
                        // 7.1.1.3.1
                        // Violates control mode.
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(false)
                                .setException(SequencingException.NB218);
                    }
                    // 7.1.1.4
                    // If the current activity has not been terminated, terminate the current the activity.
                    if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                        // 7.1.1.4.1
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(true)
                                .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Exit, activityTree, null))
                                .setSequencingRequest(
                                        new SequencingRequest(SequencingRequest.Type.Choice, activityTree, targetActivity));
                    } else { // 7.1.1.5
                        // 7.1.1.5.1
                        return new NavigationBehaviorResult()
                                .setValidNavigationRequest(true)
                                .setSequencingRequest(
                                        new SequencingRequest(SequencingRequest.Type.Choice, activityTree, targetActivity));
                    }
                } else { // 7.1.2
                    // 7.1.2.1
                    // Violates control mode.
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(false)
                            .setException(SequencingException.NB2110);
                }
            } else { // 7.2
                // 7.2.1
                // Target activity does not exist.
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB2111);
            }
        } else if (type == Type.Exit) { // 8
            // 8.1
            // Make sure the sequencing session has already begun.
            if (currentActivity != null) {
                // 8.1.1
                // Make sure the current activity has not already been terminated.
                if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                    // 8.1.1.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(true)
                            .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Exit, activityTree, null))
                            .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree, null));
                } else { // 8.1.2
                    // 8.1.2.1
                    // Activity has already terminated.
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(false)
                            .setException(SequencingException.NB2112);
                }
            } else { // 8.2
                // 8.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
        } else if (type == Type.ExitAll) { // 9
            // 9.1
            // If the sequencing session has already begun, unconditionally terminate all active activities.
            if (currentActivity != null) {
                // 9.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(true)
                        .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.ExitAll, activityTree, null))
                        .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree, null));
            } else { // 9.2
                // 9.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
        } else if (type == Type.Abandon) { // 10
            // 10.1
            // Make sure the sequencing session has already begun.
            if (currentActivity != null) {
                // 10.1.1
                // Make sure the current activity has not already been terminated.
                if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                    // 10.1.1.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(true)
                            .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Abandon, activityTree, null))
                            .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree, null));
                } else { // 10.1.2
                    // 10.1.2.1
                    return new NavigationBehaviorResult()
                            .setValidNavigationRequest(false)
                            .setException(SequencingException.NB2112);
                }
            } else { // 10.2
                // 10.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
        } else if (type == Type.AbandonAll) { // 11
            // 11.1
            // If the sequencing session has already begun, unconditionally abandon all active activities.
            if (currentActivity != null) {
                // 11.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(true)
                        .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.AbandonAll, activityTree, null))
                        .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree, null));
            } else { // 11.2
                // 11.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
        } else if (type == Type.SuspendAll) { // 12
            // 12.1
            // If the sequencing session has already begun.
            if (currentActivity != null) {
                // 12.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(true)
                        .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.SuspendAll, activityTree, null))
                        .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree, null));
            } else { // 12.2
                // 12.2.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB212);
            }
        } else if (type == Type.Jump) { // 13
            // 13.1
            // Make sure the target activity exists in the activity tree and is available.
            Activity targetActivity = navigationRequest.getTargetActivity();
            if (activityTree.existActivity(targetActivity)
                    && targetActivity.getParentActivity().getActivityStateInformation()
                    .getAvailableChildren().contains(targetActivity)) {
                // 13.1.1
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(true)
                        .setTerminationRequest(new TerminationRequest(TerminationRequest.Type.Exit, activityTree, null))
                        .setSequencingRequest(
                                new SequencingRequest(SequencingRequest.Type.Jump, activityTree, targetActivity));
            } else { // 13.2
                // 13.2.1
                // Target activity does not exist.
                return new NavigationBehaviorResult()
                        .setValidNavigationRequest(false)
                        .setException(SequencingException.NB2111);
            }
        }

        // 14
        // Undefined navigation request.
        return new NavigationBehaviorResult()
                .setValidNavigationRequest(false)
                .setException(SequencingException.NB2113);
    }

}
