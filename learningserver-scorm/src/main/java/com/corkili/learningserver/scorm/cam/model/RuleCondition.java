package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RuleCondition {

    // attributes
    private Token condition; // M
    private String referencedObjective; // O
    private Decimal measureThreshold; // O [-1.0000,1.0000]
    private Token operator; // O noOp

    public RuleCondition() {
        operator = new Token("noOp");
    }

    public Token getCondition() {
        return condition;
    }

    public void setCondition(Token condition) {
        this.condition = condition;
    }

    public String getReferencedObjective() {
        return referencedObjective;
    }

    public void setReferencedObjective(String referencedObjective) {
        this.referencedObjective = referencedObjective;
    }

    public Decimal getMeasureThreshold() {
        return measureThreshold;
    }

    public void setMeasureThreshold(Decimal measureThreshold) {
        this.measureThreshold = measureThreshold;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }
}
