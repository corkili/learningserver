package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupRule {

    // attributes
    private Token childActivitySet;
    private int minimumCount;
    private Decimal minimumPercent;

    // elements
    private RollupConditions rollupConditions;
    private Token rollupAction;

}
