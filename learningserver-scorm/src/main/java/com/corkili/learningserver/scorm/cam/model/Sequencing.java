package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;
import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;

public class Sequencing {

    // attributes
    private ID id; // O
    private IDRef idRef; // O

    // elements
    private ControlMode controlMode; // 0...1
    private SequencingRules sequencingRules; // 0...1
    private LimitConditions limitConditions; // 0...1
    private RollupRules rollupRules; // 0...1
    private Objectives objectives; // 0...1
    private RandomizationControls randomizationControls; // 0...1
    private DeliveryControls deliveryControls; // // 0...1
    private ConstrainedChoiceConsiderations constrainedChoiceConsiderations; // 0...1
    private RollupConsiderations rollupConsiderations; // 0...1
    private AdlseqObjectives adlseqObjectives; // 0...1
//  private AuxiliaryResources auxiliaryResources; // don't implementation

}
