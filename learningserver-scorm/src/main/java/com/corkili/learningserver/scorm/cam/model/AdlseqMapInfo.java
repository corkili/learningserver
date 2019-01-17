package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class AdlseqMapInfo {

    // attributes
    private AnyURI targetObjectiveID; // M
    private boolean readRawScore; // O true
    private boolean readMinScore; // O true
    private boolean readMaxScore; // O true
    private boolean readCompletionStatus; // O true
    private boolean readProgressMeasure; // O true
    private boolean writeRawScore; // O false
    private boolean writeMinScore; // O false
    private boolean writeMaxScore; // O false
    private boolean writeCompletionStatus; // O false
    private boolean writeProgressMeasure; // O false

    public AdlseqMapInfo() {
        readRawScore = true;
        readMinScore = true;
        readMaxScore = true;
        readCompletionStatus = true;
        readProgressMeasure = true;
        writeRawScore = false;
        writeMinScore = false;
        writeMaxScore = false;
        writeCompletionStatus = false;
        writeProgressMeasure = false;
    }

    public AnyURI getTargetObjectiveID() {
        return targetObjectiveID;
    }

    public void setTargetObjectiveID(AnyURI targetObjectiveID) {
        this.targetObjectiveID = targetObjectiveID;
    }

    public boolean isReadRawScore() {
        return readRawScore;
    }

    public void setReadRawScore(boolean readRawScore) {
        this.readRawScore = readRawScore;
    }

    public boolean isReadMinScore() {
        return readMinScore;
    }

    public void setReadMinScore(boolean readMinScore) {
        this.readMinScore = readMinScore;
    }

    public boolean isReadMaxScore() {
        return readMaxScore;
    }

    public void setReadMaxScore(boolean readMaxScore) {
        this.readMaxScore = readMaxScore;
    }

    public boolean isReadCompletionStatus() {
        return readCompletionStatus;
    }

    public void setReadCompletionStatus(boolean readCompletionStatus) {
        this.readCompletionStatus = readCompletionStatus;
    }

    public boolean isReadProgressMeasure() {
        return readProgressMeasure;
    }

    public void setReadProgressMeasure(boolean readProgressMeasure) {
        this.readProgressMeasure = readProgressMeasure;
    }

    public boolean isWriteRawScore() {
        return writeRawScore;
    }

    public void setWriteRawScore(boolean writeRawScore) {
        this.writeRawScore = writeRawScore;
    }

    public boolean isWriteMinScore() {
        return writeMinScore;
    }

    public void setWriteMinScore(boolean writeMinScore) {
        this.writeMinScore = writeMinScore;
    }

    public boolean isWriteMaxScore() {
        return writeMaxScore;
    }

    public void setWriteMaxScore(boolean writeMaxScore) {
        this.writeMaxScore = writeMaxScore;
    }

    public boolean isWriteCompletionStatus() {
        return writeCompletionStatus;
    }

    public void setWriteCompletionStatus(boolean writeCompletionStatus) {
        this.writeCompletionStatus = writeCompletionStatus;
    }

    public boolean isWriteProgressMeasure() {
        return writeProgressMeasure;
    }

    public void setWriteProgressMeasure(boolean writeProgressMeasure) {
        this.writeProgressMeasure = writeProgressMeasure;
    }
}
