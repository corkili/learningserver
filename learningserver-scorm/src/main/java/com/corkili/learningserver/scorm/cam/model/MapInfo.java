package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class MapInfo {

    // attributes
    private AnyURI targetObjectiveID; // M
    private boolean readSatisfiedStatus; // O true
    private boolean readNormalizedMeasure; // O true
    private boolean writeSatisfiedStatus; // O false
    private boolean writeNormalizedMeasure; // O false

    public MapInfo() {
        readSatisfiedStatus = true;
        readNormalizedMeasure = true;
        writeSatisfiedStatus = false;
        writeNormalizedMeasure = false;
    }

    public AnyURI getTargetObjectiveID() {
        return targetObjectiveID;
    }

    public void setTargetObjectiveID(AnyURI targetObjectiveID) {
        this.targetObjectiveID = targetObjectiveID;
    }

    public boolean isReadSatisfiedStatus() {
        return readSatisfiedStatus;
    }

    public void setReadSatisfiedStatus(boolean readSatisfiedStatus) {
        this.readSatisfiedStatus = readSatisfiedStatus;
    }

    public boolean isReadNormalizedMeasure() {
        return readNormalizedMeasure;
    }

    public void setReadNormalizedMeasure(boolean readNormalizedMeasure) {
        this.readNormalizedMeasure = readNormalizedMeasure;
    }

    public boolean isWriteSatisfiedStatus() {
        return writeSatisfiedStatus;
    }

    public void setWriteSatisfiedStatus(boolean writeSatisfiedStatus) {
        this.writeSatisfiedStatus = writeSatisfiedStatus;
    }

    public boolean isWriteNormalizedMeasure() {
        return writeNormalizedMeasure;
    }

    public void setWriteNormalizedMeasure(boolean writeNormalizedMeasure) {
        this.writeNormalizedMeasure = writeNormalizedMeasure;
    }
}
