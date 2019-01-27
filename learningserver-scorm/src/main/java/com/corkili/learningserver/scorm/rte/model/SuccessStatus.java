package com.corkili.learningserver.scorm.rte.model;

import java.math.BigDecimal;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class SuccessStatus implements TerminalDataType {

    private State successStatus;

    private CMI containerCMI;

    public SuccessStatus(CMI containerCMI) {
        this.successStatus = new State(new String[]{"passed", "failed", "unknown"});
        this.successStatus.setValue("unknown");
        this.containerCMI = containerCMI;
    }

    @Override
    public ScormResult set(String value) {
        return this.successStatus.set(value);
    }

    @Override
    public ScormResult get() {
        String evaluateValue = evaluate();
        if (evaluateValue == null) {
            return successStatus.get();
        } else {
            return new ScormResult(evaluateValue, ScormError.E_0);
        }
    }

    private String evaluate() {
        BigDecimal scaledPassingScore = containerCMI.getScaledPassingScore().getScaledPassingScore().getValue();
        BigDecimal scaledScore = containerCMI.getScore().getScaled().getValue();
        String successStatus = this.successStatus.getValue();
        if (scaledPassingScore == null) {
            if (successStatus == null) {
                return "unknown";
            } else {
                return null;
            }
        } else {
            if (scaledScore != null) {
                if (scaledScore.compareTo(scaledPassingScore) < 0) {
                    return "failed";
                } else {
                    return "passed";
                }
            } else {
                return "unknown";
            }
        }
    }

    public State getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(State successStatus) {
        this.successStatus = successStatus;
    }
}
