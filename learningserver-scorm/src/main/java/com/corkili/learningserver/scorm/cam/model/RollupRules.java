package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class RollupRules {

    // attributes
    private boolean rollupObjectiveSatisfied; // O true
    private boolean rollupProgressCompletion; // O true
    private Decimal objectiveMeasureWeight; // O 1.0000 [0.0000,1.0000]

    // elements
    private List<RollupRule> rollupRuleList; // 0...n

    public RollupRules() {
        rollupObjectiveSatisfied = true;
        rollupProgressCompletion = true;
        objectiveMeasureWeight = new Decimal("1.0000", 4);
        rollupRuleList = new ArrayList<>();
    }

    public boolean isRollupObjectiveSatisfied() {
        return rollupObjectiveSatisfied;
    }

    public void setRollupObjectiveSatisfied(boolean rollupObjectiveSatisfied) {
        this.rollupObjectiveSatisfied = rollupObjectiveSatisfied;
    }

    public boolean isRollupProgressCompletion() {
        return rollupProgressCompletion;
    }

    public void setRollupProgressCompletion(boolean rollupProgressCompletion) {
        this.rollupProgressCompletion = rollupProgressCompletion;
    }

    public Decimal getObjectiveMeasureWeight() {
        return objectiveMeasureWeight;
    }

    public void setObjectiveMeasureWeight(Decimal objectiveMeasureWeight) {
        this.objectiveMeasureWeight = objectiveMeasureWeight;
    }

    public List<RollupRule> getRollupRuleList() {
        return rollupRuleList;
    }

    public void setRollupRuleList(List<RollupRule> rollupRuleList) {
        this.rollupRuleList = rollupRuleList;
    }
}
