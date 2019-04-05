package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.api.behavior.result.UtilityProcessResult;
import com.corkili.learningserver.scorm.sn.api.request.RollupRequest;
import com.corkili.learningserver.scorm.sn.api.request.UtilityProcessRequest;
import com.corkili.learningserver.scorm.sn.api.util.BooleanUtils;
import com.corkili.learningserver.scorm.sn.model.definition.DeliveryControls;
import com.corkili.learningserver.scorm.sn.model.definition.LimitConditions;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.RuleCondition;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ObjectiveProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class UtilityProcess {

    /**
     * Limit Conditions Check Process [UP.1]
     *
     * For an activity; returns True if any of the activity's limit conditions have been violated.
     *
     * Reference:
     *   Activity Attempt Count TM.1.2.1
     *   Activity Progress Status TM.1.2.1
     *   Activity Absolute Duration TM.1.2.1
     *   Activity Experienced Duration TM.1.2.1
     *   Attempt Progress Status TM.1.2.2
     *   Attempt Absolute Duration TM.1.2.2
     *   Attempt Experienced Duration TM.1.2.2
     *   Limit Condition Activity Absolute Duration Control SM.3
     *   Limit Condition Activity Absolute Duration Limit SM.3
     *   Limit Condition Activity Experienced Duration Control SM.3
     *   Limit Condition Activity Experienced Duration Limit SM.3
     *   Limit Condition Attempt Absolute Duration Control SM.3
     *   Limit Condition Attempt Absolute Duration Limit SM.3
     *   Limit Condition Attempt Experienced Duration Control SM.3
     *   Limit Condition Attempt Control SM.3
     *   Limit Condition Attempt Limit SM.3
     *   Limit Condition Begin Time Limit SM.3
     *   Limit Condition Begin Time Limit Control SM.3
     *   Limit Condition End Time Limit SM.3
     *   Limit Condition End Time Limit Control SM.3
     *   Tracked SM.11
     */
    public static UtilityProcessResult processLimitConditionsCheck(UtilityProcessRequest utilityProcessRequest) {
        Activity targetActivity = utilityProcessRequest.getTargetActivity();
        LimitConditions limitConditions = targetActivity.getSequencingDefinition().getLimitConditions();
        // 1
        // If the activity is not tracked, its limit conditions cannot be violated.
        if (!targetActivity.getSequencingDefinition().getDeliveryControls().isTracked()) {
            // 1.1
            // Activity is not tracked, no limit conditions can be violated, exit UP.1.
            return new UtilityProcessResult().setLimitConditionViolated(false);
        }
        // 2
        // Only need to check activities that will begin a new attempt.
        if (targetActivity.getActivityStateInformation().isActivityIsActive()
                || targetActivity.getActivityStateInformation().isActivityIsSuspended()) {
            // 2.1
            return new UtilityProcessResult().setLimitConditionViolated(false);
        }
        // 3
        if (limitConditions.isAttemptControl()) {
            // 3.1
            if (targetActivity.getActivityProgressInformation().isActivityProgressStatus()
                    && targetActivity.getActivityProgressInformation().getActivityAttemptCount().getValue()
                    >= limitConditions.getAttemptLimit().getValue()) {
                // 3.1.1
                // Limit conditions have been violated.
                return new UtilityProcessResult().setLimitConditionViolated(true);
            }
        }
        // The following code (from 4 to 9) is optionally in this SCORM 2004 4th edition, refer SN-C-64
        // 10
        // No limit conditions have been violated.
        return new UtilityProcessResult().setLimitConditionViolated(false);
    }

    /**
     * Sequencing Rules Check Process [UP.2]
     *
     * For an activity and a set of Rule Actions; returns the action to apply or Nil.
     *
     * Reference:
     *   Rule Action SM.2
     *   Sequencing Rule Check Subprocess UP.2.1
     *   Sequencing Rule Description SM.2
     *
     * @see UtilityProcess#processSequencingRuleCheck(UtilityProcessRequest) UP.2.1
     */
    public static UtilityProcessResult processSequencingRulesCheck(UtilityProcessRequest utilityProcessRequest) {
        Activity activity = utilityProcessRequest.getTargetActivity();
        List<String> ruleActions = Arrays.asList(utilityProcessRequest.getRuleActions());
        // 1
        // Make sure the activity rules to evaluate
        if (!activity.getSequencingDefinition().getSequencingRuleDescriptions().isEmpty()) {
            // 1.1
            List<SequencingRuleDescription> ruleList = new LinkedList<>();
            for (SequencingRuleDescription sequencingRuleDescription : activity.getSequencingDefinition().getSequencingRuleDescriptions()) {
                if (sequencingRuleDescription.getConditionType() == utilityProcessRequest.getConditionType() &&
                        ruleActions.contains(sequencingRuleDescription.getRuleAction().getValue())
                        && !sequencingRuleDescription.getRuleAction().getValue().equals("Ignore")) {
                    ruleList.add(sequencingRuleDescription);
                }
            }
            // 1.2
            for (SequencingRuleDescription rule : ruleList) {
                // 1.2.1
                // Evaluate each rule, one at a time
                UtilityProcessResult utilityProcessResult = processSequencingRuleCheck(
                        new UtilityProcessRequest(utilityProcessRequest.getTargetActivityTree(), activity)
                                .setSequencingRuleDescription(rule));
                // 1.2.2
                if (utilityProcessResult.getResult() != null && utilityProcessResult.getResult()) {
                    // 1.2.2.1
                    // Stop at the first rule that evaluates to true - perform the associated action.
                    return new UtilityProcessResult().setAction(rule.getRuleAction().getValue());
                }
            }
        }
        return new UtilityProcessResult().setAction(null);
    }

    /**
     * Sequencing Rule Check Subprocess [UP.2.1]
     *
     * For an activity and a Sequencing Rule; returns True if the rule applies, False if the rule does not apply,
     * and Unknown if the condition(s) cannot be evaluated.
     *
     * Reference:
     *   Rule Combination SM.2
     *   Rule Condition SM.2
     *   Rule Condition Operator SM.2
     *   Sequencing Rule Description SM.2
     *   Track Model TM
     */
    public static UtilityProcessResult processSequencingRuleCheck(UtilityProcessRequest utilityProcessRequest) {
        // 1
        // This is used to keep track of the evaluation of the rule's conditions.
        List<Boolean> evaluateResults = new LinkedList<>();
        // 2
        for (RuleCondition ruleCondition : utilityProcessRequest.getSequencingRuleDescription().getRuleConditions()) {
            // 2.1
            // evaluate each condition against the activity's tracking information.
            Boolean evaluateResult = evaluateRuleCondition(ruleCondition, utilityProcessRequest.getTargetActivity());
            // 2.2
            if (ruleCondition.getOperator().getValue().equals("Not")) {
                // 2.2.1
                // Negating "unknown" results in "unknown".
                evaluateResult = BooleanUtils.negate(evaluateResult);
            }
            // 2.3
            // Add the evaluation of this condition to the set of evaluated conditions.
            evaluateResults.add(evaluateResult);
        }
        // 3
        // If there are no defined conditions for the rule, the rule does not apply.
        if (evaluateResults.isEmpty()) {
            return new UtilityProcessResult().setResult(null);
        }
        // 4
        // 'And' or 'Or' the set of evaluated conditions, based on the sequencing rule definition.
        Boolean result;
        if (utilityProcessRequest.getSequencingRuleDescription().getConditionCombination().getValue().equals("All")) {
            result = BooleanUtils.and(evaluateResults.toArray(new Boolean[0]));
        } else {
            result = BooleanUtils.or(evaluateResults.toArray(new Boolean[0]));
        }
        return new UtilityProcessResult().setResult(result);
    }

    /**
     * Terminate Descendent Attempts Process [UP.3]
     *
     * For an activity.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   End Attempt Process UP.4
     *
     * @see UtilityProcess#processEndAttempt(UtilityProcessRequest) UP.4
     */
    public static void processTerminateDescendentAttempts(UtilityProcessRequest utilityProcessRequest) {
        Activity identifiedActivity = utilityProcessRequest.getTargetActivity();
        ActivityTree activityTree = utilityProcessRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        Activity commonAncestor = activityTree.findCommonAncestorFor(identifiedActivity, currentActivity);
        // 2
        // The current activity must have already been exited.
        // From the current activity to the common ancestor, exclusive of the current activity and the common ancestor
        List<Activity> activityPath = new LinkedList<>();
        if (commonAncestor != null) {
            Activity tmp = currentActivity.getParentActivity();
            while (tmp != null && !tmp.equals(commonAncestor)) {
                activityPath.add(tmp);
                tmp = tmp.getParentActivity();
            }
        }
        // 3
        // There are some activities that need to be terminated.
        if (!activityPath.isEmpty()) {
            // 3.1
            for (Activity activity : activityPath) {
                // 3.1.1
                // End the current attempt on each activity.
                processEndAttempt(new UtilityProcessRequest(activityTree, activity));
            }
        }
    }

    /**
     * End Attempt Process [UP.4]
     *
     * For an activity.
     *
     * Reference:
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Attempt Completion Status TM.1.2.2
     *   Attempt Progress Status TM.1.2.2
     *   Completion Set By Content SM.11
     *   Objective Contributes to Rollup SM.6
     *   Objective Progress Status TM.1.1
     *   Objective Satisfied Status TM.1.1
     *   Objective Set By Content SM.11
     *   Tracked SM.11
     *   Overall Rollup Process RB.1.5
     *
     * @see RollupBehavior#overallRollup(RollupRequest) RB.1.5
     */
    public static void processEndAttempt(UtilityProcessRequest utilityProcessRequest) {
        Activity targetActivity = utilityProcessRequest.getTargetActivity();
        DeliveryControls deliveryControls = targetActivity.getSequencingDefinition().getDeliveryControls();
        // 1
        if (targetActivity.isLeaf()) {
            // TODO 1.1
            if (deliveryControls.isTracked()) {
//                SCORM.getInstance().mapRuntimeDataToTrackingInfo(targetActivity);
                // 1.1.1
                // The sequencer will not affect the state of suspended activities.
                if (!targetActivity.getActivityStateInformation().isActivityIsSuspended()) {
                    // 1.1.1.1
                    // Should the sequencer set the completion status of the activity?
                    if (!deliveryControls.isCompletionSetByContent()) {
                        // 1.1.1.1.1
                        // Did the content inform the sequencer of the activity's completion status?
                        if (!targetActivity.getAttemptProgressInformation().isAttemptProgressStatus()) {
                            // 1.1.1.1.1.1
                            targetActivity.getAttemptProgressInformation().setAttemptProgressStatus(true);
                            // 1.1.1.1.1.2
                            targetActivity.getAttemptProgressInformation().setAttemptCompletionStatus(true);
                        }
                    }
                    // 1.1.1.2
                    // Should the sequencer set the objective status of the activity?
                    if (!deliveryControls.isObjectiveSetByContent()) {
                        // 1.1.1.2.1
                        for (ObjectiveDescription objectiveDescription : targetActivity.getSequencingDefinition().getObjectiveDescriptions()) {
                            // 1.1.1.2.1.1
                            if (objectiveDescription.isObjectiveContributesToRollup()) {
                                // 1.1.1.2.1.1.1
                                // Did the content inform the sequencer of the activity's rolled-up objective status?
                                if (!objectiveDescription.getObjectiveProgressInformation().isObjectiveProgressStatus()) {
                                    // 1.1.1.2.1.1.1.1
                                    objectiveDescription.getObjectiveProgressInformation().setObjectiveProgressStatus(true);
                                    // 1.1.1.2.1.1.1.2
                                    objectiveDescription.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(true);
                                }
                            }
                        }
                    }
                }
            }
            // TODO SN P115
        } else { // 2 The activity has children.
            // 2.1
            // The suspended status of the parent is dependent on the suspended status of its children.
            boolean flag = false;
            for (Activity child : targetActivity.getChildren()) {
                if (child.getActivityStateInformation().isActivityIsSuspended()) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                // 2.1.1
                targetActivity.getActivityStateInformation().setActivityIsSuspended(true);
            } else { // 2.2
                // 2.2.1
                targetActivity.getActivityStateInformation().setActivityIsSuspended(false);
            }
        }
        // 3
        // The current attempt on this activity has ended.
        targetActivity.getActivityStateInformation().setActivityIsActive(false);
        // 4
        // Ensure that any status change to this activity is propagated through the entire activity tree.
        RollupBehavior.overallRollup(
                new RollupRequest(utilityProcessRequest.getTargetActivityTree(), targetActivity));
    }

    /**
     * SN-3-17
     */
    private static Boolean evaluateRuleCondition(RuleCondition ruleCondition, Activity targetActivity) {
        ObjectiveDescription objectiveDescription = targetActivity
                .findAssociatedObjectiveByID(ruleCondition.getReferencedObjective());
        if (objectiveDescription == null) {
            return null;
        }
        ObjectiveProgressInformation objectiveProgressInformation = objectiveDescription
                .getObjectiveProgressInformation();
        AttemptProgressInformation attemptProgressInformation = targetActivity.getAttemptProgressInformation();
        ActivityProgressInformation activityProgressInformation = targetActivity.getActivityProgressInformation();
        LimitConditions limitConditions = targetActivity.getSequencingDefinition().getLimitConditions();
        switch (ruleCondition.getRuleCondition().getValue()) {
            case "Always":
                return true;
            case "Satisfied":
                return objectiveProgressInformation.isObjectiveProgressStatus() ?
                        objectiveProgressInformation.isObjectiveSatisfiedStatus() : null;
            case "Objective Status Known":
                return objectiveProgressInformation.isObjectiveProgressStatus();
            case "Objective Measure Known":
                return objectiveProgressInformation.isObjectiveMeasureStatus();
            case "Objective Measure Greater Than":
                return objectiveProgressInformation.isObjectiveMeasureStatus() ?
                        objectiveProgressInformation.getObjectiveNormalizedMeasure()
                                .compareTo(ruleCondition.getMeasureThreshold()) > 0 : null;
            case "Objective Measure Less Than":
                return objectiveProgressInformation.isObjectiveMeasureStatus() ?
                        objectiveProgressInformation.getObjectiveNormalizedMeasure()
                                .compareTo(ruleCondition.getMeasureThreshold()) < 0 : null;
            case "Completed":
                return objectiveProgressInformation.isObjectiveCompletionProgressStatus() ?
                        objectiveProgressInformation.isObjectiveCompletionStatus() : null;
//                return attemptProgressInformation.isAttemptProgressStatus() ?
//                        attemptProgressInformation.isAttemptCompletionStatus() : null;
            case "Activity Progress Known":
                return objectiveProgressInformation.isObjectiveCompletionProgressStatus();
//                return attemptProgressInformation.isAttemptProgressStatus();
            case "Attempted":
                return activityProgressInformation.getActivityAttemptCount().getValue() > 0;
            case "Attempt Limit Exceeded":
                return activityProgressInformation.isActivityProgressStatus() && limitConditions.isAttemptControl()
                        && activityProgressInformation.getActivityAttemptCount().getValue() >= limitConditions.getAttemptLimit().getValue();
        }
        return null;
    }

    /**
     * Check Activity Process [UP.5]
     *
     * For an activity; returns True if the activity is disabled or violates any of its limit conditions.
     *
     * Reference:
     *   Disabled Rules SM.2
     *   Limit Conditions Check Process UP.1
     *   Sequencing Rules Check Process UP.2
     *
     * @see UtilityProcess#processLimitConditionsCheck(UtilityProcessRequest) UP.1
     * @see UtilityProcess#processSequencingRulesCheck(UtilityProcessRequest) UP.2
     */
    public static UtilityProcessResult processCheckActivity(UtilityProcessRequest utilityProcessRequest) {
        Activity targetActivity = utilityProcessRequest.getTargetActivity();
        // 1
        // Make sure the activity is not disabled.
        UtilityProcessResult utilityProcessResult = processSequencingRulesCheck(
                new UtilityProcessRequest(utilityProcessRequest.getTargetActivityTree(), targetActivity)
                        .setConditionType(ConditionType.PRECONDITION).setRuleActions("Disabled"));
        // 2
        if (utilityProcessResult.getAction() != null) {
            // 2.1
            return new UtilityProcessResult().setResult(true);
        }
        // 3
        // Make sure the activity does not violate any limit condition.
        utilityProcessResult = processLimitConditionsCheck(
                new UtilityProcessRequest(utilityProcessRequest.getTargetActivityTree(), targetActivity));
        // 4
        if (utilityProcessResult.isLimitConditionViolated()) {
            // 4.1
            return new UtilityProcessResult().setResult(true);
        }
        // 5
        // Activity is allowed.
        return new UtilityProcessResult().setResult(false);
    }

}
