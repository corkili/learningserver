package com.corkili.learningserver.scorm.rte.api;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class LearnerSession {

    private String sessionID;

    private State state;

    private Map<String, CommunicationSession> communicationSessionMap;

    private CommunicationSession currentCommunicationSession;

    LearnerSession() {
        sessionID = UUID.randomUUID().toString();
        state = State.NOT_OPEN;
        communicationSessionMap = new ConcurrentHashMap<>();
    }

    String getSessionID() {
        return sessionID;
    }

    boolean isNotOpen() {
        return state.equals(State.NOT_OPEN);
    }

    boolean isOpen() {
        return state.equals(State.OPEN);
    }

    boolean isClosed() {
        return state.equals(State.CLOSED);
    }

    boolean isCommunicationNotInitialized() {
        return state == State.OPEN && currentCommunicationSession != null && currentCommunicationSession.isNotInitialized();
    }

    boolean isCommunicationRunning() {
        return state == State.OPEN && currentCommunicationSession != null && currentCommunicationSession.isRunning();
    }

    boolean isCommunicationTerminated() {
        return state == State.OPEN && currentCommunicationSession != null && currentCommunicationSession.isTerminated();
    }

    ScormResult initialize() {
        if (currentCommunicationSession == null && state == State.OPEN) {
            currentCommunicationSession = new CommunicationSession();
            communicationSessionMap.put(currentCommunicationSession.getSessionID(), currentCommunicationSession);
        } else {
            return new ScormResult("false", ScormError.E_102);
        }
        if (currentCommunicationSession.isNotInitialized()) {
            currentCommunicationSession.switchState(CommunicationSession.State.RUNNING);
            return new ScormResult("true", ScormError.E_0);
        }
        if (currentCommunicationSession.isRunning()) {
            return new ScormResult("false", ScormError.E_103);
        }
        if (currentCommunicationSession.isTerminated()) {
            return new ScormResult("false", ScormError.E_104);
        }
        return new ScormResult("false", ScormError.E_102);
    }

    ScormResult terminate() {
        if (currentCommunicationSession == null) {
            return new ScormResult("false", ScormError.E_111);
        }
        if (currentCommunicationSession.isNotInitialized()) {
            return new ScormResult("false", ScormError.E_112);
        }
        if (currentCommunicationSession.isRunning()) {
            currentCommunicationSession.switchState(CommunicationSession.State.TERMINATED);
            currentCommunicationSession = null;
            return new ScormResult("true", ScormError.E_0);
        }
        if (currentCommunicationSession.isTerminated()) {
            return new ScormResult("false", ScormError.E_113);
        }
        return new ScormResult("false", ScormError.E_111);
    }

    void open() {
        state = State.OPEN;
    }

     void close() {
        if (currentCommunicationSession != null) {
            currentCommunicationSession.switchState(CommunicationSession.State.TERMINATED);
            communicationSessionMap.put(currentCommunicationSession.getSessionID(), currentCommunicationSession);
            currentCommunicationSession = null;
        }
        state = State.CLOSED;
     }

    enum State {
        NOT_OPEN,
        OPEN,
        CLOSED
    }

}
