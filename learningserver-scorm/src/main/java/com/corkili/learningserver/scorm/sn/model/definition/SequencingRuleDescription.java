package com.corkili.learningserver.scorm.sn.model.definition;

import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RuleConditions
 * @see com.corkili.learningserver.scorm.cam.model.ConditionRule
 */
public class SequencingRuleDescription implements DefinitionElementSet {

    private final ConditionType conditionType;
    private final Vocabulary conditionCombination;
    private final List<RuleCondition> ruleConditions;
    private final Vocabulary ruleAction;

    public SequencingRuleDescription(@NotNull ConditionType conditionType) {
        this.conditionType = conditionType;
        conditionCombination = new Vocabulary("All", "All", "Any");
        ruleConditions = new ArrayList<>();
        ruleAction = new Vocabulary("Ignore", conditionType.ruleActionVocabularyTable);
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public Vocabulary getConditionCombination() {
        return conditionCombination;
    }

    public List<RuleCondition> getRuleConditions() {
        return ruleConditions;
    }

    public Vocabulary getRuleAction() {
        return ruleAction;
    }

    public enum ConditionType {
        PRECONDITION("Skip", "Disabled", "Hidden from Choice", "Stop Forward Traversal", "Ignore"),
        POSTCONDITION("Exit Parent", "Exit All", "Retry", "Retry All", "Continue", "Previous", "Ignore"),
        EXITCONDITION("Exit", "Ignore");

        ConditionType(String... ruleActionVocabularyTable) {
            this.ruleActionVocabularyTable = ruleActionVocabularyTable;
        }

        private final String[] ruleActionVocabularyTable;

        public String[] getRuleActionVocabularyTable() {
            return ruleActionVocabularyTable;
        }
    }
}
