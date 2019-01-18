package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class Map {

    // attributes
    private AnyURI targetID; // M
    private boolean readSharedData; // O true
    private boolean writeSharedData; // O true

    public Map() {
        readSharedData = true;
        writeSharedData = true;
    }

    public AnyURI getTargetID() {
        return targetID;
    }

    public void setTargetID(AnyURI targetID) {
        this.targetID = targetID;
    }

    public boolean isReadSharedData() {
        return readSharedData;
    }

    public void setReadSharedData(boolean readSharedData) {
        this.readSharedData = readSharedData;
    }

    public boolean isWriteSharedData() {
        return writeSharedData;
    }

    public void setWriteSharedData(boolean writeSharedData) {
        this.writeSharedData = writeSharedData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("targetID", targetID)
                .append("readSharedData", readSharedData)
                .append("writeSharedData", writeSharedData)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        return new EqualsBuilder()
                .append(readSharedData, map.readSharedData)
                .append(writeSharedData, map.writeSharedData)
                .append(targetID, map.targetID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(targetID)
                .append(readSharedData)
                .append(writeSharedData)
                .toHashCode();
    }
}
