package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Duration {

    private String duration; // 0...1
    private LanguageStrings description; // 0...1

    public Duration() {
    }

    public Duration(String duration) {
        this.duration = duration;
    }

    public Duration(LanguageStrings description) {
        this.description = description;
    }

    public Duration(String duration, LanguageStrings description) {
        this.duration = duration;
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LanguageStrings getDescription() {
        return description;
    }

    public void setDescription(LanguageStrings description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("duration", duration)
                .append("description", description)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Duration duration1 = (Duration) o;

        return new EqualsBuilder()
                .append(duration, duration1.duration)
                .append(description, duration1.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(duration)
                .append(description)
                .toHashCode();
    }
}
