package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("action", action)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RuleAction that = (RuleAction) o;

        return new EqualsBuilder()
                .append(action, that.action)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(action)
                .toHashCode();
    }
}
