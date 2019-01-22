package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupRule {

    // attributes
    private Token childActivitySet; // O all
    private NonNegativeInteger minimumCount; // O 0
    private Decimal minimumPercent; // O 0.0000 [0.0000,1.0000]

    // elements
    private RollupConditions rollupConditions; // 1...1
    private Token rollupAction; // 1...1

    public RollupRule() {
        childActivitySet = new Token("all");
        minimumCount = new NonNegativeInteger("0");
        minimumPercent = new Decimal("0.0000", 4);
    }

    public Token getChildActivitySet() {
        return childActivitySet;
    }

    public void setChildActivitySet(Token childActivitySet) {
        this.childActivitySet = childActivitySet;
    }

    public NonNegativeInteger getMinimumCount() {
        return minimumCount;
    }

    public void setMinimumCount(NonNegativeInteger minimumCount) {
        this.minimumCount = minimumCount;
    }

    public Decimal getMinimumPercent() {
        return minimumPercent;
    }

    public void setMinimumPercent(Decimal minimumPercent) {
        this.minimumPercent = minimumPercent;
    }

    public RollupConditions getRollupConditions() {
        return rollupConditions;
    }

    public void setRollupConditions(RollupConditions rollupConditions) {
        this.rollupConditions = rollupConditions;
    }

    public Token getRollupAction() {
        return rollupAction;
    }

    public void setRollupAction(Token rollupAction) {
        this.rollupAction = rollupAction;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("childActivitySet", childActivitySet)
                .append("minimumCount", minimumCount)
                .append("minimumPercent", minimumPercent)
                .append("rollupConditions", rollupConditions)
                .append("rollupAction", rollupAction)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RollupRule that = (RollupRule) o;

        return new EqualsBuilder()
                .append(childActivitySet, that.childActivitySet)
                .append(minimumCount, that.minimumCount)
                .append(minimumPercent, that.minimumPercent)
                .append(rollupConditions, that.rollupConditions)
                .append(rollupAction, that.rollupAction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(childActivitySet)
                .append(minimumCount)
                .append(minimumPercent)
                .append(rollupConditions)
                .append(rollupAction)
                .toHashCode();
    }
}
