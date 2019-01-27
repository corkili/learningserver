package com.corkili.learningserver.scorm.rte.model;

import java.math.BigDecimal;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithMin;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class AudioLevel implements TerminalDataType {

    private Real7WithMin audioLevel;

    public AudioLevel() {
        this.audioLevel = new Real7WithMin(0);
        this.audioLevel.setValue(new BigDecimal(0).setScale(7, BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public ScormResult set(String value) {
        return this.audioLevel.set(value);
    }

    @Override
    public ScormResult get() {
        return this.audioLevel.get();
    }

    public Real7WithMin getAudioLevel() {
        return audioLevel;
    }

    public void setAudioLevel(Real7WithMin audioLevel) {
        this.audioLevel = audioLevel;
    }
}
