package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RollupRules
 */
public class RollupControls implements DefinitionElementSet {

    private boolean rollupObjectiveSatisfied;
    private final DecimalWithRange rollupObjectiveMeasureWeight;
    private boolean rollupProgressCompletion;

    public RollupControls() {
        rollupObjectiveSatisfied = true;
        rollupObjectiveMeasureWeight = new DecimalWithRange(1, 0, 1, 4);
        rollupProgressCompletion = true;
    }

    public boolean isRollupObjectiveSatisfied() {
        return rollupObjectiveSatisfied;
    }

    public void setRollupObjectiveSatisfied(boolean rollupObjectiveSatisfied) {
        this.rollupObjectiveSatisfied = rollupObjectiveSatisfied;
    }

    public DecimalWithRange getRollupObjectiveMeasureWeight() {
        return rollupObjectiveMeasureWeight;
    }

    public boolean isRollupProgressCompletion() {
        return rollupProgressCompletion;
    }

    public void setRollupProgressCompletion(boolean rollupProgressCompletion) {
        this.rollupProgressCompletion = rollupProgressCompletion;
    }
}
