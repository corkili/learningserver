package com.corkili.learningserver.scorm.rte.api;

import java.util.UUID;

public class CommunicationSession {

    private String sessionID;

    private State state;

    CommunicationSession() {
        sessionID = UUID.randomUUID().toString();
        state = State.NOT_INITIALIZED;
    }

    String getSessionID() {
        return sessionID;
    }

    boolean isNotInitialized() {
        return state.equals(State.NOT_INITIALIZED);
    }

    boolean isRunning() {
        return state.equals(State.RUNNING);
    }

    boolean isTerminated() {
        return state.equals(State.TERMINATED);
    }

    void switchState(State state) {
        if (state != null) {
            this.state = state;
        }
    }

    public enum State {
        NOT_INITIALIZED,
        RUNNING,
        TERMINATED
    }

}
