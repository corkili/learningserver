package com.corkili.learningserver.scorm.rte.model;

import java.math.BigDecimal;

import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class CompletionStatus implements TerminalDataType {

    private State completionStatus;
    private CMI containerCMI;

    public CompletionStatus(CMI containerCMI) {
        this.completionStatus = new State(new String[]{"completed", "incomplete", "not_attempted", "unknown"});
        this.completionStatus.setValue("unknown");
        this.containerCMI = containerCMI;
    }

    @Override
    public ScormResult set(String value) {
        return completionStatus.set(value);
    }

    @Override
    public ScormResult get() {
        String evaluateValue = evaluate();
        if (evaluateValue == null) {
            return completionStatus.get();
        } else {
            return new ScormResult(evaluateValue, ScormError.E_0);
        }
    }

    private String evaluate() {
        BigDecimal completionThreshold = containerCMI.getCompletionThreshold().getCompletionThreshold().getValue();
        BigDecimal progressMeasure = containerCMI.getProgressMeasure().getProgressMeasure().getValue();
        String completionStatus = this.completionStatus.getValue();
        if (completionThreshold == null) {
            if (completionStatus == null) {
                return "unknown";
            } else {
                return null;
            }
        } else {
            if (progressMeasure != null) {
                if (progressMeasure.compareTo(completionThreshold) < 0) {
                    return "incomplete";
                } else {
                    return "completed";
                }
            } else {
                return "unknown"; // #9
            }
        }
    }

    public State getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(State completionStatus) {
        this.completionStatus = completionStatus;
    }
}
