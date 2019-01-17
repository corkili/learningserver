package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RuleAction {

    private Token action; // M

    public RuleAction() {
    }

    public Token getAction() {
        return action;
    }

    public void setAction(Token action) {
        this.action = action;
    }
}
