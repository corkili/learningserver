package com.corkili.learningserver.scorm.sn.api.event;

public class NavigationEvent {

    private final EventType type;

    private final String targetActivityID;

    public NavigationEvent(EventType type) {
        this(type, null);
    }

    public NavigationEvent(EventType type, String targetActivityID) {
        this.type = type;
        this.targetActivityID = targetActivityID;
    }

    public EventType getType() {
        return type;
    }

    public String getTargetActivityID() {
        return targetActivityID;
    }
}
