package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SequencingRules {

    // elements
    private List<ConditionRule> preConditionRuleList; // 0...n
    private List<ConditionRule> exitConditionRuleList; // 0...n
    private List<ConditionRule> postConditionRuleList; // 0...n

    public SequencingRules() {
        preConditionRuleList = new ArrayList<>();
        exitConditionRuleList = new ArrayList<>();
        postConditionRuleList = new ArrayList<>();
    }

    public List<ConditionRule> getPreConditionRuleList() {
        return preConditionRuleList;
    }

    public void setPreConditionRuleList(List<ConditionRule> preConditionRuleList) {
        this.preConditionRuleList = preConditionRuleList;
    }

    public List<ConditionRule> getExitConditionRuleList() {
        return exitConditionRuleList;
    }

    public void setExitConditionRuleList(List<ConditionRule> exitConditionRuleList) {
        this.exitConditionRuleList = exitConditionRuleList;
    }

    public List<ConditionRule> getPostConditionRuleList() {
        return postConditionRuleList;
    }

    public void setPostConditionRuleList(List<ConditionRule> postConditionRuleList) {
        this.postConditionRuleList = postConditionRuleList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("preConditionRuleList", preConditionRuleList)
                .append("exitConditionRuleList", exitConditionRuleList)
                .append("postConditionRuleList", postConditionRuleList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SequencingRules that = (SequencingRules) o;

        return new EqualsBuilder()
                .append(preConditionRuleList, that.preConditionRuleList)
                .append(exitConditionRuleList, that.exitConditionRuleList)
                .append(postConditionRuleList, that.postConditionRuleList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(preConditionRuleList)
                .append(exitConditionRuleList)
                .append(postConditionRuleList)
                .toHashCode();
    }
}
