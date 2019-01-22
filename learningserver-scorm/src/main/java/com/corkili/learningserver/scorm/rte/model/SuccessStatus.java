package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class SuccessStatus implements TerminalDataType {

    private State successStatus;

    public SuccessStatus() {
        this.successStatus = new State(new String[]{"passed", "failed", "unknown"});
    }

    @Override
    public void set(String value) {
        this.successStatus.set(value);
    }

    @Override
    public String get() {
        return this.successStatus.get();
    }

    public State getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(State successStatus) {
        this.successStatus = successStatus;
    }
}
