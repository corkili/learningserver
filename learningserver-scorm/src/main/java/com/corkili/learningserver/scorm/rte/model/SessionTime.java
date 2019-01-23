package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;
import com.corkili.learningserver.scorm.rte.model.handler.WriteOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class SessionTime implements TerminalDataType {

    private TimeInterval sessionTime;

    public SessionTime() {
        this.sessionTime = new TimeInterval();
        registerHandler();
    }

    private void registerHandler() {
        sessionTime.registerGetHandler(new WriteOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.sessionTime.set(value);
    }

    @Override
    public ScormResult get() {
        return this.sessionTime.get();
    }

    public TimeInterval getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(TimeInterval sessionTime) {
        this.sessionTime = sessionTime;
    }
}
