package com.corkili.learningserver.scorm.sn.api.behavior.result;

import com.corkili.learningserver.scorm.sn.api.behavior.rto.EndSequencingSession;

public class OverallSequencingResult extends BaseResult {

    private final boolean success;
    private final boolean exit;
    private EndSequencingSession endSequencingSession;

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

    public EndSequencingSession getEndSequencingSession() {
        return endSequencingSession;
    }

    public OverallSequencingResult setEndSequencingSession(EndSequencingSession endSequencingSession) {
        this.endSequencingSession = endSequencingSession;
        return this;
    }

    @Override
    public OverallSequencingResult setException(SequencingException exception) {
        return (OverallSequencingResult) super.setException(exception);
    }
}
