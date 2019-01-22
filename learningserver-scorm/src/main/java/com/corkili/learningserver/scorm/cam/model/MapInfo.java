package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("targetObjectiveID", targetObjectiveID)
                .append("readSatisfiedStatus", readSatisfiedStatus)
                .append("readNormalizedMeasure", readNormalizedMeasure)
                .append("writeSatisfiedStatus", writeSatisfiedStatus)
                .append("writeNormalizedMeasure", writeNormalizedMeasure)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MapInfo mapInfo = (MapInfo) o;

        return new EqualsBuilder()
                .append(readSatisfiedStatus, mapInfo.readSatisfiedStatus)
                .append(readNormalizedMeasure, mapInfo.readNormalizedMeasure)
                .append(writeSatisfiedStatus, mapInfo.writeSatisfiedStatus)
                .append(writeNormalizedMeasure, mapInfo.writeNormalizedMeasure)
                .append(targetObjectiveID, mapInfo.targetObjectiveID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(targetObjectiveID)
                .append(readSatisfiedStatus)
                .append(readNormalizedMeasure)
                .append(writeSatisfiedStatus)
                .append(writeNormalizedMeasure)
                .toHashCode();
    }
}
