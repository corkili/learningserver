package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class SuspendData implements TerminalDataType {

    private CharacterString suspendData;

    public SuspendData() {
        this.suspendData = new CharacterString();
        registerHandler();
    }

    private void registerHandler() {
        suspendData.registerGetHandler(context -> {
            if (((CharacterString) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        });
    }

    @Override
    public ScormResult set(String value) {
        return this.suspendData.set(value);
    }

    @Override
    public ScormResult get() {
        return this.suspendData.get();
    }

    public CharacterString getSuspendData() {
        return suspendData;
    }

    public void setSuspendData(CharacterString suspendData) {
        this.suspendData = suspendData;
    }
}
