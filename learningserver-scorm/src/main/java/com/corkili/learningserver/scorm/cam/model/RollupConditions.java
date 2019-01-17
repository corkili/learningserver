package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupConditions {

    // attributes
    private Token conditionCombination; // O all

    // elements
    private List<RollupCondition> rollupConditionList; // 1...n

    public RollupConditions() {
        conditionCombination = new Token("all");
        rollupConditionList = new ArrayList<>();
    }

    public Token getConditionCombination() {
        return conditionCombination;
    }

    public void setConditionCombination(Token conditionCombination) {
        this.conditionCombination = conditionCombination;
    }

    public List<RollupCondition> getRollupConditionList() {
        return rollupConditionList;
    }

    public void setRollupConditionList(List<RollupCondition> rollupConditionList) {
        this.rollupConditionList = rollupConditionList;
    }
}
