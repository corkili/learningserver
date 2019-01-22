package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Entry implements TerminalDataType {

    private State entry;

    public Entry() {
        entry = new State(new String[]{"ab_initio","resume","_nil_"});
    }

    @Override
    public void set(String value) {
        entry.set(value);
    }

    @Override
    public String get() {
        return entry.get();
    }

    public State getEntry() {
        return entry;
    }

    public void setEntry(State entry) {
        this.entry = entry;
    }
}
