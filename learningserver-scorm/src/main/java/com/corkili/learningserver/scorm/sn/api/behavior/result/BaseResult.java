package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class BaseResult {

    private SequencingException exception;

    public SequencingException getException() {
        return exception;
    }

    public BaseResult setException(SequencingException exception) {
        this.exception = exception;
        return this;
    }
}
