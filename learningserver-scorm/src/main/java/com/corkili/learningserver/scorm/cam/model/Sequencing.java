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

    public Sequencing() {
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public IDRef getIdRef() {
        return idRef;
    }

    public void setIdRef(IDRef idRef) {
        this.idRef = idRef;
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }

    public SequencingRules getSequencingRules() {
        return sequencingRules;
    }

    public void setSequencingRules(SequencingRules sequencingRules) {
        this.sequencingRules = sequencingRules;
    }

    public LimitConditions getLimitConditions() {
        return limitConditions;
    }

    public void setLimitConditions(LimitConditions limitConditions) {
        this.limitConditions = limitConditions;
    }

    public RollupRules getRollupRules() {
        return rollupRules;
    }

    public void setRollupRules(RollupRules rollupRules) {
        this.rollupRules = rollupRules;
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives objectives) {
        this.objectives = objectives;
    }

    public RandomizationControls getRandomizationControls() {
        return randomizationControls;
    }

    public void setRandomizationControls(RandomizationControls randomizationControls) {
        this.randomizationControls = randomizationControls;
    }

    public DeliveryControls getDeliveryControls() {
        return deliveryControls;
    }

    public void setDeliveryControls(DeliveryControls deliveryControls) {
        this.deliveryControls = deliveryControls;
    }

    public ConstrainedChoiceConsiderations getConstrainedChoiceConsiderations() {
        return constrainedChoiceConsiderations;
    }

    public void setConstrainedChoiceConsiderations(ConstrainedChoiceConsiderations constrainedChoiceConsiderations) {
        this.constrainedChoiceConsiderations = constrainedChoiceConsiderations;
    }

    public RollupConsiderations getRollupConsiderations() {
        return rollupConsiderations;
    }

    public void setRollupConsiderations(RollupConsiderations rollupConsiderations) {
        this.rollupConsiderations = rollupConsiderations;
    }

    public AdlseqObjectives getAdlseqObjectives() {
        return adlseqObjectives;
    }

    public void setAdlseqObjectives(AdlseqObjectives adlseqObjectives) {
        this.adlseqObjectives = adlseqObjectives;
    }
}
