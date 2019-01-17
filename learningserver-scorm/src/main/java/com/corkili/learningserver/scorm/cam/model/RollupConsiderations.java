package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupConsiderations {

    // attributes
    private Token requiredForSatisfied; // O always
    private Token requiredForNotSatisfied; // O always
    private Token requiredForCompleted; // O always
    private Token requiredForIncomplete; // O always
    private boolean measureSatisfactionIfActive; // O true

}
