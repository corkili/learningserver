package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class OverallSequencingResult extends BaseResult {

    private final boolean success;
    private final boolean exit;
    private Boolean endSequencingSession;

    public OverallSequencingResult(boolean success) {
        this.success = success;
        this.exit = false;
    }

    public OverallSequencingResult(boolean success, boolean exit) {
        this.success = success;
        this.exit = exit;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isExit() {
        return exit;
    }

    public Boolean getEndSequencingSession() {
        return endSequencingSession;
    }

    public OverallSequencingResult setEndSequencingSession(Boolean endSequencingSession) {
        this.endSequencingSession = endSequencingSession;
        return this;
    }

    @Override
    public OverallSequencingResult setException(SequencingException exception) {
        return (OverallSequencingResult) super.setException(exception);
    }
}
