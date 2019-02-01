package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.corkili.learningserver.scorm.sn.api.behavior.common.TraversalDirection;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingBehaviorResult;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingException;
import com.corkili.learningserver.scorm.sn.api.behavior.result.UnifiedProcessResult;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;
import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.UnifiedProcessRequest;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class SequencingBehavior {

    /**
     * Flow Tree Traversal Subprocess [SB.2.1]
     *
     * For an activity, a traversal direction, a consider children flag, and a previous traversal direction;
     * returns the 'next' activity in directed traversal of the activity tree, may return the traversal direction,
     * may indicate control be returned to the LTS; and may return an exception code:
     *
     * Reference:
     *   Available Children AM.1.1
     *   Sequencing Control Forward Only SM.1
     *   Terminate Descendent Attempts Process UP.3
     *
     * @see UnifiedProcess#processTerminateDescendentAttempts(UnifiedProcessRequest) UP.3\
     */
    public static SequencingBehaviorResult processFlowTreeTraversal(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection previousTraversalDirection = sequencingRequest.getPreviousTraversalDirection();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        boolean considerChildren = sequencingRequest.isConsiderChildren();
        // 1
        boolean reversedDirection = false;
        // 2
        // Test if we have skipped all of the children in a forward only cluster moving backward.
        if (previousTraversalDirection == TraversalDirection.Backward
                && targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                .lastIndexOf(targetActivity) == targetActivity.getParentActivity().getActivityStateInformation()
                .getAvailableChildren().size() - 1) {
            // 2.1
            // traversal direction is Backward
            traversalDirection = TraversalDirection.Backward;
            // 2.2
            // activity is the first activity in the activity's parent's list of Available Children
//            targetActivity = targetActivity.getParentActivity().getActivityStateInformation()
//                    .getAvailableChildren().get(0);
            // 2.3
            reversedDirection = true;
        }
        // 3
        if (traversalDirection == TraversalDirection.Forward) {
            // 3.1
            // Walking off the tree causes the sequencing session to end
            List<Activity> preOrderActivityList = activityTree.preorder();
            Activity lastAvailableActivity = null;
            for (int i = preOrderActivityList.size() - 1; i >= 0; i--) {
                if (activityTree.isAvailable(preOrderActivityList.get(i))) {
                    lastAvailableActivity = preOrderActivityList.get(i);
                    break;
                }
            }
            if (Objects.equals(targetActivity, lastAvailableActivity)
                    || (activityTree.isRoot(targetActivity) && !considerChildren)) {
                // 3.1.1
                UnifiedProcess.processTerminateDescendentAttempts(
                        new UnifiedProcessRequest(activityTree, activityTree.getRoot()));
                // 3.1.2
                return new SequencingBehaviorResult().setEndSequencingSession(true);
            }
            // 3.2
            if (targetActivity.isLeaf() || !considerChildren) {
                // 3.2.1
                if (targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                        .lastIndexOf(targetActivity) == targetActivity.getParentActivity().getActivityStateInformation()
                        .getAvailableChildren().size() - 1) {
                    // 3.2.1.1
                    // Recursion - Move to the activity's parent's next forward sibling.
                    // 3.2.1.2
                    // Return the result of the recursion
                    return processFlowTreeTraversal(
                            new SequencingRequest(sequencingRequest.getRequestType(), activityTree,
                                    targetActivity.getParentActivity())
                                    .setTraversalDirection(TraversalDirection.Forward)
                                    .setPreviousTraversalDirection(null)
                                    .setConsiderChildren(false));
                } else { // 3.2.2
                    // 3.2.2.1
                    Activity nextActivity = targetActivity.getParentActivity().getActivityStateInformation()
                            .getAvailableChildren().get(targetActivity.getParentActivity().getActivityStateInformation()
                                    .getAvailableChildren().indexOf(targetActivity) + 1);
                    // 3.2.2.2
                    return new SequencingBehaviorResult()
                            .setNextActivity(nextActivity)
                            .setTraversalDirection(traversalDirection);
                }
            } else { // 3.3 Entering a cluster - Forward.
                // 3.3.1
                if (!targetActivity.getActivityStateInformation().getAvailableChildren().isEmpty()) {
                    // 3.3.1.1
                    return new SequencingBehaviorResult()
                            .setNextActivity(targetActivity.getActivityStateInformation().getAvailableChildren().get(0))
                            .setTraversalDirection(traversalDirection);
                } else { // 3.3.2
                    // 3.3.2.1
                    return new SequencingBehaviorResult().setException(SequencingException.SB212);
                }
            }
        }
        // 4
        if (traversalDirection == TraversalDirection.Backward) {
            // 4.1
            // Cannot walk off the root of the activity tree.
            if (activityTree.isRoot(targetActivity)) {
                // 4.1.1.
                return new SequencingBehaviorResult().setException(SequencingException.SB213);
            }
            // 4.2
            if (targetActivity.isLeaf() || !considerChildren) {
                // 4.2.1
                // Only test 'forward only' if we are not going to leave this forward only cluster.
                if (!reversedDirection) {
                    // 4.2.1.1
                    // Test the control mode before traversing.
                    if (targetActivity.getParentActivity().getSequencingDefinition()
                            .getSequencingControlMode().isSequencingControlForwardOnly()) {
                        // 4.2.1.1.1
                        return new SequencingBehaviorResult().setException(SequencingException.SB214);
                    }
                }
                // 4.2.2
                if (targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                        .indexOf(targetActivity) == 0) {
                    // 4.2.2.1
                    // Recursion - Move to the activity's parent's next backward sibling.
                    // 4.2.2.2
                    // Return the result of the recursion.
                    return processFlowTreeTraversal(
                            new SequencingRequest(sequencingRequest.getRequestType(), activityTree,
                                    targetActivity.getParentActivity())
                                    .setTraversalDirection(TraversalDirection.Backward)
                                    .setPreviousTraversalDirection(null)
                                    .setConsiderChildren(false));
                } else { // 4.2.3
                    // 4.2.3.1
                    Activity nextActivity = targetActivity.getActivityStateInformation().getAvailableChildren().get(
                            targetActivity.getActivityStateInformation().getAvailableChildren()
                                    .indexOf(targetActivity) - 1);
                    // 4.2.3.2
                    return new SequencingBehaviorResult()
                            .setNextActivity(nextActivity)
                            .setTraversalDirection(traversalDirection);
                }
            } else { // 4.3 Entering a cluster - Backward
                // 4.3.1
                if (!targetActivity.getActivityStateInformation().getAvailableChildren().isEmpty()) {
                    // 4.3.1.1
                    if (targetActivity.getSequencingDefinition().getSequencingControlMode().isSequencingControlForwardOnly()) {
                        // 4.3.1.1.1
                        // Start at the beginning of a forward only cluster.
                        return new SequencingBehaviorResult()
                                .setNextActivity(targetActivity.getActivityStateInformation().getAvailableChildren().get(0))
                                .setTraversalDirection(TraversalDirection.Forward);
                    } else { // 4.3.1.2
                        // 4.3.1.2.1
                        // Start at the end of the cluster if we are backing into it.
                        return new SequencingBehaviorResult()
                                .setNextActivity(targetActivity.getActivityStateInformation().getAvailableChildren()
                                        .get(targetActivity.getActivityStateInformation().getAvailableChildren().size() - 1));
                    }
                } else { // 4.3.2
                    return new SequencingBehaviorResult().setException(SequencingException.SB212);
                }
            }
        }
        return new SequencingBehaviorResult();
    }

    /**
     * Flow Activity Traversal Subprocess [SB.2.2]
     *
     * For an activity, a traversal direction, and a previous traversal direction; returns the 'next' activity
     * in a directed traversal of the activity tree and True if the activity can be delivered; may indicate
     * control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Check Activity Process UP.5
     *   Flow Activity Traversal Subprocess SB.2.2
     *   Flow Tree Traversal Subprocess SB.2.1
     *   Sequencing Control Flow SM.1
     *   Sequencing Rules Check Process UP.2
     *
     * @see SequencingBehavior#processFlowActivityTraversal(SequencingRequest) SB.2.2
     * @see SequencingBehavior#processFlowTreeTraversal(SequencingRequest) SB.2.1
     * @see UnifiedProcess#processSequencingRulesCheck(UnifiedProcessRequest) UP.2
     */
    public static SequencingBehaviorResult processFlowActivityTraversal(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        TraversalDirection previousTraversalDirection = sequencingRequest.getPreviousTraversalDirection();
        // 1
        // Confirm that 'flow' is enabled.
        if (!targetActivity.getParentActivity().getSequencingDefinition()
                .getSequencingControlMode().isSequencingControlFlow()) {
            // 1.1
            return new SequencingBehaviorResult().setDeliverable(false).setNextActivity(targetActivity)
                    .setException(SequencingException.SB221);
        }
        UnifiedProcessResult unifiedProcessResult = UnifiedProcess.processSequencingRulesCheck(
                new UnifiedProcessRequest(activityTree, targetActivity)
                        .setConditionType(ConditionType.PRECONDITION)
                        .setRuleActions("Skip"));
        // 3
        // Activity is skipped, try to go to the 'next' activity.
        if (unifiedProcessResult.getAction() != null) {
            // 3.1
            SequencingBehaviorResult sequencingBehaviorResult = processFlowTreeTraversal(
                    new SequencingRequest(sequencingRequest.getRequestType(), activityTree, targetActivity)
                            .setTraversalDirection(traversalDirection)
                            .setPreviousTraversalDirection(previousTraversalDirection)
                            .setConsiderChildren(false));
            // 3.2
            if (sequencingBehaviorResult.getNextActivity() == null) {
                // 3.2.1
                return new SequencingBehaviorResult()
                        .setDeliverable(false)
                        .setNextActivity(targetActivity)
                        .setEndSequencingSession(sequencingBehaviorResult.getEndSequencingSession())
                        .setException(sequencingBehaviorResult.getException());
            } else { // 3.3
                // 3.3.1
                // Make sure the recursive call considers the correct direction.
                SequencingBehaviorResult result;
                if (previousTraversalDirection == TraversalDirection.Backward
                        && sequencingBehaviorResult.getTraversalDirection() == TraversalDirection.Backward) {
                    // 3.3.1.1
                    // Recursive call - make sure the 'next' activity is OK
                    result = processFlowActivityTraversal(new SequencingRequest(
                            sequencingRequest.getRequestType(), activityTree, sequencingBehaviorResult.getNextActivity())
                            .setTraversalDirection(traversalDirection).setPreviousTraversalDirection(null));
                } else { // 3.3.2
                    // 3.3.2.1
                    // Recursive call - make sure the 'next' activity is OK.
                    result = processFlowActivityTraversal(new SequencingRequest(
                            sequencingRequest.getRequestType(), activityTree, sequencingBehaviorResult.getNextActivity())
                            .setTraversalDirection(traversalDirection)
                            .setPreviousTraversalDirection(previousTraversalDirection));
                }
                // 3.3.3
                // possible exit from recursion
                return result;
            }
        }
        // 4
        // Make sure the activity is allowed
        UnifiedProcessResult checkActivityResult = UnifiedProcess.processCheckActivity(
                new UnifiedProcessRequest(activityTree, targetActivity));
        // 5
        if (checkActivityResult.getResult()) {
            return new SequencingBehaviorResult().setDeliverable(false).setNextActivity(targetActivity)
                    .setException(SequencingException.SB222);
        }
        // 6
        // Cannot deliver a non-leaf activity; enter the cluster looking for a leaf.
        if (!targetActivity.isLeaf()) {
            // 6.1
            SequencingBehaviorResult flowTreeResult = processFlowTreeTraversal(new SequencingRequest(
                    sequencingRequest.getRequestType(), activityTree, targetActivity)
                    .setTraversalDirection(traversalDirection).setPreviousTraversalDirection(null)
                    .setConsiderChildren(true));
            // 6.2
            if (flowTreeResult.getNextActivity() == null) {
                // 6.2.1
                return new SequencingBehaviorResult().setDeliverable(false).setNextActivity(targetActivity)
                        .setEndSequencingSession(flowTreeResult.getEndSequencingSession())
                        .setException(flowTreeResult.getException());
            } else { // 6.3
                SequencingBehaviorResult result;
                // 6.3.1
                // Check if we are flowing backward through a forward only cluster - must move forward instead.
                if (traversalDirection == TraversalDirection.Backward
                        && flowTreeResult.getTraversalDirection() == TraversalDirection.Forward) {
                    // 6.3.1.1
                    // Recursive call - Make sure the identified activity is OK.
                    result = processFlowActivityTraversal(new SequencingRequest(
                            sequencingRequest.getRequestType(), activityTree, flowTreeResult.getNextActivity())
                            .setTraversalDirection(TraversalDirection.Forward)
                            .setPreviousTraversalDirection(TraversalDirection.Backward));
                } else { // 6.3.2
                    // 6.3.2.1
                    // Recursive call - Make sure the identified activity is OK.
                    result = processFlowActivityTraversal(new SequencingRequest(
                            sequencingRequest.getRequestType(), activityTree, flowTreeResult.getNextActivity())
                            .setTraversalDirection(traversalDirection)
                            .setPreviousTraversalDirection(null));
                }
                // 6.3.3
                return result;
            }
        }
        // 7
        // Found a leaf
        return new SequencingBehaviorResult().setDeliverable(true).setNextActivity(targetActivity);
    }

    /**
     * Flow Subprocess [SB.2.3]
     *
     * For an activity, a traversal direction, and a consider children flag; indicates if the flow was successful
     * and at what activity the flow stopped; may indicate control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Flow Activity Traversal Subprocess SB.2.2
     *   Flow Tree Traversal Subprocess SB.2.1
     *
     * @see SequencingBehavior#processFlowActivityTraversal(SequencingRequest) SB.2.2
     * @see SequencingBehavior#processFlowTreeTraversal(SequencingRequest) SB.2.1
     */
    public static SequencingBehaviorResult processFlow(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        boolean considerChildren = sequencingRequest.isConsiderChildren();
        // 1
        // The candidate activity is where we start 'flowing' from.

        // 2
        // Attempt to move away from the starting activity, one activity in the specified direction.
        SequencingBehaviorResult flowTreeResult = processFlowTreeTraversal(new SequencingRequest(
                sequencingRequest.getRequestType(), activityTree, targetActivity)
                .setTraversalDirection(traversalDirection).setPreviousTraversalDirection(null)
                .setConsiderChildren(considerChildren));
        // 3
        if (flowTreeResult.getNextActivity() == null) {
            // 3.1
            return new SequencingBehaviorResult().setIdentifiedActivity(targetActivity)
                    .setDeliverable(false).setEndSequencingSession(flowTreeResult.getEndSequencingSession())
                    .setException(flowTreeResult.getException());
        } else { // 4
            // 4.1
            targetActivity = flowTreeResult.getNextActivity();
            // 4.2
            // Validate the candidate activity and traverse until a valid leaf is encountered.
            SequencingBehaviorResult flowActivityResult = processFlowActivityTraversal(new SequencingRequest(
                    sequencingRequest.getRequestType(), activityTree, targetActivity)
                    .setTraversalDirection(traversalDirection).setPreviousTraversalDirection(null));
            // 4.3
            return new SequencingBehaviorResult()
                    .setIdentifiedActivity(flowActivityResult.getNextActivity())
                    .setDeliverable(flowActivityResult.isDeliverable())
                    .setEndSequencingSession(flowActivityResult.getEndSequencingSession())
                    .setException(flowActivityResult.getException());
        }
    }

    /**
     * Choice Activity Traversal Subprocess [SB.2.4]
     *
     * For an activity and a traversal direction;
     * returns True if the activity can be reached; may return an exception code.
     *
     * Reference:
     *   Sequencing Control Forward Only SM.1
     *   Sequencing Rules Check Process UP.2
     *
     * @see UnifiedProcess#processSequencingRulesCheck(UnifiedProcessRequest) UP.2
     */
    public static SequencingBehaviorResult processChoiceActivityTraversal(SequencingRequest sequencingRequest) {
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        // 1
        if (traversalDirection == TraversalDirection.Forward) {
            // 1.1
            UnifiedProcessResult checkResult = UnifiedProcess.processSequencingRulesCheck(
                    new UnifiedProcessRequest(sequencingRequest.getTargetActivityTree(), targetActivity)
                            .setConditionType(ConditionType.PRECONDITION)
                            .setRuleActions("Stop Forward Traversal"));
            // 1.2
            if (checkResult.getAction() != null) {
                // 1.2.1
                return new SequencingBehaviorResult().setReachable(false).setException(SequencingException.SB241);
            }
            // 1.3
            return new SequencingBehaviorResult().setReachable(true);
        }
        // 2
        if (traversalDirection == TraversalDirection.Backward) {
            // 2.1
            if (targetActivity.getParentActivity() != null) {
                // 2.1.1
                if (targetActivity.getParentActivity().getSequencingDefinition().getSequencingControlMode()
                        .isSequencingControlForwardOnly()) {
                    // 2.1.1.1
                    return new SequencingBehaviorResult().setReachable(false).setException(SequencingException.SB242);
                }
            } else { // 2.2
                // 2.2.1
                // Cannot walk backward from the root of the activity tree.
                return new SequencingBehaviorResult().setReachable(false).setException(SequencingException.SB243);
            }
            // 2.3
            return new SequencingBehaviorResult().setReachable(true);
        }
        return new SequencingBehaviorResult().setReachable(true);
    }

    /**
     * Start Sequencing Request Process [SB.2.5]
     *
     * May return a delivery request; may indicate control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   Flow Subprocess SB.2.3
     *
     * @see SequencingBehavior#processFlow(SequencingRequest) SB.2.3
     */
    public static SequencingBehaviorResult processStartSequencingRequest(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        // Make sure the sequencing session has not already begun.
        if (currentActivity != null) {
            // 1.1
            // Nothing to deliver.
            return new SequencingBehaviorResult().setException(SequencingException.SB251);
        }
        // 2
        // Before starting, make sure the activity tree contains more than one activity.
        if (activityTree.getRoot().isLeaf()) {
            // 2.1
            // Only one activity it must be a leaf
            return new SequencingBehaviorResult().setDeliveryRequest(
                    new DeliveryRequest(activityTree, activityTree.getRoot()));
        } else { // 3
            // 3.1
            // Attempt to flow into the activity tree.
            SequencingBehaviorResult flowResult = processFlow(new SequencingRequest(
                    sequencingRequest.getRequestType(), activityTree, activityTree.getRoot())
                    .setTraversalDirection(TraversalDirection.Forward).setConsiderChildren(true));
            // 3.2
            if (!flowResult.isDeliverable()) {
                // 3.2.1
                // Nothing to Deliver
                return new SequencingBehaviorResult()
                        .setDeliveryRequest(new DeliveryRequest(activityTree, null))
                        .setEndSequencingSession(flowResult.getEndSequencingSession())
                        .setException(flowResult.getException());
            } else { // 3.3
                // 3.3.1
                return new SequencingBehaviorResult()
                        .setDeliveryRequest(new DeliveryRequest(activityTree, flowResult.getIdentifiedActivity()));
            }
        }
    }

    /**
     * Resume All Sequencing Request Process [SB.2.6]
     *
     * May return a delivery request; may return an exception code.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   Suspended Activity AM.1.2
     */
    public static SequencingBehaviorResult processResumeAllSequencingRequest(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        Activity suspendedActivity = activityTree.getGlobalStateInformation().getSuspendedActivity();
        // 1
        // Make sure the sequencing session has not already begun.
        if (currentActivity != null) {
            // 1.1
            // Nothing to deliver.
            return new SequencingBehaviorResult().setException(SequencingException.SB261);
        }
        // 2
        // Make sure there is something to resume.
        if (suspendedActivity == null) {
            // 2.1
            // Nothing to deliver.
            return new SequencingBehaviorResult().setException(SequencingException.SB262);
        }
        return new SequencingBehaviorResult()
                .setDeliveryRequest(new DeliveryRequest(activityTree, suspendedActivity));
    }

    /**
     * Continue Sequencing Request Process [SB.2.7]
     *
     * May return a delivery request; may indicate control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   Flow Subprocess SB.2.3
     *
     * @see SequencingBehavior#processFlow(SequencingRequest) SB.2.3
     */
    public static SequencingBehaviorResult processContinueSequencingRequest(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        // Make sure the sequencing session has already begun.
        if (currentActivity == null) {
            // 1.1
            return new SequencingBehaviorResult().setException(SequencingException.SB271);
        }
        // 2
        if (!activityTree.isRoot(currentActivity)) {
            // 2.1
            // Confirm a 'flow' traversal is allowed from the activity.
            if (!currentActivity.getSequencingDefinition().getSequencingControlMode().isSequencingControlFlow()) {
                // 2.1.1
                return new SequencingBehaviorResult().setException(SequencingException.SB272);
            }
        }
        // 3
        // Flow in a forward direction to the next allowed activity.
        SequencingBehaviorResult flowResult = processFlow(new SequencingRequest(
                sequencingRequest.getRequestType(), activityTree, currentActivity)
                .setTraversalDirection(TraversalDirection.Forward).setConsiderChildren(false));
        // 4
        if (!flowResult.isDeliverable()) {
            // 4.1
            // Nothing to deliver.
            return new SequencingBehaviorResult()
                    .setEndSequencingSession(flowResult.getEndSequencingSession())
                    .setException(flowResult.getException());
        } else { // 5
            // 5.1
            return new SequencingBehaviorResult()
                    .setDeliveryRequest(new DeliveryRequest(activityTree, flowResult.getIdentifiedActivity()));
        }
    }

    /**
     * Previous Sequencing Request Process [SB.2.8]
     *
     * May return a delivery request; may return an exception code.
     *
     * Reference:
     *   Current Activity AM.1.2
     *   Flow Subprocess SB.2.3
     *
     * @see SequencingBehavior#processFlow(SequencingRequest) SB.2.3
     */
    public static SequencingBehaviorResult processPreviousSequencingRequest(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        // 1
        // Make sure the sequencing session has already begun.
        if (currentActivity == null) {
            // 1.1
            return new SequencingBehaviorResult().setException(SequencingException.SB281);
        }
        // 2
        if (!activityTree.isRoot(currentActivity)) {
            // 2.1
            // Confirm a 'flow' traversal is allowed from the activity.
            if (!currentActivity.getSequencingDefinition().getSequencingControlMode().isSequencingControlFlow()) {
                // 2.1.1
                return new SequencingBehaviorResult().setException(SequencingException.SB282);
            }
        }
        // 3
        // Flow in a backward direction to the next allowed activity.
        SequencingBehaviorResult flowResult = processFlow(new SequencingRequest(
                sequencingRequest.getRequestType(), activityTree, currentActivity)
                .setTraversalDirection(TraversalDirection.Backward).setConsiderChildren(false));
        // 4
        if (!flowResult.isDeliverable()) {
            // 4.1
            // Nothing to deliver.
            return new SequencingBehaviorResult()
                    .setEndSequencingSession(flowResult.getEndSequencingSession())
                    .setException(flowResult.getException());
        } else { // 5
            // 5.1
            return new SequencingBehaviorResult()
                    .setDeliveryRequest(new DeliveryRequest(activityTree, flowResult.getIdentifiedActivity()));
        }
    }

    /**
     * Choice Sequencing Request Process [SB.2.9]
     *
     * For a target activity; may return a delivery request; may change the Current Activity;
     * may return an exception code.
     *
     * Reference:
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Available Children AM.1.1
     *   Check Activity Process UP.5
     *   Choice Flow Subprocess SB.2.9.1
     *   Choice Activity Traversal Subprocess SB.2.4
     *   Current Activity AM.1.2
     *   End Attempt Process UP.4
     *   Flow Subprocess SB.2.3
     *   Sequencing Control Mode Choice SM.1
     *   Sequencing Control Choice Exit SM.1
     *   Sequencing Rules Check Process UP.2
     *   Terminate Descendent Attempts Process UP.3
     *   adlseq:constrainedChoice SCORM SN
     *   adlseq:preventActivation SCORM SN
     *
     * @see UnifiedProcess#processCheckActivity(UnifiedProcessRequest) UP.5
     * @see SequencingBehavior#processChoiceFlow(SequencingRequest) SB.2.9.1
     * @see SequencingBehavior#processChoiceActivityTraversal(SequencingRequest) SB.2.4
     * @see UnifiedProcess#processEndAttempt(UnifiedProcessRequest) UP.4
     * @see SequencingBehavior#processFlow(SequencingRequest) SB.2.3
     * @see UnifiedProcess#processSequencingRulesCheck(UnifiedProcessRequest) UP.2
     * @see UnifiedProcess#processTerminateDescendentAttempts(UnifiedProcessRequest) UP.3
     */
    public static SequencingBehaviorResult processChoiceSequencingRequest(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity currentActivity = activityTree.getGlobalStateInformation().getCurrentActivity();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        // 1
        // There must be a target activity for choice.
        if (targetActivity == null) {
            // 1.1
            // Nothing to deliver.
            return new SequencingBehaviorResult().setException(SequencingException.SB291);
        }
        // 2
        // From root of the activity tree to the target activity, inclusive
        LinkedList<Activity> activityPath = new LinkedList<>();
        Activity tmp = targetActivity;
        while (tmp != null) {
            activityPath.addFirst(tmp);
            tmp = tmp.getParentActivity();
        }
        // 3
        for (Activity activity : activityPath) {
            // 3.1
            if (activityTree.isRoot(activity)) {
                // 3.1.1
                // The activity is currently not available.
                if (!targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                        .contains(targetActivity)) {
                    // 3.1.1.1
                    return new SequencingBehaviorResult().setException(SequencingException.SB292);
                }
            }
            // 3.2
            // Cannot choose something that is hidden.
            UnifiedProcessResult rulesCheckResult = UnifiedProcess.processSequencingRulesCheck(
                    new UnifiedProcessRequest(activityTree, activity)
                            .setConditionType(ConditionType.PRECONDITION)
                            .setRuleActions("Hidden from Choice"));
            // 3.3
            if (rulesCheckResult.getAction() != null) {
                // 3.3.1
                // Nothing to deliver.
                // Cannot choose something that is hidden.
                return new SequencingBehaviorResult().setException(SequencingException.SB293);
            }
        }
        // 4
        if (!activityTree.isRoot(targetActivity)) {
            // 4.1
            // Confirm that control mode allow 'choice' of the target.
            if (!targetActivity.getParentActivity().getSequencingDefinition()
                    .getSequencingControlMode().isSequencingControlChoice()) {
                // 4.1.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB294);
            }
        }
        // 5
        // Has the sequencing session a already begun?
        Activity commonAncestor;
        if (currentActivity != null) {
            // 5.1
            commonAncestor = activityTree.findCommonAncestorFor(currentActivity, targetActivity);
        } else { // 6
            // 6.1
            // No, choosing the target will start the sequencing session.
            commonAncestor = activityTree.getRoot();
        }
        boolean breakAllCases = false;
        // 7
        // Case #1 - select the current activity.
        if (Objects.equals(currentActivity, targetActivity)) {
            // 7.1
            // Nothing to do in this case.
            breakAllCases = true;
        }
        // 8
        // Case #2 - same cluster; move toward the target activity.
        if (!breakAllCases && targetActivity.isSiblingActivity(currentActivity)) {
            // 8.1
            // We are attempting to walk toward the target activity.
            // Once we reach the target activity, we don't need to test it.
            // From the current activity to the target activity, exclusive of the target activity
            List<Activity> activityList = ActivityTree.getActivitySequenceList(
                    currentActivity, true, targetActivity, false);
            // 8.2
            // Nothing to choose.
            if (activityList.isEmpty()) {
                // 8.2.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB295);
            }
            // 8.3
            List<Activity> preOrderList = activityTree.preorder();
            TraversalDirection traverse;
            if (preOrderList.indexOf(targetActivity) > preOrderList.indexOf(currentActivity)) {
                // 8.3.1
                traverse = TraversalDirection.Forward;
            } else { // 8.4
                // 8.4.1
                traverse = TraversalDirection.Backward;
            }
            // 8.5
            for (Activity activity : activityList) {
                // 8.5.1
                SequencingBehaviorResult choiceActivityResult = processChoiceActivityTraversal(
                        new SequencingRequest(sequencingRequest.getRequestType(), activityTree, activity)
                                .setTraversalDirection(traverse));
                // 8.5.2
                if (!choiceActivityResult.isReachable()) {
                    // 8.5.2.1
                    // Nothing to deliver.
                    return new SequencingBehaviorResult().setException(choiceActivityResult.getException());
                }
            }
            // 8.6
            breakAllCases = true;
        }
        // 9
        // Case #3 - path to the target is forward in the activity tree
        if (!breakAllCases && Objects.equals(currentActivity, commonAncestor) || currentActivity == null) {
            // 9.1
            // From the common ancestor to the target activity, exclusive of the target activity.
            LinkedList<Activity> activityPath3 = new LinkedList<>();
            tmp = targetActivity.getParentActivity();
            while (tmp != null) {
                activityPath3.addFirst(tmp);
                if (tmp.equals(commonAncestor)) {
                    break;
                }
                tmp = tmp.getParentActivity();
            }
            // 9.2
            if (activityPath3.isEmpty()) {
                // 9.2.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB295);
            }
            // 9.3
            for (Activity activity : activityPath3) {
                // 9.3.1
                SequencingBehaviorResult choiceActivityResult = processChoiceActivityTraversal(
                        new SequencingRequest(sequencingRequest.getRequestType(), activityTree, activity)
                                .setTraversalDirection(TraversalDirection.Forward));
                // 9.3.2
                if (!choiceActivityResult.isReachable()) {
                    // 9.3.2.1
                    // Nothing to deliver.
                    return new SequencingBehaviorResult().setException(choiceActivityResult.getException());
                }
                // 9.3.3
                // If the activity being considered is not already active,
                // make sure we are allowed to activate it.
                if (!activity.getActivityStateInformation().isActivityIsActive()
                        && !activity.equals(commonAncestor)
                        && activity.getSequencingDefinition().getConstrainChoiceControls().isPreventActivation()) {
                    // 9.3.3.1
                    return new SequencingBehaviorResult().setException(SequencingException.SB296);
                }
            }
            // 9.4
            breakAllCases = true;
        }
        // 10
        // Case #4 - path to the target is backward in the activity tree.
        if (!breakAllCases && Objects.equals(targetActivity, commonAncestor)) {
            // 10.1
            // From the current activity to the target activity, inclusive
            List<Activity> activityPath4 = new ArrayList<>();
            tmp = currentActivity;
            while (tmp != null) {
                activityPath4.add(tmp);
                if (tmp.equals(targetActivity)) {
                    break;
                }
                tmp = tmp.getParentActivity();
            }
            // 10.2
            if (activityPath4.isEmpty()) {
                // 10.2.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB295);
            }
            // 10.3
            for (int i = 0; i < activityPath4.size(); i++) {
                // 10.3.1
                if (i != activityPath4.size() - 1) {
                    // 10.3.1.1
                    // Make sure an activity that should not exit will exit if the target is delivered.
                    if (!activityPath4.get(i).getSequencingDefinition().getSequencingControlMode()
                            .isSequencingControlChoiceExit()) {
                        // 10.3.1.1.1
                        return new SequencingBehaviorResult().setException(SequencingException.SB297);
                    }
                }
            }
            // 10.4
            breakAllCases = true;
        }
        // 11
        // Case #5 - target is a descendent activity of the common ancestor.
        if (!breakAllCases && commonAncestor.isDescendent(targetActivity)) {
            // 11.1
            // From the current activity to the common ancestor, excluding the common ancestor.
            List<Activity> activityPath5 = new LinkedList<>();
            tmp = currentActivity;
            while (tmp != null && !tmp.equals(commonAncestor)) {
                activityPath5.add(currentActivity);
                tmp = tmp.getParentActivity();
            }
            // 11.2
            if (activityPath5.isEmpty()) {
                // 11.2.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB295);
            }
            // 11.3
            Activity constrainedActivity = null;
            // 11.4
            // Walk up the tree to the common ancestor.
            for (Activity activity : activityPath5) {
                // 11.4.1
                // Make sure an activity that should not exit will exit if the target is delivered.
                if (!activity.getSequencingDefinition().getSequencingControlMode().isSequencingControlChoiceExit()) {
                    // 11.4.1.1
                    // Nothing to deliver.
                    return new SequencingBehaviorResult().setException(SequencingException.SB297);
                }
                // 11.4.2
                // Find the closest constrained activity to the current activity.
                if (constrainedActivity == null) {
                    // 11.4.2.1
                    if (activity.getSequencingDefinition().getConstrainChoiceControls().isConstrainChoice()) {
                        // 11.4.2.1.1
                        constrainedActivity = activity;
                    }
                }
            }
            // 11.5
            if (constrainedActivity != null) {
                TraversalDirection traverse;
                // 11.5.1
                List<Activity> preOrderList = activityTree.preorder();
                if (preOrderList.indexOf(targetActivity) > preOrderList.indexOf(constrainedActivity)) {
                    // 11.5.1.1
                    // 'Flow' in a forward direction to see what activity comes next.
                    traverse = TraversalDirection.Forward;
                } else { // 11.5.2
                    // 11.5.2.1
                    // 'Flow' in a backward direction to see what activity comes next.
                    traverse = TraversalDirection.Backward;
                }
                // 11.5.3
                SequencingBehaviorResult choiceFlowResult = processChoiceFlow(new SequencingRequest(
                        sequencingRequest.getRequestType(), activityTree, constrainedActivity)
                        .setTraversalDirection(traverse));
                // 11.5.4
                Activity activityToConsider = choiceFlowResult.getIdentifiedActivity();
                // 11.5.5
                // Make sure the target activity is within the set of 'flow' constrained choices.
                if (!(activityToConsider.isDescendent(targetActivity) && activityTree.isAvailable(targetActivity))
                        && !Objects.equals(targetActivity, activityToConsider)
                        && !Objects.equals(targetActivity, constrainedActivity)) {
                    // 11.5.5.1
                    return new SequencingBehaviorResult().setException(SequencingException.SB298);
                }
            }
            // 11.6
            // from the common ancestor to the target activity, exclusive of the target activity
            LinkedList<Activity> activityPath6 = new LinkedList<>();
            tmp = targetActivity.getParentActivity();
            while (tmp != null) {
                activityPath6.addFirst(tmp);
                tmp = tmp.getParentActivity();
            }
            // 11.7
            if (activityPath6.isEmpty()) {
                // 11.7.1
                // Nothing to deliver.
                return new SequencingBehaviorResult().setException(SequencingException.SB295);
            }
            // 11.8
            // Walk toward the target activity.
            List<Activity> preOrderList = activityTree.preorder();
            if (preOrderList.indexOf(targetActivity) > preOrderList.indexOf(currentActivity)) {
                // 11.8.1
                for (Activity activity : activityPath6) {
                    // 11.8.1.1
                    SequencingBehaviorResult choiceActivityResult = processChoiceActivityTraversal(
                            new SequencingRequest(sequencingRequest.getRequestType(), activityTree, activity)
                                    .setTraversalDirection(TraversalDirection.Forward));
                    // 11.8.1.2
                    if (!choiceActivityResult.isReachable()) {
                        // 11.8.1.2.1
                        // Nothing to deliver.
                        return new SequencingBehaviorResult().setException(choiceActivityResult.getException());
                    }
                    // 11.8.1.3
                    // If the activity being considered is not already active,
                    // make sure we are allowed to activate it.
                    if (!activity.getActivityStateInformation().isActivityIsActive()
                            && !Objects.equals(activity, commonAncestor) && activity
                            .getSequencingDefinition().getConstrainChoiceControls().isPreventActivation()) {
                        // 11.8.1.3.1
                        // Nothing to deliver.
                        return new SequencingBehaviorResult().setException(SequencingException.SB296);
                    }
                }
            } else { // 11.9
                // 11.9.1
                activityPath6.addLast(targetActivity);
                // 11.9.2
                for (Activity activity : activityPath6) {
                    // 11.9.2.1
                    // If the activity being considered is not already active,
                    // make sure we are allowed to activate it.
                    if (!activity.getActivityStateInformation().isActivityIsActive()
                            && !Objects.equals(activity, commonAncestor) && activity
                            .getSequencingDefinition().getConstrainChoiceControls().isPreventActivation()) {
                        // 11.9.2.1.1
                        return new SequencingBehaviorResult().setException(SequencingException.SB296);
                    }
                }
            }
            // 11.10
            breakAllCases = true;
        }
        // 12
        if (targetActivity.isLeaf()) {
            // 12.1
            return new SequencingBehaviorResult()
                    .setDeliveryRequest(new DeliveryRequest(activityTree, targetActivity));
        }
        // 13
        // The identified activity is a cluster.
        // Enter the cluster and attempt to find a descendent leaf to deliver.
        SequencingBehaviorResult flowResult = processFlow(new SequencingRequest(
                sequencingRequest.getRequestType(), activityTree, targetActivity)
                .setTraversalDirection(TraversalDirection.Forward)
                .setConsiderChildren(true));
        // 14
        // Nothing to deliver, but we succeeded in reaching the target activity - move the current activity.
        if (!flowResult.isDeliverable()) {
            // 14.1
            UnifiedProcess.processTerminateDescendentAttempts(new UnifiedProcessRequest(activityTree, commonAncestor));
            // 14.2
            UnifiedProcess.processEndAttempt(new UnifiedProcessRequest(activityTree, commonAncestor));
            // 14.3
            activityTree.getGlobalStateInformation().setCurrentActivity(targetActivity);
            // 14.4
            // Nothing to deliver.
            return new SequencingBehaviorResult().setException(SequencingException.SB299);
        } else { // 15
            // 15.1
            return new SequencingBehaviorResult()
                    .setDeliveryRequest(new DeliveryRequest(activityTree, flowResult.getIdentifiedActivity()));
        }
    }

    /**F
     * Choice Flow Subprocess [SB.2.9.1]
     *
     * For an activity and a traversal direction; indicates at what activity the flow stopped.
     *
     * Reference:
     *   Choice Flow Tree Traversal Subprocess SB.2.9.2
     *
     * @see SequencingBehavior#processChoiceFlowTreeTraversal(SequencingRequest) SB.2.9.2
     */
    public static SequencingBehaviorResult processChoiceFlow(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        // 1
        // Attempt to move away from the activity, 'one' activity in the specified direction.
        SequencingBehaviorResult choiceFlowTreeResult = processChoiceFlowTreeTraversal(
                new SequencingRequest(sequencingRequest.getRequestType(), activityTree, targetActivity)
                        .setTraversalDirection(traversalDirection));
        // 2
        if (choiceFlowTreeResult.getNextActivity() == null) {
            // 2.1
            return new SequencingBehaviorResult().setIdentifiedActivity(targetActivity);
        } else { // 3
            // 3.1
            return new SequencingBehaviorResult().setIdentifiedActivity(choiceFlowTreeResult.getNextActivity());
        }
    }

    /**
     * Choice Flow Tree Traversal Subprocess [SB.2.9.2]
     *
     * For an activity, a traversal direction; returns the 'next' activity in directed traversal of the activity tree.
     *
     * Reference:
     *   Available Children AM.1.1
     *   Choice Flow Tree Traversal Subprocess SB.2.9.2
     *
     * @see SequencingBehavior#processChoiceFlowTreeTraversal(SequencingRequest) SB.2.9.2
     */
    public static SequencingBehaviorResult processChoiceFlowTreeTraversal(SequencingRequest sequencingRequest) {
        ActivityTree activityTree = sequencingRequest.getTargetActivityTree();
        Activity targetActivity = sequencingRequest.getTargetActivity();
        TraversalDirection traversalDirection = sequencingRequest.getTraversalDirection();
        // 1
        if (traversalDirection == TraversalDirection.Forward) {
            // 1.1
            // Cannot walk off the activity tree.
            List<Activity> preOrderActivityList = activityTree.preorder();
            Activity lastAvailableActivity = null;
            for (int i = preOrderActivityList.size() - 1; i >= 0; i--) {
                if (activityTree.isAvailable(preOrderActivityList.get(i))) {
                    lastAvailableActivity = preOrderActivityList.get(i);
                    break;
                }
            }
            if (Objects.equals(targetActivity, lastAvailableActivity) || activityTree.isRoot(targetActivity)) {
                // 1.1.1
                return new SequencingBehaviorResult();
            }
            // 1.2
            if (targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                    .lastIndexOf(targetActivity) == targetActivity.getParentActivity().getActivityStateInformation()
                    .getAvailableChildren().size() - 1) {
                // 1.2.1
                // Recursion - Move to the activity's parent's next forward sibling.
                // 1.2.2
                // Return the result of the recursion
                return processChoiceFlowTreeTraversal(new SequencingRequest(
                        sequencingRequest.getRequestType(), activityTree, targetActivity.getParentActivity())
                        .setTraversalDirection(TraversalDirection.Forward));
            } else { // 1.3
                // 1.3.1
                Activity nextActivity = targetActivity.getParentActivity().getActivityStateInformation()
                        .getAvailableChildren().get(targetActivity.getParentActivity().getActivityStateInformation()
                                .getAvailableChildren().indexOf(targetActivity) + 1);
                // 1.3.2
                return new SequencingBehaviorResult().setNextActivity(nextActivity);
            }
        }
        // 2
        if (traversalDirection == TraversalDirection.Backward) {
            // 2.1
            // Cannot walk off the root of the activity tree.
            if (activityTree.isRoot(targetActivity)) {
                // 2.1.1
                return new SequencingBehaviorResult();
            }
            // 2.2
            if (targetActivity.getParentActivity().getActivityStateInformation().getAvailableChildren()
                    .indexOf(targetActivity) == 0) {
                // 2.2.1
                // Recursion – Move to the activity’s parent’s next backward sibling.
                // 2.2.2
                // // Return the result of the recursion
                return processChoiceFlowTreeTraversal(new SequencingRequest(
                        sequencingRequest.getRequestType(), activityTree, targetActivity.getParentActivity())
                        .setTraversalDirection(TraversalDirection.Backward));
            } else { // 2.3
                // 2.3.1
                Activity nextActivity = targetActivity.getParentActivity().getActivityStateInformation()
                        .getAvailableChildren().get(targetActivity.getParentActivity().getActivityStateInformation()
                                .getAvailableChildren().indexOf(targetActivity) - 1);
                // 2.3.2
                return new SequencingBehaviorResult().setNextActivity(nextActivity);
            }
        }
        return new SequencingBehaviorResult();
    }

    /**
     * Sequencing Request Process [SB.2.12]
     *
     * For a sequencing request; validates the sequencing request; my return a delivery request; may indicate
     * control be returned to the LTS; may return an exception code.
     *
     * Reference:
     *   Choice Sequencing Request Process SB.2.9
     *   Continue Sequencing Request Process SB.2.7
     *   Exit Sequencing Request Process SB.2.11
     *   Previous Sequencing Request Process SB.2.8
     *   Resume All Sequencing Request Process SB.2.6
     *   Retry Sequencing Request Process SB.2.10
     *   Start Sequencing Request Process SB.2.5
     *
     */
    public static SequencingBehaviorResult processSequencingRequest(SequencingRequest sequencingRequest) {
        return new SequencingBehaviorResult();
    }

}
