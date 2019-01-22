package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("rollupObjectiveSatisfied", rollupObjectiveSatisfied)
                .append("rollupProgressCompletion", rollupProgressCompletion)
                .append("objectiveMeasureWeight", objectiveMeasureWeight)
                .append("rollupRuleList", rollupRuleList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RollupRules that = (RollupRules) o;

        return new EqualsBuilder()
                .append(rollupObjectiveSatisfied, that.rollupObjectiveSatisfied)
                .append(rollupProgressCompletion, that.rollupProgressCompletion)
                .append(objectiveMeasureWeight, that.objectiveMeasureWeight)
                .append(rollupRuleList, that.rollupRuleList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(rollupObjectiveSatisfied)
                .append(rollupProgressCompletion)
                .append(objectiveMeasureWeight)
                .append(rollupRuleList)
                .toHashCode();
    }
}
