package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

// TODO RTE-4-35 (page 95)
public class CompletionStatus implements TerminalDataType {

    private State completionStatus;

    public CompletionStatus() {
        this.completionStatus = new State(new String[]{"completed", "incomplete", "not_attempted", "unknown"});
    }

    @Override
    public void set(String value) {
        completionStatus.set(value);
    }

    @Override
    public String get() {
        return completionStatus.get();
    }

    public State getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(State completionStatus) {
        this.completionStatus = completionStatus;
    }
}
