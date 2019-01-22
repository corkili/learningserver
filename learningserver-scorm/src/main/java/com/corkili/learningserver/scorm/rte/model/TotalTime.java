package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TimeInterval;

public class TotalTime implements TerminalDataType {

    private TimeInterval totalTime;

    public TotalTime() {
        this.totalTime = new TimeInterval();
    }

    @Override
    public void set(String value) {
        this.totalTime.set(value);
    }

    @Override
    public String get() {
        return this.totalTime.get();
    }

    public TimeInterval getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TimeInterval totalTime) {
        this.totalTime = totalTime;
    }
}
