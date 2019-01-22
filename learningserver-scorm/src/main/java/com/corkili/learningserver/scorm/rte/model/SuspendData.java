package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class SuspendData implements TerminalDataType {

    private CharacterString suspendData;

    public SuspendData() {
        this.suspendData = new CharacterString();
    }

    @Override
    public void set(String value) {
        this.suspendData.set(value);
    }

    @Override
    public String get() {
        return this.suspendData.get();
    }

    public CharacterString getSuspendData() {
        return suspendData;
    }

    public void setSuspendData(CharacterString suspendData) {
        this.suspendData = suspendData;
    }
}
