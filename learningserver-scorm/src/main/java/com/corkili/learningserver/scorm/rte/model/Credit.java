package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Credit implements TerminalDataType {

    private State credit;

    public Credit() {
        credit = new State(new String[]{"credit", "no_credit"});
    }

    @Override
    public void set(String value) {
        credit.set(value);
    }

    @Override
    public String get() {
        return credit.get();
    }

    public State getCredit() {
        return credit;
    }

    public void setCredit(State credit) {
        this.credit = credit;
    }
}
