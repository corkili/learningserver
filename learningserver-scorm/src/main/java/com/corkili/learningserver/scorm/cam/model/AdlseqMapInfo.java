package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("targetObjectiveID", targetObjectiveID)
                .append("readRawScore", readRawScore)
                .append("readMinScore", readMinScore)
                .append("readMaxScore", readMaxScore)
                .append("readCompletionStatus", readCompletionStatus)
                .append("readProgressMeasure", readProgressMeasure)
                .append("writeRawScore", writeRawScore)
                .append("writeMinScore", writeMinScore)
                .append("writeMaxScore", writeMaxScore)
                .append("writeCompletionStatus", writeCompletionStatus)
                .append("writeProgressMeasure", writeProgressMeasure)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdlseqMapInfo that = (AdlseqMapInfo) o;

        return new EqualsBuilder()
                .append(readRawScore, that.readRawScore)
                .append(readMinScore, that.readMinScore)
                .append(readMaxScore, that.readMaxScore)
                .append(readCompletionStatus, that.readCompletionStatus)
                .append(readProgressMeasure, that.readProgressMeasure)
                .append(writeRawScore, that.writeRawScore)
                .append(writeMinScore, that.writeMinScore)
                .append(writeMaxScore, that.writeMaxScore)
                .append(writeCompletionStatus, that.writeCompletionStatus)
                .append(writeProgressMeasure, that.writeProgressMeasure)
                .append(targetObjectiveID, that.targetObjectiveID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(targetObjectiveID)
                .append(readRawScore)
                .append(readMinScore)
                .append(readMaxScore)
                .append(readCompletionStatus)
                .append(readProgressMeasure)
                .append(writeRawScore)
                .append(writeMinScore)
                .append(writeMaxScore)
                .append(writeCompletionStatus)
                .append(writeProgressMeasure)
                .toHashCode();
    }
}
