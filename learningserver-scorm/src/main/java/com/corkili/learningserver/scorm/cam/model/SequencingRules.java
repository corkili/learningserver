package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class SequencingRules {

    // elements
    private List<ConditionRule> preConditionRuleList; // 0...n
    private List<ConditionRule> exitConditionRuleList; // 0...n
    private List<ConditionRule> postConditionRuleList; // 0...n

}
