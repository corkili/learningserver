package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;

public class MaximumTimeAllowed implements TerminalDataType {

    private TimeInterval maximumTimeAllowed;

    public MaximumTimeAllowed() {
        this.maximumTimeAllowed = new TimeInterval();
    }

    @Override
    public void set(String value) {
        this.maximumTimeAllowed.set(value);
    }

    @Override
    public String get() {
        return this.maximumTimeAllowed.get();
    }

    public TimeInterval getMaximumTimeAllowed() {
        return maximumTimeAllowed;
    }

    public void setMaximumTimeAllowed(TimeInterval maximumTimeAllowed) {
        this.maximumTimeAllowed = maximumTimeAllowed;
    }
}
