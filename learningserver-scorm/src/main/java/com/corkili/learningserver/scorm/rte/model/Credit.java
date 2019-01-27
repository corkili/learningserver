package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Credit implements TerminalDataType {

    private State credit;

    public Credit() {
        credit = new State(new String[]{"credit", "no_credit"});
        credit.setValue("credit");
        registerHandler();
    }

    private void registerHandler() {
        credit.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return credit.set(value);
    }

    @Override
    public ScormResult get() {
        return credit.get();
    }

    public State getCredit() {
        return credit;
    }

    public void setCredit(State credit) {
        this.credit = credit;
    }
}
