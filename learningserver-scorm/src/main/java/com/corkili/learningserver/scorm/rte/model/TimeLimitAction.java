package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class TimeLimitAction implements TerminalDataType {

    private State timeLimitAction;

    public TimeLimitAction() {
        this.timeLimitAction
                = new State(new String[]{"exist_message", "continue_message", "exit_no_message", "continue_no_message"});

    }

    @Override
    public void set(String value) {
        this.timeLimitAction.set(value);
    }

    @Override
    public String get() {
        return this.timeLimitAction.get();
    }

    public State getTimeLimitAction() {
        return timeLimitAction;
    }

    public void setTimeLimitAction(State timeLimitAction) {
        this.timeLimitAction = timeLimitAction;
    }
}
