package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class LaunchData implements TerminalDataType {

    private CharacterString launchData;

    public LaunchData() {
        this.launchData = new CharacterString();
    }

    @Override
    public void set(String value) {
        this.launchData.set(value);
    }

    @Override
    public String get() {
        return this.launchData.get();
    }

    public CharacterString getLaunchData() {
        return launchData;
    }

    public void setLaunchData(CharacterString launchData) {
        this.launchData = launchData;
    }
}
