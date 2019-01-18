package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("condition", condition)
                .append("referencedObjective", referencedObjective)
                .append("measureThreshold", measureThreshold)
                .append("operator", operator)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RuleCondition that = (RuleCondition) o;

        return new EqualsBuilder()
                .append(condition, that.condition)
                .append(referencedObjective, that.referencedObjective)
                .append(measureThreshold, that.measureThreshold)
                .append(operator, that.operator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(condition)
                .append(referencedObjective)
                .append(measureThreshold)
                .append(operator)
                .toHashCode();
    }
}
