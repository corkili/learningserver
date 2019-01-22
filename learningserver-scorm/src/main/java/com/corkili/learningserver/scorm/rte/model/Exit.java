package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Exit implements TerminalDataType {

    private State exit;

    public Exit() {
        this.exit = new State(new String[]{"timeout", "suspend", "logout", "normal", "_nil_"});
    }

    @Override
    public void set(String value) {
        exit.set(value);
    }

    @Override
    public String get() {
        return exit.get();
    }

    public State getExit() {
        return exit;
    }

    public void setExit(State exit) {
        this.exit = exit;
    }
}
