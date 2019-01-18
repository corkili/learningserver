package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("conditionCombination", conditionCombination)
                .append("ruleConditionList", ruleConditionList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RuleConditions that = (RuleConditions) o;

        return new EqualsBuilder()
                .append(conditionCombination, that.conditionCombination)
                .append(ruleConditionList, that.ruleConditionList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conditionCombination)
                .append(ruleConditionList)
                .toHashCode();
    }
}
