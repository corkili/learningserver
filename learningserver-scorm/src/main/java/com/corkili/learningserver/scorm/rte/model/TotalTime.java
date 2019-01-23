package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class TotalTime implements TerminalDataType {

    private TimeInterval totalTime;

    public TotalTime() {
        this.totalTime = new TimeInterval();
        registerHandler();
    }

    private void registerHandler() {
        totalTime.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.totalTime.set(value);
    }

    @Override
    public ScormResult get() {
        return this.totalTime.get();
    }

    public TimeInterval getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TimeInterval totalTime) {
        this.totalTime = totalTime;
    }
}
