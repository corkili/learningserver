package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupRule {

    // attributes
    private Token childActivitySet; // O all
    private int minimumCount; // O 0
    private Decimal minimumPercent; // O 0.0000

    // elements
    private RollupConditions rollupConditions; // 1...1
    private Token rollupAction; // 1...1

}
