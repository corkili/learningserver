package com.corkili.learningserver.scorm.sn.model.definition;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;
import com.corkili.learningserver.scorm.sn.model.tracking.ObjectiveProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

/**
 * @see com.corkili.learningserver.scorm.cam.model.Objective
 */
public class ObjectiveDescription implements DefinitionElementSet {

    private String objectiveID;
    private boolean objectiveSatisfiedByMeasure;
    private final DecimalWithRange objectiveMinimumSatisfiedNormalizedMeasure;
    private final boolean objectiveContributesToRollup;
    private final List<ObjectiveMap> objectiveMaps;
    private final ObjectiveProgressInformation objectiveProgressInformation;
    private final Activity context;

    public ObjectiveDescription(boolean isPrimaryObjective, Activity context) {
        objectiveSatisfiedByMeasure = false;
        objectiveMinimumSatisfiedNormalizedMeasure = new DecimalWithRange(1, -1, 1, 4);
        objectiveContributesToRollup = isPrimaryObjective;
        objectiveMaps = new ArrayList<>();
        objectiveProgressInformation = new ObjectiveProgressInformation(this);
        this.context = context;
    }

    public String getObjectiveID() {
        return objectiveID;
    }

    public void setObjectiveID(String objectiveID) {
        this.objectiveID = objectiveID;
    }

    public boolean isObjectiveSatisfiedByMeasure() {
        return objectiveSatisfiedByMeasure;
    }

    public void setObjectiveSatisfiedByMeasure(boolean objectiveSatisfiedByMeasure) {
        this.objectiveSatisfiedByMeasure = objectiveSatisfiedByMeasure;
    }

    public DecimalWithRange getObjectiveMinimumSatisfiedNormalizedMeasure() {
        return objectiveMinimumSatisfiedNormalizedMeasure;
    }

    public boolean isObjectiveContributesToRollup() {
        return objectiveContributesToRollup;
    }

    public List<ObjectiveMap> getObjectiveMaps() {
        return objectiveMaps;
    }

    public ObjectiveProgressInformation getObjectiveProgressInformation() {
        return objectiveProgressInformation;
    }

    public Activity getContext() {
        return context;
    }
}
