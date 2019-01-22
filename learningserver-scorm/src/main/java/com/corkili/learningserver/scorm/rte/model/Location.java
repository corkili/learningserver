package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Location implements TerminalDataType {

    private CharacterString location;

    public Location() {
        this.location = new CharacterString();
    }

    @Override
    public void set(String value) {
        this.location.set(value);
    }

    @Override
    public String get() {
        return this.location.get();
    }

    public CharacterString getLocation() {
        return location;
    }

    public void setLocation(CharacterString location) {
        this.location = location;
    }
}
