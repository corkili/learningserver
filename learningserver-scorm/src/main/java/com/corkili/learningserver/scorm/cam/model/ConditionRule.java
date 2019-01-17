package com.corkili.learningserver.scorm.cam.model;

public class ConditionRule {

    private RuleConditions ruleConditions; // 1...1
    private RuleAction ruleAction; // 1...1

    public ConditionRule() {
    }

    public RuleConditions getRuleConditions() {
        return ruleConditions;
    }

    public void setRuleConditions(RuleConditions ruleConditions) {
        this.ruleConditions = ruleConditions;
    }

    public RuleAction getRuleAction() {
        return ruleAction;
    }

    public void setRuleAction(RuleAction ruleAction) {
        this.ruleAction = ruleAction;
    }
}
