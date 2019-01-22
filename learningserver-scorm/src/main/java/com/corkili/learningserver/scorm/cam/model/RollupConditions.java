package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("conditionCombination", conditionCombination)
                .append("rollupConditionList", rollupConditionList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RollupConditions that = (RollupConditions) o;

        return new EqualsBuilder()
                .append(conditionCombination, that.conditionCombination)
                .append(rollupConditionList, that.rollupConditionList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(conditionCombination)
                .append(rollupConditionList)
                .toHashCode();
    }
}
