package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;
import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;

public class Sequencing {

    // attributes
    private ID id;
    private IDRef idRef;

    // elements
    private ControlMode controlMode;
    private SequencingRules sequencingRules;
    private LimitConditions limitConditions;
    private AuxiliaryResources auxiliaryResources;
    private RollupRules rollupRules;
    private Objectives objectives;
    private RandomizationControls randomizationControls;
    private DeliveryControls deliveryControls;
    private ConstrainedChoiceConsiderations constrainedChoiceConsiderations;
    private RollupConsiderations rollupConsiderations;
    private AdlseqObjectives adlseqObjectives;

}
