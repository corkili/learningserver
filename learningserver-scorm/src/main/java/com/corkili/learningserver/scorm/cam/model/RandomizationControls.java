package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RandomizationControls {

    // attributes
    private Token randomizationTiming; // O never
    private NonNegativeInteger selectCount; // O
    private boolean reorderChildren; // O false
    private Token selectionTiming; // O never

    public RandomizationControls() {
        randomizationTiming = new Token("never");
        reorderChildren = false;
        selectionTiming = new Token("never");
    }

    public Token getRandomizationTiming() {
        return randomizationTiming;
    }

    public void setRandomizationTiming(Token randomizationTiming) {
        this.randomizationTiming = randomizationTiming;
    }

    public NonNegativeInteger getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(NonNegativeInteger selectCount) {
        this.selectCount = selectCount;
    }

    public boolean isReorderChildren() {
        return reorderChildren;
    }

    public void setReorderChildren(boolean reorderChildren) {
        this.reorderChildren = reorderChildren;
    }

    public Token getSelectionTiming() {
        return selectionTiming;
    }

    public void setSelectionTiming(Token selectionTiming) {
        this.selectionTiming = selectionTiming;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("randomizationTiming", randomizationTiming)
                .append("selectCount", selectCount)
                .append("reorderChildren", reorderChildren)
                .append("selectionTiming", selectionTiming)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RandomizationControls that = (RandomizationControls) o;

        return new EqualsBuilder()
                .append(reorderChildren, that.reorderChildren)
                .append(randomizationTiming, that.randomizationTiming)
                .append(selectCount, that.selectCount)
                .append(selectionTiming, that.selectionTiming)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(randomizationTiming)
                .append(selectCount)
                .append(reorderChildren)
                .append(selectionTiming)
                .toHashCode();
    }
}
