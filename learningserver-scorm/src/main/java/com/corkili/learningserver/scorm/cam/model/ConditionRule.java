package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ruleConditions", ruleConditions)
                .append("ruleAction", ruleAction)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConditionRule that = (ConditionRule) o;

        return new EqualsBuilder()
                .append(ruleConditions, that.ruleConditions)
                .append(ruleAction, that.ruleAction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(ruleConditions)
                .append(ruleAction)
                .toHashCode();
    }
}
