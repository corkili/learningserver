package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

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
}
