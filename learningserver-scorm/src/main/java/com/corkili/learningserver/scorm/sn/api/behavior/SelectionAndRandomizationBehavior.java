package com.corkili.learningserver.scorm.sn.api.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.corkili.learningserver.scorm.sn.api.request.SelectionAndRandomizationRequest;
import com.corkili.learningserver.scorm.sn.model.definition.RandomizationControls;
import com.corkili.learningserver.scorm.sn.model.definition.SelectionControls;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public class SelectionAndRandomizationBehavior {

    /**
     * Select Children Process [SR.1]
     *
     * For an activity; may change the Available Children for the activity.
     *
     * Reference:
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Available Children AM.1.1
     *   Activity Progress Status TM.1.2.1
     *   Selection Count SM.9
     *   Selection Count Status SM.9
     *   Selection Timing SM.9
     */
    public static void selectChildren(SelectionAndRandomizationRequest request) {
        Activity targetActivity = request.getTargetActivity();
        SelectionControls selectionControls = targetActivity.getSequencingDefinition().getSelectionControls();
        // 1
        // Cannot apply selection to a leaf activity.
        if (targetActivity.isLeaf()) {
            // 1.1
            return;
        }
        // 2
        // Cannot apply selection to a suspended or active activity.
        if (targetActivity.getActivityStateInformation().isActivityIsSuspended()
                || targetActivity.getActivityStateInformation().isActivityIsActive()) {
            // 2.1
            return;
        }
        // 3
        if (selectionControls.getSelectionTiming().getValue().equals("Once")) { // 4
            // 4.1
            // If the activity has not been attempted yet.
            if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()) {
                // 4.1.1
                if (selectionControls.isSelectionCountStatus()) {
                    // 4.1.1.1
                    List<Activity> childList = new LinkedList<>();
                    // 4.1.1.2
                    Set<Integer> indexSet = new HashSet<>();
                    int times = Math.min(
                            selectionControls.getSelectionCount().getValue(), targetActivity.getChildren().size());
                    while (indexSet.size() < times) {
                        indexSet.add((int) (Math.random() * (targetActivity.getChildren().size() - 1)));
                    }
                    List<Integer> indexList = new ArrayList<>(indexSet);
                    Collections.sort(indexList);
                    for (Integer i : indexList) {
                        childList.add(targetActivity.getChildren().get(i));
                    }
                    // 4.1.1.3
                    targetActivity.getActivityStateInformation().getAvailableChildren().clear();
                    targetActivity.getActivityStateInformation().getAvailableChildren().addAll(childList);
                }
            }
            // 4.2
            // exit
        }
    }

    /**
     * Randomize Children Process [SR.2]
     *
     * For an activity; may change the Available Children for the activity.
     *
     * Reference:
     *   Activity is Active AM.1.1
     *   Activity is Suspended AM.1.1
     *   Available Children AM.1.1
     *   Activity Progress Status TM.1.2.1
     *   Randomize Children SM.10
     *   Randomization Timing SM.10
     */
    public static void randomizeChildren(SelectionAndRandomizationRequest request) {
        Activity targetActivity = request.getTargetActivity();
        RandomizationControls randomizationControls = targetActivity.getSequencingDefinition().getRandomizationControls();
        // 1
        // Cannot apply randomization to a leaf activity
        if (targetActivity.isLeaf()) {
            // 1.1
            return;
        }
        // 2
        // Cannot apply randomization to a suspended or active activity.
        if (targetActivity.getActivityStateInformation().isActivityIsSuspended()
                || targetActivity.getActivityStateInformation().isActivityIsActive()) {
            // 2.1
            return;
        }
        // 3
        // 4
        if (randomizationControls.getRandomizationTiming().getValue().equals("Once")) {
            // 4.1
            // If the activity has not been attempted yet.
            if (!targetActivity.getActivityProgressInformation().isActivityProgressStatus()) {
                // 4.1.1
                if (randomizationControls.isRandomizeChildren()) {
                    // 4.1.1.1
                    Collections.shuffle(targetActivity.getActivityStateInformation().getAvailableChildren());
                }
            }
            // 4.2
            // exit
        } else if (randomizationControls.getRandomizationTiming().getValue().equals("On Each New Attempt")) { // 5
            // 5.1
            if (randomizationControls.isRandomizeChildren()) {
                // 5.1.1
                Collections.shuffle(targetActivity.getActivityStateInformation().getAvailableChildren());
            }
            // 5.2
            // exit
        }
        // 6
        // Undefined timing attribute
        // exit
    }

}
