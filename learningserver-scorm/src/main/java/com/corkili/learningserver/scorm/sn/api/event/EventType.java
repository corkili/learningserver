package com.corkili.learningserver.scorm.sn.api.event;

public enum  EventType {
    Start(true, false),
    ResumeAll(true, false),
    Continue(true, true),
    Previous(true, true),
    Choose(true, true),
    Jump(false, true),
    Abandon(true, true),
    AbandonAll(true, true),
    SuspendAll(true, true),
    UnqualifiedExit(true, false),
    ExitAll(true, true);

    private final boolean triggeredByLMS;
    private final boolean triggeredBySCO;

    EventType(boolean triggeredByLMS, boolean triggeredBySCO) {
        this.triggeredByLMS = triggeredByLMS;
        this.triggeredBySCO = triggeredBySCO;
    }

    public boolean isTriggeredByLMS() {
        return triggeredByLMS;
    }

    public boolean isTriggeredBySCO() {
        return triggeredBySCO;
    }
}


