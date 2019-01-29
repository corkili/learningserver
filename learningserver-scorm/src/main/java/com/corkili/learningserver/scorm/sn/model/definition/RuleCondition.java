package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;
import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RuleCondition
 */
public class RuleCondition {

    private final Vocabulary ruleCondition;
    private String referencedObjective;
    private final DecimalWithRange measureThreshold;
    private final Vocabulary operator;

    public RuleCondition() {
        ruleCondition = new Vocabulary("Always", "Satisfied", "Objective Status Known",
                "Objective Measure Known", "Objective Measure Greater Than", "Objective Measure Less Than",
                "Completed", "Activity Progress Known", "Attempted", "Attempt Limit Exceeded", "Always");
        measureThreshold = new DecimalWithRange(0, -1, 1, 4);
        operator = new Vocabulary("NO-OP", "NO-OP", "Not");
    }

    public Vocabulary getRuleCondition() {
        return ruleCondition;
    }

    public String getReferencedObjective() {
        return referencedObjective;
    }

    public void setReferencedObjective(String referencedObjective) {
        this.referencedObjective = referencedObjective;
    }

    public DecimalWithRange getMeasureThreshold() {
        return measureThreshold;
    }

    public Vocabulary getOperator() {
        return operator;
    }
}
