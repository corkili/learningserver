package com.corkili.learningserver.scorm.rte.model.result;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;

public class ScormResult {

    private String returnValue;

    private ScormError error;

    private String diagnostic;

    public ScormResult(@NotNull String returnValue, @NotNull ScormError error) {
        this(returnValue, error, error.getMsg());
    }

    public ScormResult(@NotNull String returnValue, @NotNull ScormError error, @NotNull String diagnostic) {
        this.returnValue = returnValue;
        this.error = error;
        this.diagnostic = diagnostic;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public ScormResult setReturnValue(@NotNull String returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public ScormError getError() {
        return error;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public ScormResult setDiagnostic(@NotNull String diagnostic) {
        this.diagnostic = diagnostic;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("returnValue", returnValue)
                .append("error", error)
                .append("diagnostic", diagnostic)
                .toString();
    }
}
