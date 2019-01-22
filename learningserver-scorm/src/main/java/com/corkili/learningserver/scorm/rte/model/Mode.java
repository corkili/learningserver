package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

// TODO RTE-4-93
public class Mode implements TerminalDataType {

    private State mode;

    public Mode() {
        this.mode = new State(new String[]{"browse", "normal", "review"});
    }

    @Override
    public void set(String value) {
        this.mode.set(value);
    }

    @Override
    public String get() {
        return this.mode.get();
    }

    public State getMode() {
        return mode;
    }

    public void setMode(State mode) {
        this.mode = mode;
    }
}
