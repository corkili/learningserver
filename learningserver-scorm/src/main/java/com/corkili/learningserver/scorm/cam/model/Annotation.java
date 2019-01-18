package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.VCard;

public class Annotation {

    private VCard entity; // 0...1
    private DateTime date; // 0...1
    private LanguageString description; // 0...1

    public Annotation() {
    }

    public VCard getEntity() {
        return entity;
    }

    public void setEntity(VCard entity) {
        this.entity = entity;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entity", entity)
                .append("date", date)
                .append("description", description)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Annotation that = (Annotation) o;

        return new EqualsBuilder()
                .append(entity, that.entity)
                .append(date, that.date)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(entity)
                .append(date)
                .append(description)
                .toHashCode();
    }
}
