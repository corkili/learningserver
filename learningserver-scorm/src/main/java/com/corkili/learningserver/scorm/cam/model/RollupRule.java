package com.corkili.learningserver.scorm.cam.model;

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
}
