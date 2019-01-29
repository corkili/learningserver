package com.corkili.learningserver.scorm.sn.model.definition;

public class ObjectiveMap {

    private final String ActivityObjectiveID;
    private final String targetObjectiveID;
    private boolean readObjectiveSatisfiedStatus;
    private boolean writeObjectiveSatisfiedStatus;
    private boolean readObjectiveNormalizedMeasure;
    private boolean writeObjectiveNormalizedMeasure;
    private boolean readRawScore;
    private boolean writeRawScore;
    private boolean readMinScore;
    private boolean writeMinScore;
    private boolean readMaxScore;
    private boolean writeMaxScore;
    private boolean readCompletionStatus;
    private boolean writeCompletionStatus;
    private boolean readProgressMeasure;
    private boolean writeProgressMeasure;

    public ObjectiveMap(String activityObjectiveID, String targetObjectiveID) {
        ActivityObjectiveID = activityObjectiveID;
        this.targetObjectiveID = targetObjectiveID;
        readObjectiveSatisfiedStatus = true;
        writeObjectiveSatisfiedStatus = false;
        readObjectiveNormalizedMeasure = true;
        writeObjectiveNormalizedMeasure = false;
        readRawScore = true;
        writeRawScore = false;
        readMinScore = true;
        writeMinScore = false;
        readMaxScore = true;
        writeMaxScore = false;
        readCompletionStatus = true;
        writeCompletionStatus = false;
        readProgressMeasure = true;
        writeProgressMeasure = false;
    }

    public String getActivityObjectiveID() {
        return ActivityObjectiveID;
    }

    public String getTargetObjectiveID() {
        return targetObjectiveID;
    }

    public boolean isReadObjectiveSatisfiedStatus() {
        return readObjectiveSatisfiedStatus;
    }

    public void setReadObjectiveSatisfiedStatus(boolean readObjectiveSatisfiedStatus) {
        this.readObjectiveSatisfiedStatus = readObjectiveSatisfiedStatus;
    }

    public boolean isWriteObjectiveSatisfiedStatus() {
        return writeObjectiveSatisfiedStatus;
    }

    public void setWriteObjectiveSatisfiedStatus(boolean writeObjectiveSatisfiedStatus) {
        this.writeObjectiveSatisfiedStatus = writeObjectiveSatisfiedStatus;
    }

    public boolean isReadObjectiveNormalizedMeasure() {
        return readObjectiveNormalizedMeasure;
    }

    public void setReadObjectiveNormalizedMeasure(boolean readObjectiveNormalizedMeasure) {
        this.readObjectiveNormalizedMeasure = readObjectiveNormalizedMeasure;
    }

    public boolean isWriteObjectiveNormalizedMeasure() {
        return writeObjectiveNormalizedMeasure;
    }

    public void setWriteObjectiveNormalizedMeasure(boolean writeObjectiveNormalizedMeasure) {
        this.writeObjectiveNormalizedMeasure = writeObjectiveNormalizedMeasure;
    }

    public boolean isReadRawScore() {
        return readRawScore;
    }

    public void setReadRawScore(boolean readRawScore) {
        this.readRawScore = readRawScore;
    }

    public boolean isWriteRawScore() {
        return writeRawScore;
    }

    public void setWriteRawScore(boolean writeRawScore) {
        this.writeRawScore = writeRawScore;
    }

    public boolean isReadMinScore() {
        return readMinScore;
    }

    public void setReadMinScore(boolean readMinScore) {
        this.readMinScore = readMinScore;
    }

    public boolean isWriteMinScore() {
        return writeMinScore;
    }

    public void setWriteMinScore(boolean writeMinScore) {
        this.writeMinScore = writeMinScore;
    }

    public boolean isReadMaxScore() {
        return readMaxScore;
    }

    public void setReadMaxScore(boolean readMaxScore) {
        this.readMaxScore = readMaxScore;
    }

    public boolean isWriteMaxScore() {
        return writeMaxScore;
    }

    public void setWriteMaxScore(boolean writeMaxScore) {
        this.writeMaxScore = writeMaxScore;
    }

    public boolean isReadCompletionStatus() {
        return readCompletionStatus;
    }

    public void setReadCompletionStatus(boolean readCompletionStatus) {
        this.readCompletionStatus = readCompletionStatus;
    }

    public boolean isWriteCompletionStatus() {
        return writeCompletionStatus;
    }

    public void setWriteCompletionStatus(boolean writeCompletionStatus) {
        this.writeCompletionStatus = writeCompletionStatus;
    }

    public boolean isReadProgressMeasure() {
        return readProgressMeasure;
    }

    public void setReadProgressMeasure(boolean readProgressMeasure) {
        this.readProgressMeasure = readProgressMeasure;
    }

    public boolean isWriteProgressMeasure() {
        return writeProgressMeasure;
    }

    public void setWriteProgressMeasure(boolean writeProgressMeasure) {
        this.writeProgressMeasure = writeProgressMeasure;
    }
}
