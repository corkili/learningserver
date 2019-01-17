package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RuleConditions {

    // attributes
    private Token conditionCombination; // O all

    // elements
    private List<RuleCondition> ruleConditionList; // 1...n

    public RuleConditions() {
        conditionCombination = new Token("all");
        ruleConditionList = new ArrayList<>();
    }

    public Token getConditionCombination() {
        return conditionCombination;
    }

    public void setConditionCombination(Token conditionCombination) {
        this.conditionCombination = conditionCombination;
    }

    public List<RuleCondition> getRuleConditionList() {
        return ruleConditionList;
    }

    public void setRuleConditionList(List<RuleCondition> ruleConditionList) {
        this.ruleConditionList = ruleConditionList;
    }
}
