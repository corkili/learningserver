package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupCondition {

    // attributes
    private Token condition; // M
    private Token operator; // O noOp

    public RollupCondition() {
        operator = new Token("noOp");
    }

    public Token getCondition() {
        return condition;
    }

    public void setCondition(Token condition) {
        this.condition = condition;
    }

    public Token getOperator() {
        return operator;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }
}
