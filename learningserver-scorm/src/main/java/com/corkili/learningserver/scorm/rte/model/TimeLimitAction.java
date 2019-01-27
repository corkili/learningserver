package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class TimeLimitAction implements TerminalDataType {

    private State timeLimitAction;

    public TimeLimitAction() {
        this.timeLimitAction
                = new State(new String[]{"exist,message", "continue,message", "exit,no message", "continue,no message"});
        this.timeLimitAction.setValue("continue,no message");
        registerHandler();
    }

    private void registerHandler() {
        timeLimitAction.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.timeLimitAction.set(value);
    }

    @Override
    public ScormResult get() {
        return this.timeLimitAction.get();
    }

    public State getTimeLimitAction() {
        return timeLimitAction;
    }

    public void setTimeLimitAction(State timeLimitAction) {
        this.timeLimitAction = timeLimitAction;
    }
}
