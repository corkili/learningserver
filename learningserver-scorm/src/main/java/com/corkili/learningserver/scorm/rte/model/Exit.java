package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.WriteOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Exit implements TerminalDataType {

    private State exit;

    public Exit() {
        this.exit = new State(new String[]{"timeout", "suspend", "logout", "normal", ""});
        this.exit.setValue("");
        registerHandler();
    }

    private void registerHandler() {
        exit.registerGetHandler(new WriteOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return exit.set(value);
    }

    @Override
    public ScormResult get() {
        return exit.get();
    }

    public State getExit() {
        return exit;
    }

    public void setExit(State exit) {
        this.exit = exit;
    }
}
