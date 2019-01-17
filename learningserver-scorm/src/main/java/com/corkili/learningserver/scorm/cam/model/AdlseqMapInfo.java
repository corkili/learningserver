package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class AdlseqMapInfo {

    // attributes
    private AnyURI targetObjectiveID; // M
    private boolean readRawScore; // O true
    private boolean readMinScore; // O true
    private boolean readMaxScore; // O true
    private boolean readCompletionStatus; // O true
    private boolean readProgressMeasure; // O true
    private boolean writeRawScore; // O false
    private boolean writeMinScore; // O false
    private boolean writeMaxScore; // O false
    private boolean writeCompletionStatus; // O false
    private boolean writeProgressMeasure; // O false

}
