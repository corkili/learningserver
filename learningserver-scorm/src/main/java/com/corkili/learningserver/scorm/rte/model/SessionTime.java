package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;

public class SessionTime implements TerminalDataType {

    private TimeInterval sessionTime;

    public SessionTime() {
        this.sessionTime = new TimeInterval();
    }

    @Override
    public void set(String value) {
        this.sessionTime.set(value);
    }

    @Override
    public String get() {
        return this.sessionTime.get();
    }

    public TimeInterval getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(TimeInterval sessionTime) {
        this.sessionTime = sessionTime;
    }
}
