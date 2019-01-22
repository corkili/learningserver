package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithMin;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class AudioLevel implements TerminalDataType {

    private Real7WithMin audioLevel;

    public AudioLevel() {
        this.audioLevel = new Real7WithMin(0);
    }

    @Override
    public void set(String value) {
        this.audioLevel.set(value);
    }

    @Override
    public String get() {
        return this.audioLevel.get();
    }

    public Real7WithMin getAudioLevel() {
        return audioLevel;
    }

    public void setAudioLevel(Real7WithMin audioLevel) {
        this.audioLevel = audioLevel;
    }
}
