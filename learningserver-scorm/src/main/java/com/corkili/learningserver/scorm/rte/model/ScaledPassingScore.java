package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class ScaledPassingScore implements TerminalDataType {

    private Real7WithRange scaledPassingScore;

    public ScaledPassingScore() {
        this.scaledPassingScore = new Real7WithRange(-1, 1);
    }

    @Override
    public void set(String value) {
        this.scaledPassingScore.set(value);
    }

    @Override
    public String get() {
        return this.scaledPassingScore.get();
    }

    public Real7WithRange getScaledPassingScore() {
        return scaledPassingScore;
    }

    public void setScaledPassingScore(Real7WithRange scaledPassingScore) {
        this.scaledPassingScore = scaledPassingScore;
    }
}
