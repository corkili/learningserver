package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class LaunchData implements TerminalDataType {

    private CharacterString launchData;

    public LaunchData() {
        this.launchData = new CharacterString();
        registerHandler();
    }

    private void registerHandler() {
        launchData.registerGetHandler(context -> {
            if (((CharacterString) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler(new ReadOnlyHandler());

    }

    @Override
    public ScormResult set(String value) {
        return this.launchData.set(value);
    }

    @Override
    public ScormResult get() {
        return this.launchData.get();
    }

    public CharacterString getLaunchData() {
        return launchData;
    }

    public void setLaunchData(CharacterString launchData) {
        this.launchData = launchData;
    }
}
