package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class MaximumTimeAllowed implements TerminalDataType {

    private TimeInterval maximumTimeAllowed;

    public MaximumTimeAllowed() {
        this.maximumTimeAllowed = new TimeInterval();
        registerHandler();
    }

    private void registerHandler() {
        maximumTimeAllowed.registerGetHandler(context -> {
            if (((TimeInterval) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.maximumTimeAllowed.set(value);
    }

    @Override
    public ScormResult get() {
        return this.maximumTimeAllowed.get();
    }

    public TimeInterval getMaximumTimeAllowed() {
        return maximumTimeAllowed;
    }

    public void setMaximumTimeAllowed(TimeInterval maximumTimeAllowed) {
        this.maximumTimeAllowed = maximumTimeAllowed;
    }
}
