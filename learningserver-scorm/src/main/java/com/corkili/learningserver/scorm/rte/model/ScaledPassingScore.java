package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class ScaledPassingScore implements TerminalDataType {

    private Real7WithRange scaledPassingScore;

    public ScaledPassingScore() {
        this.scaledPassingScore = new Real7WithRange(-1, 1);
        registerHandler();
    }

    private void registerHandler() {
        scaledPassingScore.registerGetHandler(context -> {
            if (((Real7WithRange) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        this.scaledPassingScore.set(value);
        return null;
    }

    @Override
    public ScormResult get() {
        return this.scaledPassingScore.get();
    }

    public Real7WithRange getScaledPassingScore() {
        return scaledPassingScore;
    }

    public void setScaledPassingScore(Real7WithRange scaledPassingScore) {
        this.scaledPassingScore = scaledPassingScore;
    }
}
