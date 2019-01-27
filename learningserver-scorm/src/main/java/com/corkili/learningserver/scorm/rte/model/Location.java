package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Location implements TerminalDataType {

    private CharacterString location;

    public Location() {
        this.location = new CharacterString();
        this.location.setValue("");
        registerHandler();
    }

    private void registerHandler() {
        location.registerGetHandler(context -> {
            if (((CharacterString) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        });
    }

    @Override
    public ScormResult set(String value) {
        return this.location.set(value);
    }

    @Override
    public ScormResult get() {
        return this.location.get();
    }

    public CharacterString getLocation() {
        return location;
    }

    public void setLocation(CharacterString location) {
        this.location = location;
    }
}
