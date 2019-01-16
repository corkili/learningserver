package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RuleCondition {

    // attributes
    private String referencedObjective;
    private Decimal measureThreshold;
    private Token operator;
    private Token condition;

}
