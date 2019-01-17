package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RuleCondition {

    // attributes
    private Token condition; // M
    private String referencedObjective; // O
    private Decimal measureThreshold; // O
    private Token operator; // O noOp

}
