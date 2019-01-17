package com.corkili.learningserver.scorm.cam.model;

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
}
