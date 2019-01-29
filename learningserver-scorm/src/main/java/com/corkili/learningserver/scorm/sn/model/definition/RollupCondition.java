package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RollupCondition
 */
public class RollupCondition {

    private final Vocabulary rollupCondition;
    private final Vocabulary operator;

    public RollupCondition() {
        rollupCondition = new Vocabulary("Never", "Satisfied", "Objective Status Known",
                "Objective Measure Known", "Completed", "Activity Progress Known", "Attempted", "Attempt Limit Exceeded",
                "Never");
        operator = new Vocabulary("NO-OP", "NO-OP", "Not");
    }

    public Vocabulary getRollupCondition() {
        return rollupCondition;
    }

    public Vocabulary getOperator() {
        return operator;
    }
}
