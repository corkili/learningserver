package com.corkili.learningserver.scorm.sn.api;

public class AttemptContext {

    private final AttemptContext parentAttemptContext;

    public AttemptContext(AttemptContext parentAttemptContext) {
        this.parentAttemptContext = parentAttemptContext;
    }

    public AttemptContext getParentAttemptContext() {
        return parentAttemptContext;
    }
}
