package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DateTime {

    private String dateTime; // 0...1
    private LanguageStrings description; // 0...1

    public DateTime() {
    }

    public DateTime(String dateTime, LanguageStrings description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    public DateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime(LanguageStrings description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
                .append("dateTime", dateTime)
                .append("description", description)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DateTime dateTime1 = (DateTime) o;

        return new EqualsBuilder()
                .append(dateTime, dateTime1.dateTime)
                .append(description, dateTime1.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dateTime)
                .append(description)
                .toHashCode();
    }
}
