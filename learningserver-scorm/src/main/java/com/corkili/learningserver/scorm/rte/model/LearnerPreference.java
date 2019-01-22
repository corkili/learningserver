package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithMin;
import com.corkili.learningserver.scorm.rte.model.datatype.State;

public class LearnerPreference implements GeneralDataType {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta("audio_level")
    private AudioLevel audioLevel;

    @Meta("language")
    private Language language;

    @Meta("delivery_speed")
    private Real7WithMin deliverySpeed;

    @Meta("audio_captioning")
    private State audioCaptioning;

    public LearnerPreference() {
        this.children = new CharacterString("audio_level,language,delivery_speed,audio_captioning");
        this.audioLevel = new AudioLevel();
        this.language = new Language();
        this.deliverySpeed = new Real7WithMin(0);
        this.audioCaptioning = new State(new String[]{"off", "no_change", "on"});
    }

    public CharacterString getChildren() {
        return children;
    }

    public void setChildren(CharacterString children) {
        this.children = children;
    }

    public AudioLevel getAudioLevel() {
        return audioLevel;
    }

    public void setAudioLevel(AudioLevel audioLevel) {
        this.audioLevel = audioLevel;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Real7WithMin getDeliverySpeed() {
        return deliverySpeed;
    }

    public void setDeliverySpeed(Real7WithMin deliverySpeed) {
        this.deliverySpeed = deliverySpeed;
    }

    public State getAudioCaptioning() {
        return audioCaptioning;
    }

    public void setAudioCaptioning(State audioCaptioning) {
        this.audioCaptioning = audioCaptioning;
    }
}
