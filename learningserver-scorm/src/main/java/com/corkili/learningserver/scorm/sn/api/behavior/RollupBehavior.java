package com.corkili.learningserver.scorm.sn.api.behavior;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.api.behavior.result.RollupBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.UtilityProcessResult;
import com.corkili.learningserver.scorm.sn.api.request.RollupRequest;
import com.corkili.learningserver.scorm.sn.api.request.UtilityProcessRequest;
import com.corkili.learningserver.scorm.sn.api.util.BooleanUtils;
import com.corkili.learningserver.scorm.sn.model.definition.LimitConditions;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.RollupCondition;
import com.corkili.learningserver.scorm.sn.model.definition.RollupRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ObjectiveProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public class RollupBehavior {

    private static final RollupRuleDescription DEFAULT_ROLLUP_RULE_SATISFIED;
    private static final RollupRuleDescription DEFAULT_ROLLUP_RULE_NOT_SATISFIED;
    private static final RollupRuleDescription DEFAULT_ROLLUP_RULE_COMPLETED;
    private static final RollupRuleDescription DEFAULT_ROLLUP_RULE_INCOMPLETE;

    static {
        DEFAULT_ROLLUP_RULE_SATISFIED = new RollupRuleDescription();
        DEFAULT_ROLLUP_RULE_SATISFIED.getChildActivitySet().setValue("All");
        RollupCondition rollupCondition = new RollupCondition();
        rollupCondition.getRollupCondition().setValue("Satisfied");
        DEFAULT_ROLLUP_RULE_SATISFIED.getRollupConditions().add(rollupCondition);
        DEFAULT_ROLLUP_RULE_SATISFIED.getRollupAction().setValue("Satisfied");

        DEFAULT_ROLLUP_RULE_NOT_SATISFIED = new RollupRuleDescription();
        DEFAULT_ROLLUP_RULE_NOT_SATISFIED.getChildActivitySet().setValue("All");
        rollupCondition = new RollupCondition();
        rollupCondition.getRollupCondition().setValue("Objective Status Known");
        DEFAULT_ROLLUP_RULE_NOT_SATISFIED.getRollupConditions().add(rollupCondition);
        DEFAULT_ROLLUP_RULE_NOT_SATISFIED.getRollupAction().setValue("Not Satisfied");

        DEFAULT_ROLLUP_RULE_COMPLETED = new RollupRuleDescription();
        DEFAULT_ROLLUP_RULE_COMPLETED.getChildActivitySet().setValue("All");
        rollupCondition = new RollupCondition();
        rollupCondition.getRollupCondition().setValue("Completed");
        DEFAULT_ROLLUP_RULE_COMPLETED.getRollupConditions().add(rollupCondition);
        DEFAULT_ROLLUP_RULE_COMPLETED.getRollupAction().setValue("Completed");

        DEFAULT_ROLLUP_RULE_INCOMPLETE = new RollupRuleDescription();
        DEFAULT_ROLLUP_RULE_INCOMPLETE.getChildActivitySet().setValue("All");
        rollupCondition = new RollupCondition();
        rollupCondition.getRollupCondition().setValue("Activity Progress Known");
        DEFAULT_ROLLUP_RULE_INCOMPLETE.getRollupConditions().add(rollupCondition);
        DEFAULT_ROLLUP_RULE_INCOMPLETE.getRollupAction().setValue("Incomplete");
    }


    /**
     * Measure Rollup Process [RB.1.1a]
     *
     * For an activity; may change the Objective Information for the activity.
     *
     * Reference:
     *   Objective Contributes To Rollup SM.6
     *   Objective Description SM.6
     *   Objective Measure Status TM.1.1
     *   Objective Normalized Measure TM.1.1
     *   Rollup Objective Measure Weight SM.8
     *   Tracked SM.11
     */
    public static void processMeasureRollup(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1
        BigDecimal totalWeightedMeasure = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
        // 2
        boolean validData = false;

        // 3
        BigDecimal countedMeasures = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
        // 4
        ObjectiveDescription targetObjective = null;
        // 5
        for (ObjectiveDescription objective : targetActivity.getSequencingDefinition().getObjectiveDescriptions()) {
            // 5.1
            // Find the target objective for the rolled-up measure
            if (objective.isObjectiveContributesToRollup()) {
                // 5.1.1
                targetObjective = objective;
                // 5.1.2
                break;
            }
        }
        // 6
        if (targetObjective != null) {
            // 6.1
            for (Activity child : targetActivity.getChildren()) {
                // 6.1.1
                // Only include tracked children.
                if (child.getSequencingDefinition().getDeliveryControls().isTracked()) {
                    // 6.1.1.1
                    ObjectiveDescription rolledUpObjective = null;
                    // 6.1.1.2
                    for (ObjectiveDescription objective : child.getSequencingDefinition().getObjectiveDescriptions()) {
                        // 6.1.1.2.1
                        if (objective.isObjectiveContributesToRollup()) {
                            // 6.1.1.2.1.1
                            rolledUpObjective = objective;
                            break;
                        }
                    }
                    // 6.1.1.3
                    if (rolledUpObjective != null) {
                        // 6.1.1.3.1
                        countedMeasures = countedMeasures.add(child.getSequencingDefinition()
                                .getRollupControls().getRollupObjectiveMeasureWeight().getValue());
                        // 6.1.1.3.2
                        if (rolledUpObjective.getObjectiveProgressInformation().isObjectiveMeasureStatus()) {
                            // 6.1.1.3.2.1
                            totalWeightedMeasure = totalWeightedMeasure.add(
                                    rolledUpObjective.getObjectiveProgressInformation()
                                            .getObjectiveNormalizedMeasure().getValue().multiply(
                                                    child.getSequencingDefinition().getRollupControls()
                                                            .getRollupObjectiveMeasureWeight().getValue()));
                            // 6.1.1.3.2.2
                            validData = true;
                        }
                    } else { // 6.1.1.4
                        // 6.1.1.4.1
                        // One of the children dose not include a rolled-up objective.
                        return;
                    }
                }
            }
            // 6.2
            if (!validData) {
                // 6.2.1
                // No tracking state rolled-up, cannot determine the rolled-up measure.
                targetObjective.getObjectiveProgressInformation().setObjectiveMeasureStatus(false);
            } else { // 6.3
                // 6.3.1
                // Set the rolled-up measure for the target objective.
                if (countedMeasures.compareTo(BigDecimal.ZERO) >  0) {
                    // 6.3.1.1
                    targetObjective.getObjectiveProgressInformation().setObjectiveMeasureStatus(true);
                    // 6.3.1.2
                    // total / counted
                    targetObjective.getObjectiveProgressInformation().setObjectiveNormalizedMeasure(
                            totalWeightedMeasure.divide(countedMeasures, BigDecimal.ROUND_HALF_UP).doubleValue());
                } else { // 6.3.2
                    // 6.3.2.1
                    // No children contributed weight.
                    targetObjective.getObjectiveProgressInformation().setObjectiveMeasureStatus(false);
                }
            }
        }
        // 7
        // No objective contributes to rollup, so we cannot set anything.
    }

    /**
     * Completion Measure Rollup Process [RB.1.1b]
     *
     * For an activity; may change the Objective Information for the activity.
     *
     * Reference:
     *   Attempt Completion Amount Status TM.1.1
     *   Attempt Completion Amount TM.1.1
     *   Tracked SM.11
     *   adlcp:minProgressMeasure
     *   adlcp:progressWeight
     */
    public static void processCompletionMeasureRollup(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1
        BigDecimal totalWeightedMeasure = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
        // 2
        boolean validData = false;
        // 3
        BigDecimal countedMeasure = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
        // 4
        for (Activity child : targetActivity.getChildren()) {
            // 4.1
            // Only include tracked children.
            if (child.getSequencingDefinition().getDeliveryControls().isTracked()) {
                // 4.1.1
                // The child is included, account for it in weighted average
                countedMeasure = countedMeasure.add(child.getSequencingDefinition().getCompletionThreshold()
                        .getProgressWeight().getValue());
                // 4.1.2
                if (child.getAttemptProgressInformation().isAttemptCompletionAmountStatus()) {
                    // 4.1.2.1
                    // Only include progress that has been reported or previously rolled-up.
                    totalWeightedMeasure = totalWeightedMeasure.add(child.getAttemptProgressInformation()
                            .getAttemptCompletionAmount().getValue().multiply(child.getSequencingDefinition()
                                    .getCompletionThreshold().getProgressWeight().getValue()));
                    // 4.1.2.2
                    validData = true;
                }
            }
        }
        // 5
        if (!validData) {
            // 5.1
            // No progress state rolled-up, cannot determine the rolled-up progress.
            targetActivity.getAttemptProgressInformation().setAttemptCompletionAmountStatus(false);
        } else { // 5.2
            // 5.2.1
            // Set the rolled-up progress.
            if (countedMeasure.compareTo(BigDecimal.ZERO) > 0) {
                // 5.2.1.1
                targetActivity.getAttemptProgressInformation().setAttemptCompletionAmountStatus(true);
                // 5.2.1.2
                targetActivity.getAttemptProgressInformation().getAttemptCompletionAmount().setValue(
                        totalWeightedMeasure.divide(countedMeasure, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else { // 5.2.2
                // 5.2.2.1
                // No children contributed weight.
                targetActivity.getAttemptProgressInformation().setAttemptCompletionAmountStatus(false);
            }
        }
    }

    /**
     * Objective Rollup Using Measure Process [RB.1.2a]
     *
     * For an activity; may change the Objective Information for the activity.
     *
     * Reference:
     *   Objective Contributes to Rollup SM.6
     *   Objective Description SM.6
     *   Objective Satisfied by Measure SM.6
     *   Objective Measure Status TM.1.1
     *   Objective Normalized Measure TM.1.1
     *   Objective Progress Status TM.1.1
     *   Objective Satisfied Status TM.1.1
     *   Activity is Active AM.1.1
     *   adlseq:measureSatisfactionIfActive SCORM SN
     */
    public static void processObjectiveRollupUsingMeasureProcess(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1
        ObjectiveDescription targetObjective = null;
        // 2
        for (ObjectiveDescription objective : targetActivity.getSequencingDefinition().getObjectiveDescriptions()) {
            // 2.1
            // Identify the objective that may be altered based on the activity's children's rolled-up measure.
            if (objective.isObjectiveContributesToRollup()) {
                // 2.1.1
                targetObjective = objective;
                // 2.1.2
                break;
            }
        }
        // 3
        if (targetObjective != null) {
            // 3.1
            // If the objective is satisfied by measure, test the rolled-up measure against the defined threshold.
            if (targetObjective.isObjectiveSatisfiedByMeasure()) {
                // 3.1.1
                // No Measure known, so objective status is unreliable.
                if (!targetObjective.getObjectiveProgressInformation().isObjectiveMeasureStatus()) {
                    // 3.1.1.1
                    targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(false);
                } else { // 3.1.2
                    // 3.1.2.1
                    if (!targetActivity.getActivityStateInformation().isActivityIsActive()
                            || (targetActivity.getActivityStateInformation().isActivityIsActive()
                            && targetActivity.getSequencingDefinition().getRollupConsiderationControls()
                            .isMeasureSatisfactionIfActive())) {
                        // 3.1.2.1.1
                        if (targetObjective.getObjectiveProgressInformation().getObjectiveNormalizedMeasure().getValue()
                                .compareTo(targetObjective.getObjectiveMinimumSatisfiedNormalizedMeasure().getValue()) >= 0) {
                            // 3.1.2.1.1.1
                            targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(true);
                            // 3.1.2.1.1.2
                            targetObjective.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(true);
                        } else { // 3.1.2.1.2
                            // 3.1.2.1.2.1
                            targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(true);
                            // 3.1.2.1.2.2
                            targetObjective.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(false);
                        }
                    } else { // 3.1.2.2
                        // 3.1.2.2.1
                        // Incomplete information, do not evaluate objective status.
                        targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(false);
                    }
                }
            }
            // 3.2
            // exit
        }
        // 4
        // 4.1
        // No objective contributes to rollup, so we cannot set anything.
        // exit
    }

    /**
     * Objective Rollup Using Rules Process [RB.1.2b]
     *
     * For an activity; may change the Objective Information for the activity.
     *
     * Reference:
     *   Objective Contributes to Rollup SM.6
     *   Objective Description SM.6
     *   Objective Progress Status TM.1.1
     *   Objective Satisfied Status TM.1.1
     *   Rollup Rule Check Subprocess RB.1.4
     *   Rollup Action SM.5
     *   Rollup Condition SM.5
     *   Rollup Action SM.5
     *   Rollup Child Activity Set SM.5
     *
     * @see RollupBehavior#checkRollupRule(RollupRequest) RB.1.4
     */
    public static void processObjectiveRollupUsingRules(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1
        // If no objective rollup rules are defined, use the default rollup rules.
        boolean useDefault = true;
        for (RollupRuleDescription rollupRule : targetActivity.getSequencingDefinition().getRollupRuleDescriptions()) {
            if ("Satisfied".equals(rollupRule.getRollupAction().getValue())
                    || "Not Satisfied".equals(rollupRule.getRollupAction().getValue())) {
                useDefault = false;
                break;
            }
        }
        // 2
        ObjectiveDescription targetObjective = null;
        // 3
        for (ObjectiveDescription objective : targetActivity.getSequencingDefinition().getObjectiveDescriptions()) {
            // 3.1
            // Identify the objective that may be altered based on the activity's children's rolled-up status.
            if (objective.isObjectiveContributesToRollup()) {
                // 3.1.1
                targetObjective = objective;
                // 3.1.2
                break;
            }
        }
        // 4
        if (targetObjective != null) {
            // 4.1
            // Process all Not satisfied rules first.
            RollupBehaviorResult notSatisfiedResult = checkRollupRule(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                            .setRollupAction("Not Satisfied")
                            .setUseDefaultRollupRule(useDefault)
                            .setDefaultRollupRule(DEFAULT_ROLLUP_RULE_NOT_SATISFIED));
            // 4.2
            if (notSatisfiedResult.getEvaluation()) {
                // 4.2.1
                targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(true);
                // 4.2.2
                targetObjective.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(false);
            }
            // 4.3
            // Process all Satisfied
            RollupBehaviorResult satisfiedResult = checkRollupRule(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                            .setRollupAction("Satisfied")
                            .setUseDefaultRollupRule(useDefault)
                            .setDefaultRollupRule(DEFAULT_ROLLUP_RULE_SATISFIED));
            // 4.4
            if (satisfiedResult.getEvaluation()) {
                // 4.4.1
                targetObjective.getObjectiveProgressInformation().setObjectiveProgressStatus(true);
                targetObjective.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(true);
            }
            // 4.5
            // exit
        }
        // 5 & 5.1
        // exit
        // No objective contributes to rollup, so we cannot set anything.
    }

    /**
     * Activity Progress Rollup Using Measure Process [RB.1.3a]
     *
     * For an activity; may change the Attempt Information for the activity.
     *
     * Reference:
     *   Attempt Completion Status TM.1.2.2
     *   Attempt Progress Status TM.1.2.2
     *   Attempt Completion Amount Status TM.1.1
     *   Attempt Completion Amount TM.1.1
     *   adlcp:completedByMeasure SCORM CAN
     *   adlcp:minProgressMeasure SCORM CAM
     */
    public static void processActivityProgressRollupUsingMeasure(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        AttemptProgressInformation attemptProgressInformation = targetActivity.getAttemptProgressInformation();
        // 1
        attemptProgressInformation.setAttemptProgressStatus(false);
        // 2
        attemptProgressInformation.setAttemptCompletionStatus(false);
        // 3
        // If the completion is determined by measure, test the rolled-up progress against the defined threshold.
        if (targetActivity.getSequencingDefinition().getCompletionThreshold().isCompletedByMeasure()) {
            // 3.1
            // No progress amount known, so the status is unreliable.
            if (!attemptProgressInformation.isAttemptCompletionAmountStatus()) {
                // 3.1.1
                attemptProgressInformation.setAttemptCompletionStatus(false);
            } else { // 3.2
                // 3.2.1
                if (attemptProgressInformation.getAttemptCompletionAmount().getValue().compareTo(targetActivity
                        .getSequencingDefinition().getCompletionThreshold().getMinimumProgressMeasure().getValue()) >= 0) {
                    // 3.2.1.1
                    attemptProgressInformation.setAttemptProgressStatus(true);
                    attemptProgressInformation.setAttemptCompletionStatus(true);
                } else {
                    attemptProgressInformation.setAttemptProgressStatus(true);
                    attemptProgressInformation.setAttemptCompletionStatus(false);
                }
            }
        } else { // 4
            // 4.1
            // Incomplete information, do not evaluate completion status
            attemptProgressInformation.setAttemptProgressStatus(false);
        }
        // 5
        //exit
    }

    /**
     * Activity Progress Rollup Using Rules [RB.1.3b]
     *
     * For an activity; may change the attempt information for the activity.
     *
     * Reference:
     *   Attempt Completion Status TM.1.2.2
     *   Attempt Progress Status TM.1.2.2
     *   Rollup Rule Check Subprocess RB.1.4
     *   Rollup Action SM.5
     *   Rollup Condition SM.5
     *   Rollup Action SM.5
     *   Rollup Child Activity Set SM.5
     *
     * @see RollupBehavior#checkRollupRule(RollupRequest) RB.1.4
     */
    public static void processActivityProgressRollupUsingRules(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        AttemptProgressInformation attemptProgressInformation = targetActivity.getAttemptProgressInformation();
        // 1 & 1.1 & 1.2
        // If no objective rollup rules are defined, use the default rollup rules.
        boolean useDefault = true;
        for (RollupRuleDescription rollupRule : targetActivity.getSequencingDefinition().getRollupRuleDescriptions()) {
            if ("Completed".equals(rollupRule.getRollupAction().getValue())
                    || "Incomplete".equals(rollupRule.getRollupAction().getValue())) {
                useDefault = false;
                break;
            }
        }
        // 2
        // Process all Incomplete rules first.
        RollupBehaviorResult IncompleteResult = checkRollupRule(
                new RollupRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                        .setRollupAction("Incomplete")
                        .setUseDefaultRollupRule(useDefault)
                        .setDefaultRollupRule(DEFAULT_ROLLUP_RULE_INCOMPLETE));
        // 3
        if (IncompleteResult.getEvaluation()) {
            // 3.1
            attemptProgressInformation.setAttemptProgressStatus(true);
            // 3.2
            attemptProgressInformation.setAttemptCompletionStatus(false);
        }
        // 4
        RollupBehaviorResult completedResult = checkRollupRule(
                new RollupRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                        .setRollupAction("Completed")
                        .setUseDefaultRollupRule(useDefault)
                        .setDefaultRollupRule(DEFAULT_ROLLUP_RULE_COMPLETED));
        // 5
        if (completedResult.getEvaluation()) {
            // 5.1
            attemptProgressInformation.setAttemptProgressStatus(true);
            // 5.2
            attemptProgressInformation.setAttemptCompletionStatus(true);
        }
        // 6
    }

    /**
     * Rollup Rule Check Subprocess [RB.1.4]
     *
     * For an activity and a Rollup Action; returns True if the action applies.
     *
     * Reference:
     *   Check Child for Rollup Subprocess RB.1.4.2
     *   Evaluate Rollup Conditions Subprocess RB.1.4.1
     *   Rollup Action SM.5
     *   Rollup Child Activity Set SM.5
     *   Rollup Minimum Count SM.5
     *   Rollup Minimum Percent SM.5
     *   Rollup Rule Description SM.5
     *   Tracked SM.11
     *   Tracking Model TM
     *
     * @see RollupBehavior#checkChildForRollup(RollupRequest) RB.1.4.2
     * @see RollupBehavior#evaluateRollupConditions(RollupRequest) RB.1.4.1
     */
    public static RollupBehaviorResult checkRollupRule(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1 & 1.1
        // Make sure the activity rules to evaluate
        List<RollupRuleDescription> ruleList = new LinkedList<>();
        if (!rollupRequest.isUseDefaultRollupRule()) {
            for (RollupRuleDescription rollupRule : targetActivity.getSequencingDefinition().getRollupRuleDescriptions()) {
                if (rollupRule.getRollupAction().getValue().equals(rollupRequest.getRollupAction())) {
                    ruleList.add(rollupRule);
                }
            }
        } else {
            ruleList.add(rollupRequest.getDefaultRollupRule());
        }
        if (!ruleList.isEmpty()) {
            // 1.2
            for (RollupRuleDescription rule : ruleList) {
                // 1.2.1
                List<Boolean> contributingChildrenBag = new ArrayList<>();
                // 1.2.2
                for (Activity child : targetActivity.getChildren()) {
                    // 1.2.2.1
                    if (child.getSequencingDefinition().getDeliveryControls().isTracked()) {
                        // 1.2.2.1.1
                        // Make sure this child contributes to the status of its parent.
                        RollupBehaviorResult rollupBehaviorResult = checkChildForRollup(
                                new RollupRequest(rollupRequest.getTargetActivityTree(), child)
                                        .setRollupAction(rule.getRollupAction().getValue()));
                        // 1.2.2.1.2
                        if (rollupBehaviorResult.isChildIsIncludedInRollup()) {
                            // 1.2.2.1.2.1
                            // Evaluate the rollup conditions on the child activity.
                            RollupBehaviorResult evaluateResult = evaluateRollupConditions(
                                    new RollupRequest(rollupRequest.getTargetActivityTree(), child)
                                            .setRollupRule(rule));
                            // 1.2.2.1.2.2
                            // Account for a possible "unknown" condition evaluation.
                            // 1.2.2.1.2.2.1
                            // 1.2.2.1.2.3
                            // 1.2.2.1.2.3.1
                            // 1.2.2.1.2.3.1.1
                            // 1.2.2.1.2.3.2
                            // 1.2.2.1.2.3.2.1
                            contributingChildrenBag.add(evaluateResult.getEvaluation());
                        }
                    }
                }
                // 1.2.3
                // Determine if the appropriate children contributed to rollup; if they did, the status of the
                // activity should be changed.
                boolean statusChange = false;
                // 1.2.4
                // noinspection StatementWithEmptyBody
                if (contributingChildrenBag.isEmpty()) {
                    // 1.2.4.1
                    // No action;
                    // do not change status unless some child contributed to rollup
                } else if (rule.getChildActivitySet().getValue().equals("All")) { // 1.2.5
                    // 1.2.5.1
                    if (!contributingChildrenBag.contains(false) && !contributingChildrenBag.contains(null)) {
                        // 1.2.5.1.1
                        statusChange = true;
                    }
                } else if (rule.getChildActivitySet().getValue().equals("Any")) { // 1.2.6
                    // 1.2.6.1
                    if (contributingChildrenBag.contains(true)) {
                        // 1.2.6.1.1
                        statusChange = true;
                    }
                } else if (rule.getChildActivitySet().getValue().equals("None")) { // 1.2.7
                    // 1.2.7.1
                    if (!contributingChildrenBag.contains(true) && !contributingChildrenBag.contains(null)) {
                        // 1.2.7.1.1
                        statusChange = true;
                    }
                } else if (rule.getChildActivitySet().getValue().equals("At Least Count")) { // 1.2.8
                    // 1.2.8.1
                    if (contributingChildrenBag.stream().filter(c -> c != null && c).count()
                            >= rule.getRollupMinimumCount().getValue()) {
                        // 1.2.8.1.1
                        statusChange = true;
                    }
                } else if (rule.getChildActivitySet().getValue().equals("At Least Percent")) { // 1.2.9
                    // 1.2.9.1
                    if (BigDecimal.valueOf(contributingChildrenBag.stream().filter(c -> c != null && c).count()
                            / (long) contributingChildrenBag.size()).setScale(4, BigDecimal.ROUND_HALF_UP)
                            .compareTo(rule.getRollupMinimumPercent().getValue()) >= 0) {
                        // 1.2.9.1.1
                        statusChange = true;
                    }
                }
                // 1.2.10
                if (statusChange) {
                    // 1.2.10.1
                    // Stop at the first rule that evaluates to true - perform the associated action.
                    return new RollupBehaviorResult().setEvaluation(true);
                }
            }
        }
        // 2
        // No rules evaluated to true - do not perform any action.
        return new RollupBehaviorResult().setEvaluation(false);
    }

    /**
     * Evaluate Rollup Conditions Subprocess [RB.1.4.1]
     *
     * For an activity, a Condition Combination, and a set of Rollup Conditions; returns True if the condition(s)
     * evaluate to True, False if the condition(s) evaluate to False, and Unknown if the condition(s) cannot be evaluated.
     *
     * Reference:
     *   Condition Combination SM.5
     *   Rollup Condition SM.5
     *   Rollup Condition Operator SM.5
     *   Tracking Model TM
     */
    public static RollupBehaviorResult evaluateRollupConditions(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        RollupRuleDescription rollupRule = rollupRequest.getRollupRule();
        // 1
        // This is used to keep track of the evaluation of the rule's conditions.
        List<Boolean> rollupConditionBag = new ArrayList<>();
        // 2
        for (RollupCondition rollupCondition : rollupRule.getRollupConditions()) {
            // 2.1
            // Evaluate each condition against the activity's tracking information.
            // This evaluation may result in "unknown".
            Boolean evaluateResult = evaluateRuleCondition(rollupCondition, targetActivity);
            // 2.2
            // Negating "unknown" results in "unknown"
            if (rollupCondition.getOperator().getValue().equals("Not")) {
                // 2.2.1
                evaluateResult = BooleanUtils.negate(evaluateResult);
            }
            // 2.3
            // Add the evaluation of this condition to the set of evaluated conditions.
            rollupConditionBag.add(evaluateResult);
        }
        // 3
        // If there are no defined conditions for the rule, we cannot determine if the rule applies.
        if (rollupConditionBag.isEmpty()) {
            // 3.1
            return new RollupBehaviorResult().setEvaluation(null);
        }
        // 4
        // 'And' or 'Or' the set of evaluated conditions, based on the rollup rule definition
        Boolean evaluation;
        if ("All".equals(rollupRule.getConditionCombination().getValue())) {
            evaluation = BooleanUtils.and(rollupConditionBag.toArray(new Boolean[0]));
        } else {
            evaluation = BooleanUtils.or(rollupConditionBag.toArray(new Boolean[0]));
        }
        return new RollupBehaviorResult().setEvaluation(evaluation);
    }

    private static Boolean evaluateRuleCondition(RollupCondition rollupCondition, Activity targetActivity) {
        ObjectiveDescription objectiveDescription = targetActivity.findAssociatedObjectiveForContributingToRollup();
        if (objectiveDescription == null) {
            return null;
        }
        ObjectiveProgressInformation objectiveProgressInformation = objectiveDescription
                .getObjectiveProgressInformation();
        AttemptProgressInformation attemptProgressInformation = targetActivity.getAttemptProgressInformation();
        ActivityProgressInformation activityProgressInformation = targetActivity.getActivityProgressInformation();
        LimitConditions limitConditions = targetActivity.getSequencingDefinition().getLimitConditions();
        switch (rollupCondition.getRollupCondition().getValue()) {
            case "Never":
                return false;
            case "Satisfied":
                return objectiveProgressInformation.isObjectiveProgressStatus() ?
                        objectiveProgressInformation.isObjectiveSatisfiedStatus() : null;
            case "Objective Status Known":
                return objectiveProgressInformation.isObjectiveProgressStatus();
            case "Objective Measure Known":
                return objectiveProgressInformation.isObjectiveMeasureStatus();
            case "Completed":
                return objectiveProgressInformation.isObjectiveCompletionProgressStatus()  ?
                        objectiveProgressInformation.isObjectiveCompletionStatus() : null;
//                return attemptProgressInformation.isAttemptProgressStatus()
//                        && attemptProgressInformation.isAttemptCompletionStatus();
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
     * Check Child for Rollup Subprocess [RB.1.4.2]
     *
     * For an activity and a Rollup Action; returns True if the activity is included in rollup.
     *
     * Reference:
     *   Rollup Action SM.5
     *   Rollup Objective Satisfied SM.8
     *   RollupProgress Completion SM.8
     *   Activity Attempt Count TM.1.2.1
     *   Sequencing Rules Check Process UP.2
     *   adlseq:requiredForSatisfied SCORM SN
     *   adlseq:requiredForNotSatisfied SCORM SN
     *   adlseq:requiredForCompleted SCORM SN
     *   adlseq:requiredForIncomplete SCORM SN
     *
     * @see UtilityProcess#processSequencingRulesCheck(UtilityProcessRequest) UP.2
     */
    public static RollupBehaviorResult checkChildForRollup(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        String rollupAction = rollupRequest.getRollupAction();
        // 1
        boolean included = false;
        // 2
        if ("Satisfied".equals(rollupAction) || "Not Satisfied".equals(rollupAction)) {
            // 2.1
            // Test the objective rollup control.
            if (targetActivity.getSequencingDefinition().getRollupControls().isRollupObjectiveSatisfied()) {
                // 2.1.1
                // Default Behavior - aldseq:requiredFor[xxx] == always.
                included = true;
                // 2.1.2
                if (("Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                        .getRollupConsiderationControls().getRequiredForSatisfied().getValue().equals("ifNotSuspended"))
                        || ("Not Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                        .getRollupConsiderationControls().getRequiredForNotSatisfied().getValue().equals("ifNotSuspended"))) {
                    // 2.1.2.1
                    if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()
                            || (targetActivity.getActivityProgressInformation().getActivityAttemptCount().getValue() > 0
                            && targetActivity.getActivityStateInformation().isActivityIsSuspended())) {
                        // 2.1.2.1.1
                        included = false;
                    }
                } else { // 2.1.3
                    // 2.1.3.1
                    if (("Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                            .getRollupConsiderationControls().getRequiredForSatisfied().getValue().equals("ifAttempted"))
                            || ("Not Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                            .getRollupConsiderationControls().getRequiredForNotSatisfied().getValue().equals("ifAttempted"))) {
                        // 2.1.3.1.1
                        if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()
                                || targetActivity.getActivityProgressInformation().getActivityAttemptCount().getValue() == 0) {
                            // 2.1.3.1.1.1
                            included = false;
                        }
                    } else { // 2.1.3.2
                        // 2.1.3.2.1
                        if (("Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                                .getRollupConsiderationControls().getRequiredForSatisfied().getValue().equals("ifNotSkipped"))
                                || ("Not Satisfied".equals(rollupAction) && targetActivity.getSequencingDefinition()
                                .getRollupConsiderationControls().getRequiredForNotSatisfied().getValue().equals("ifNotSkipped"))) {
                            // 2.1.3.2.1.1
                            UtilityProcessResult utilityProcessResult = UtilityProcess.processSequencingRulesCheck(
                                    new UtilityProcessRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                                            .setConditionType(ConditionType.PRECONDITION).setRuleActions("Skip"));
                            // 2.1.3.2.1.2
                            if (utilityProcessResult.getAction() != null) {
                                included = false;
                            }
                        }
                    }
                }
            }
        }
        // 3
        if ("Completed".equals(rollupAction) || "Incomplete".equals(rollupAction)) {
            // 3.1
            // Test the progress rollup control.
            if (targetActivity.getSequencingDefinition().getRollupControls().isRollupProgressCompletion()) {
                // 3.1.1
                // Default Behavior - adlseq:requiredFor[xxx] == always.
                included = true;
                // 3.1.2
                if (("Completed".equals(rollupAction) && targetActivity.getSequencingDefinition()
                        .getRollupConsiderationControls().getRequiredForCompleted().getValue().equals("ifNotSuspended"))
                        || ("Incomplete".equals(rollupAction) && targetActivity.getSequencingDefinition()
                        .getRollupConsiderationControls().getRequiredForIncomplete().getValue().equals("ifNotSuspended"))) {
                    // 3.1.2.1
                    if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()
                            || (targetActivity.getActivityProgressInformation().getActivityAttemptCount().getValue() > 0
                            && targetActivity.getActivityStateInformation().isActivityIsSuspended())) {
                        // 3.1.2.1.1
                        included = false;
                    }
                } else { // 3.1.3
                    // 3.1.3.1
                    if (("Completed".equals(rollupAction) && targetActivity.getSequencingDefinition()
                            .getRollupConsiderationControls().getRequiredForCompleted().getValue().equals("ifAttempted"))
                            || ("Incomplete".equals(rollupAction) && targetActivity.getSequencingDefinition()
                            .getRollupConsiderationControls().getRequiredForIncomplete().getValue().equals("ifAttempted"))) {
                        // 3.1.3.1.1
                        if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()
                                || targetActivity.getActivityProgressInformation().getActivityAttemptCount().getValue() == 0) {
                            // 3.1.3.1.1.1
                            included = false;
                        }
                    } else { // 3.1.3.2
                        // 3.1.3.2.1
                        if (("Completed".equals(rollupAction) && targetActivity.getSequencingDefinition()
                                .getRollupConsiderationControls().getRequiredForCompleted().getValue().equals("ifNotSkipped"))
                                || ("Incomplete".equals(rollupAction) && targetActivity.getSequencingDefinition()
                                .getRollupConsiderationControls().getRequiredForIncomplete().getValue().equals("ifNotSkipped"))) {
                            // 3.1.3.2.1.1
                            UtilityProcessResult utilityProcessResult = UtilityProcess.processSequencingRulesCheck(
                                    new UtilityProcessRequest(rollupRequest.getTargetActivityTree(), targetActivity)
                                            .setConditionType(ConditionType.PRECONDITION).setRuleActions("Skip"));
                            // 3.1.3.2.1.2
                            if (utilityProcessResult.getAction() != null) {
                                included = false;
                            }
                        }
                    }
                }
            }
        }
        return new RollupBehaviorResult().setChildIsIncludedInRollup(included);
    }

    /**
     * Overall Rollup Process [RB.1.5]
     *
     * For an activity; may change the tracking information for the activity and its ancestor.
     *
     * Reference:
     *   Activity Progress Rollup Process RB.1.3
     *   Measure Rollup Process RB.1.1a
     *   Completion Measure Rollup Process RB.1.1b
     *   Objective Rollup Process RB.1.2
     *   Tracked SM.11
     *   Tracking Model TM
     *
     * @see RollupBehavior#processMeasureRollup(RollupRequest) RB.1.1a
     * @see RollupBehavior#processCompletionMeasureRollup(RollupRequest) RB.1.1b
     * @see RollupBehavior#processObjectiveRollupUsingMeasureProcess(RollupRequest) RB.1.2a
     * @see RollupBehavior#processObjectiveRollupUsingRules(RollupRequest) RB.1.2b
     * @see RollupBehavior#processActivityProgressRollupUsingMeasure(RollupRequest) RB.1.3a
     * @see RollupBehavior#processActivityProgressRollupUsingRules(RollupRequest) RB.1.3b
     */
    public static void overallRollup(RollupRequest rollupRequest) {
        Activity targetActivity = rollupRequest.getTargetActivity();
        // 1
        // From the root of the activity tree to the activity, inclusive, in reverse order
        List<Activity> activityPath = new LinkedList<>();
        Activity tmp = targetActivity;
        while (tmp != null) {
            activityPath.add(tmp);
            tmp = tmp.getParentActivity();
        }
        // 2
        if (activityPath.isEmpty()) {
            // 2.1
            // Nothing to Rollup.
            return;
        }
        // 3
        for (Activity activity : activityPath) {
            // 3.1
            // Only apply Measure Rollup to non-leaf activities.
            if (!activity.getChildren().isEmpty()) {
                // 3.1.1
                // Rollup the activity's measure
                processMeasureRollup(new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
                // 3.1.2
                // Rollup the activity's progress measure
                processCompletionMeasureRollup(new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
            }
            // 3.2
            // Apply the appropriate Objective Rollup Process to the activity
            processObjectiveRollupUsingMeasureProcess(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
            processObjectiveRollupUsingRules(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
            // 3.3
            // Apply the appropriate Activity Progress Rollup Process to the activity
            processActivityProgressRollupUsingMeasure(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
            processActivityProgressRollupUsingRules(
                    new RollupRequest(rollupRequest.getTargetActivityTree(), activity));
        }

    }

}
