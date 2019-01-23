package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Entry implements TerminalDataType {

    private State entry;

    public Entry() {
        entry = new State(new String[]{"ab_initio","resume","_nil_"});
        registerHandler();
    }

    private void registerHandler() {
        entry.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return entry.set(value);
    }

    @Override
    public ScormResult get() {
        return entry.get();
    }

    public State getEntry() {
        return entry;
    }

    public void setEntry(State entry) {
        this.entry = entry;
    }
}
