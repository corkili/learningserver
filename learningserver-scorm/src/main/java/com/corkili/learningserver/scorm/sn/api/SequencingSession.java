package com.corkili.learningserver.scorm.sn.api;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SequencingSession {

    private final String sessionID;

    private State state;

    public SequencingSession() {
        this.sessionID = UUID.randomUUID().toString();
        this.state = State.INITIALIZED;
    }

    public void establish() {
        state = State.ESTABLISHED;
    }

    public void suspend() {
        state = State.SUSPENDED;
    }

    public void close() {
        state = State.CLOSED;
    }

    public boolean isInitialized() {
        return state == State.INITIALIZED;
    }

    public boolean isEstablished() {
        return state == State.ESTABLISHED;
    }

    public boolean isSuspended() {
        return state == State.SUSPENDED;
    }

    public boolean isClosed() {
        return state == State.CLOSED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SequencingSession that = (SequencingSession) o;

        return new EqualsBuilder()
                .append(sessionID, that.sessionID)
                .append(state, that.state)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sessionID)
                .append(state)
                .toHashCode();
    }

    public enum State {
        INITIALIZED,
        ESTABLISHED,
        SUSPENDED,
        CLOSED
    }

}
