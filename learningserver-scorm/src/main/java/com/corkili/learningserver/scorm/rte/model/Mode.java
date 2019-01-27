package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

// TODO RTE-4-93
public class Mode implements TerminalDataType {

    private State mode;

    public Mode() {
        this.mode = new State(new String[]{"browse", "normal", "review"});
        this.mode.setValue("normal");
        registerHandler();
    }

    private void registerHandler() {
        mode.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.mode.set(value);
    }

    @Override
    public ScormResult get() {
        return this.mode.get();
    }

    public State getMode() {
        return mode;
    }

    public void setMode(State mode) {
        this.mode = mode;
    }
}
