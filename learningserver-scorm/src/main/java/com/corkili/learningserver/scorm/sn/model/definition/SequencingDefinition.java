package com.corkili.learningserver.scorm.sn.model.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SequencingDefinition {

    private final SequencingControlMode sequencingControlMode;
    private final ConstrainChoiceControls constrainChoiceControls;
    private final List<SequencingRuleDescription> sequencingRuleDescriptions;
    private final LimitConditions limitConditions;
    private final List<RollupRuleDescription> rollupRuleDescriptions;
    private final RollupControls rollupControls;
    private final RollupConsiderationControls rollupConsiderationControls;
    private final List<ObjectiveDescription> objectiveDescriptions;
    private final SelectionControls selectionControls;
    private final RandomizationControls randomizationControls;
    private final DeliveryControls deliveryControls;
    private final CompletionThreshold completionThreshold;

    public SequencingDefinition() {
        sequencingControlMode = new SequencingControlMode();
        constrainChoiceControls = new ConstrainChoiceControls();
        sequencingRuleDescriptions = new ArrayList<>();
        limitConditions = new LimitConditions();
        rollupRuleDescriptions = new ArrayList<>();
        rollupControls = new RollupControls();
        rollupConsiderationControls = new RollupConsiderationControls();
        objectiveDescriptions = new ArrayList<>();
        selectionControls = new SelectionControls();
        randomizationControls = new RandomizationControls();
        deliveryControls = new DeliveryControls();
        completionThreshold = new CompletionThreshold();
    }

    public SequencingControlMode getSequencingControlMode() {
        return sequencingControlMode;
    }

    public ConstrainChoiceControls getConstrainChoiceControls() {
        return constrainChoiceControls;
    }

    public List<SequencingRuleDescription> getSequencingRuleDescriptions() {
        return sequencingRuleDescriptions;
    }

    public LimitConditions getLimitConditions() {
        return limitConditions;
    }

    public List<RollupRuleDescription> getRollupRuleDescriptions() {
        return rollupRuleDescriptions;
    }

    public RollupControls getRollupControls() {
        return rollupControls;
    }

    public RollupConsiderationControls getRollupConsiderationControls() {
        return rollupConsiderationControls;
    }

    public List<ObjectiveDescription> getObjectiveDescriptions() {
        return objectiveDescriptions;
    }

    public SelectionControls getSelectionControls() {
        return selectionControls;
    }

    public RandomizationControls getRandomizationControls() {
        return randomizationControls;
    }

    public DeliveryControls getDeliveryControls() {
        return deliveryControls;
    }

    public CompletionThreshold getCompletionThreshold() {
        return completionThreshold;
    }

    public ObjectiveDescription findObjectiveDescriptionByID(String objectiveID) {
        for (ObjectiveDescription objectiveDescription : objectiveDescriptions) {
            if (Objects.equals(objectiveID, objectiveDescription.getObjectiveID())) {
                return objectiveDescription;
            }
        }
        return null;
    }
}
