package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingException;
import com.corkili.learningserver.scorm.sn.api.behavior.result.TerminationBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.UnifiedProcessResult;
import com.corkili.learningserver.scorm.sn.api.request.RollupRequest;
import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest.Type;
import com.corkili.learningserver.scorm.sn.api.request.UnifiedProcessRequest;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class TerminationBehavior {

    /**
     * Sequencing Exit Action Rules Subprocess [TB2.1]
     *
     * For the Current Activity; may change the Current Activity.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   End Attempt Process UP.4
     *   Sequencing Rules Check Process UP.2
     *   Sequencing Rule Description SM.2
     *   Terminate Descendent Attempts Process UP.3
     *
     * @see UnifiedProcess#processEndAttempt(UnifiedProcessRequest) UP.4
     * @see UnifiedProcess#processSequencingRulesCheck(UnifiedProcessRequest) UP.2
     * @see UnifiedProcess#processTerminateDescendentAttempts(UnifiedProcessRequest) UP.3
     */
    public static void processSequencingExitActionRules(TerminationRequest terminationRequest) {
        ActivityTree activityTree = terminationRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();

        // 1
        LinkedList<Activity> activityPath = new LinkedList<>();
        Activity tmp = currentActivity.getParentActivity();
        while (tmp != null) {
            activityPath.addFirst(tmp);
            tmp = tmp.getParentActivity();
        }
        // 2
        Activity exitTarget = null;
        // 3
        // Evaluate all exit rules along the active path, starting at the root of the activity tree.
        for (Activity activity : activityPath) {
            // 3.1
            UnifiedProcessResult unifiedProcessResult = UnifiedProcess.processSequencingRulesCheck(
                    new UnifiedProcessRequest(activityTree, activity)
                            .setConditionType(ConditionType.EXITCONDITION).setRuleActions("Exit"));
            // 3.2
            if (unifiedProcessResult.getAction() != null) {
                // 3.2.1
                // Stop at the first activity that has an exit rule evaluating to true
                exitTarget = activity;
                // 3.2.2
                break;
            }
        }
        // 4
        if (exitTarget != null) {
            // 4.1
            // End the current attempt on all active descendents.
            UnifiedProcess.processTerminateDescendentAttempts(
                    new UnifiedProcessRequest(terminationRequest.getTargetActivityTree(), exitTarget));
            // 4.2
            // End the current attempt on the 'exiting' activity
            UnifiedProcess.processEndAttempt(
                    new UnifiedProcessRequest(terminationRequest.getTargetActivityTree(), exitTarget));
            // 4.3
            // Move the current activity to the activity that identified for termination.
            activityTree.getGlobalStateInformation().setCurrentActivity(exitTarget);
        }
    }

    /**
     * Sequencing Post Condition Rules Subprocess [TB2.2]
     *
     * For the Current Activity; may return a termination request and a sequencing request.
     *
     * Reference:
     *   Activity is Suspended TM.1.1
     *   Current Activity AM.1.2
     *   Sequencing Rules Check Process UP.2
     *   Sequencing Rule Description SM.2
     *
     * @see UnifiedProcess#processSequencingRulesCheck(UnifiedProcessRequest) UP.2
     */
    public static TerminationBehaviorResult processSequencingPostConditionRules(TerminationRequest terminationRequest) {
        ActivityTree activityTree = terminationRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        // Do not apply post condition rules to a suspended activity.
        if (currentActivity.getActivityStateInformation().isActivityIsSuspended()) {
            // 1.1
            return new TerminationBehaviorResult();
        }
        // 2
        // Apply the post condition rules to the current activity.
        UnifiedProcessResult unifiedProcessResult = UnifiedProcess.processSequencingRulesCheck(
                new UnifiedProcessRequest(activityTree, currentActivity)
                        .setConditionType(ConditionType.POSTCONDITION)
                        .setRuleActions(ConditionType.POSTCONDITION.getRuleActionVocabularyTable()));
        // 3
        if (unifiedProcessResult.getAction() != null) {
            // 3.1
            if (Arrays.asList("Retry", "Continue", "Previous").contains(unifiedProcessResult.getAction())) {
                // 3.1.1
                // Attempt to override any pending sequencing request with this one.
                return new TerminationBehaviorResult()
                        .setSequencingRequest(new SequencingRequest(
                                SequencingRequest.Type.valueOf(unifiedProcessResult.getAction()),
                                activityTree, currentActivity));
            }
            // 3.2
            if (Arrays.asList("Exit Parent", "Exit All").contains(unifiedProcessResult.getAction())) {
                // 3.2.1
                // Terminate the appropriate activity(ies).
                TerminationRequest.Type type = "Exit Parent".equals(unifiedProcessResult.getAction()) ?
                        Type.ExitParent : Type.ExitAll;
                return new TerminationBehaviorResult()
                        .setTerminationRequest(new TerminationRequest(type, activityTree, currentActivity));
            }
            // 3.3
            if ("Retry All".equals(unifiedProcessResult.getAction())) {
                // 3.3.1
                // Terminate all active activities and move the current activity to the root of the activity tree;
                // then perform an "in-process" start.
                return new TerminationBehaviorResult()
                        .setTerminationRequest(new TerminationRequest(Type.ExitAll, activityTree, currentActivity))
                        .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Retry, activityTree, currentActivity));
            }
        }
        // 4
        return new TerminationBehaviorResult();
    }

    /**
     * Termination Request Process [TB.2.3]
     *
     * For a termination request, ends the current attempt on the Current Activity, returns the validity of the
     * termination request; may return a sequencing request; may return an exception code.
     *
     * Reference:
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Current Activity AM.1.2
     *   End Attempt Process UP.4
     *   Sequencing Exit Action Rules Subprocess TB.2.1
     *   Sequencing Post Condition Rules Subprocess TB.2.2
     *   Terminate Descendent Attempts Process UP.3
     *
     * @see UnifiedProcess#processEndAttempt(UnifiedProcessRequest) UP.4
     * @see TerminationBehavior#processSequencingExitActionRules(TerminationRequest) TB.2.1
     * @see TerminationBehavior#processSequencingPostConditionRules(TerminationRequest) TB.2.2
     * @see UnifiedProcess#processTerminateDescendentAttempts(UnifiedProcessRequest) UP.3
     */
    public static TerminationBehaviorResult processTerminationRequest(TerminationRequest terminationRequest) {
        ActivityTree activityTree = terminationRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        // If the sequencing session has not begun, there is nothing to terminate.
        if (currentActivity == null) {
            // 1.1
            return new TerminationBehaviorResult()
                    .setValidTerminationRequest(false).setException(SequencingException.TB231);
        }
        // 2
        // If the current activity has already been terminated, there is nothing to terminate.
        if ((terminationRequest.getRequestType() == Type.Exit || terminationRequest.getRequestType() == Type.Abandon)
                && !currentActivity.getActivityStateInformation().isActivityIsActive()) {
            // 2.1
            return new TerminationBehaviorResult()
                    .setValidTerminationRequest(false).setException(SequencingException.TB232);
        }
        // 3
        if (terminationRequest.getRequestType() == Type.Exit) {
            // 3.1
            // Ensure the state of the current activity is up to date.
            UnifiedProcess.processEndAttempt(new UnifiedProcessRequest(activityTree, currentActivity));
            // 3.2
            // Check if any of the current activity's ancestors need to terminate.
            processSequencingExitActionRules(new TerminationRequest(terminationRequest.getRequestType(), activityTree, currentActivity));
            // 3.3
            boolean processedExit;
            int count = 0;
            SequencingRequest returnedSR = null;
            do {
                // 3.3.1
                processedExit = false;
                // 3.3.2
                TerminationBehaviorResult terminationBehaviorResult = processSequencingPostConditionRules(
                        new TerminationRequest(terminationRequest.getRequestType(), activityTree, currentActivity));
                if (terminationBehaviorResult.getSequencingRequest() != null) {
                    count++;
                    returnedSR = terminationBehaviorResult.getSequencingRequest();
                }
                // 3.3.3
                if (terminationBehaviorResult.getTerminationRequest() != null
                        && terminationBehaviorResult.getTerminationRequest().getRequestType() == Type.ExitAll) {
                    // 3.3.3.1
                    terminationRequest.setRequestType(Type.ExitAll);
                    // 3.3.3.2
                    // Process an Exit All Termination Request.
                    break;
                }
                // 3.3.4
                // If we exit the parent of the current activity,
                // move the current activity to the parent of the current activity
                if (terminationBehaviorResult.getTerminationRequest() != null
                        && terminationBehaviorResult.getTerminationRequest().getRequestType() == Type.ExitParent) {
                    // 3.3.4.1
                    // The root of the activity tree does not have a parent to exit.
                    if (!activityTree.isRoot(currentActivity)) {
                        // 3.3.4.1.1
                        activityTree.getGlobalStateInformation().setCurrentActivity(currentActivity.getParentActivity());
                        currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
                        // 3.3.4.1.2
                        UnifiedProcess.processEndAttempt(new UnifiedProcessRequest(activityTree, currentActivity));
                        // 3.3.4.1.3
                        // Need to evaluate post conditions on the new current activity.
                        processedExit = true;
                    } else { // 3.3.4.2
                        // 3.3.4.2.1
                        return new TerminationBehaviorResult()
                                .setValidTerminationRequest(false).setException(SequencingException.TB234);
                    }
                } else { // 3.3.5
                    // 3.3.5.1
                    // If the attempt on the root of the Activity Tree is ending without a Retry,
                    // the Sequencing Session also ends.
                    if (activityTree.isRoot(currentActivity) && (terminationBehaviorResult.getSequencingRequest() != null
                            && terminationBehaviorResult.getSequencingRequest().getRequestType() != SequencingRequest.Type.Retry)) {
                        // 3.3.5.1.1
                        return new TerminationBehaviorResult().setValidTerminationRequest(true)
                                .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree,
                                        activityTree.getGlobalStateInformation().getCurrentActivity()));
                    }
                }
            } while (!processedExit); // 3.4
            // 3.5
            return new TerminationBehaviorResult().setValidTerminationRequest(true)
                    .setSequencingRequest(count == 1 ? returnedSR : null);
        }
        // 4
        if (terminationRequest.getRequestType() == Type.ExitAll) {
            // 4.1
            // Has the completion subprocess and rollup been applied to the current activity yet?
            if (currentActivity.getActivityStateInformation().isActivityIsActive()) {
                // 4.1.1
                UnifiedProcess.processEndAttempt(new UnifiedProcessRequest(activityTree, currentActivity));
            }
            // 4.2
            UnifiedProcess.processTerminateDescendentAttempts(
                    new UnifiedProcessRequest(activityTree, activityTree.getRoot()));
            // 4.3
            UnifiedProcess.processEndAttempt(new UnifiedProcessRequest(activityTree, activityTree.getRoot()));
            // 4.4
            // Move the current activity to the root of the activity tree.
            activityTree.getGlobalStateInformation().setCurrentActivity(activityTree.getRoot());
            currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
            // 4.5
            // Inform the sequencer that the sequencing session has ended.
            return new TerminationBehaviorResult().setValidTerminationRequest(true)
                    .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree,
                            activityTree.getGlobalStateInformation().getCurrentActivity()));
        } else if (terminationRequest.getRequestType() == Type.SuspendAll) { // 5
            // 5.1
            // If the current activity is active or already suspended,
            // suspend it and all of its descendents.
            if (currentActivity.getActivityStateInformation().isActivityIsActive()
                    || currentActivity.getActivityStateInformation().isActivityIsSuspended()) {
                // 5.1.1
                // Ensure that any status change to this activity is propagated through the entire activity tree.
                RollupBehavior.overallRollup(new RollupRequest(activityTree, currentActivity));
                // 5.1.2
                activityTree.getGlobalStateInformation().setSuspendedActivity(currentActivity);
            } else { // 5.2
                // 5.2.1
                // Make sure the current activity is not the root of the activity tree.
                if (!activityTree.isRoot(currentActivity)) {
                    // 5.2.1.1
                    activityTree.getGlobalStateInformation().setSuspendedActivity(currentActivity.getParentActivity());
                } else { // 5.2.2
                    // 5.2.2.1
                    // Nothing to suspend.
                    return new TerminationBehaviorResult().setValidTerminationRequest(false)
                            .setException(SequencingException.TB233);
                }
            }
            // 5.3
            List<Activity> activityPath = new LinkedList<>();
            Activity tmp = activityTree.getGlobalStateInformation().getSuspendedActivity();
            while (tmp != null) {
                activityPath.add(tmp);
                tmp = tmp.getParentActivity();
            }
            // 5.4
            if (activityPath.isEmpty()) {
                // 5.4.1
                // Nothing to suspend.
                return new TerminationBehaviorResult().setValidTerminationRequest(false)
                        .setException(SequencingException.TB235);
            }
            // 5.5
            for (Activity activity : activityPath) {
                // 5.5.1
                activity.getActivityStateInformation().setActivityIsActive(false);
                // 5.5.2
                activity.getActivityStateInformation().setActivityIsSuspended(true);
            }
            // 5.6
            // Move the current activity to the root of the activity tree.
            activityTree.getGlobalStateInformation().setCurrentActivity(activityTree.getRoot());
            // 5.7
            // Inform the sequencer that the sequencing session has ended.
            return new TerminationBehaviorResult().setValidTerminationRequest(true)
                    .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree,
                            activityTree.getGlobalStateInformation().getCurrentActivity()));
        } else if (terminationRequest.getRequestType() == Type.Abandon) { // 6
            // 6.1
            currentActivity.getActivityStateInformation().setActivityIsActive(false);
            // 6.2
            return new TerminationBehaviorResult().setValidTerminationRequest(true);
        } else if (terminationRequest.getRequestType() == Type.AbandonAll) { // 7
            // 7.1
            List<Activity> activityPath = new LinkedList<>();
            Activity tmp = currentActivity;
            while (tmp != null) {
                activityPath.add(tmp);
                tmp = tmp.getParentActivity();
            }
            // 7.2
            if (activityPath.isEmpty()) {
                // 7.2.1
                // Nothing to abandon
                return new TerminationBehaviorResult()
                        .setValidTerminationRequest(false)
                        .setException(SequencingException.TB236);
            }
            // 7.3
            for (Activity activity : activityPath) {
                // 7.3.1
                activity.getActivityStateInformation().setActivityIsActive(false);
            }
            // 7.4
            // Move the current activity to the root of the activity tree.
            activityTree.getGlobalStateInformation().setCurrentActivity(activityTree.getRoot());
            // 7.5
            // Inform the sequencer that the sequencing session has ended.
            return new TerminationBehaviorResult()
                    .setValidTerminationRequest(true)
                    .setSequencingRequest(new SequencingRequest(SequencingRequest.Type.Exit, activityTree,
                            activityTree.getGlobalStateInformation().getCurrentActivity()));
        }
        // Undefined termination request.
        return new TerminationBehaviorResult().setValidTerminationRequest(false)
                .setException(SequencingException.TB237);
    }





















































}
