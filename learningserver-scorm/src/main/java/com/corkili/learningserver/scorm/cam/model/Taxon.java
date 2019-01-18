package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Taxon {

    private String id; // 0...1
    private LanguageString entry; // 0...1

    public Taxon() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LanguageString getEntry() {
        return entry;
    }

    public void setEntry(LanguageString entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("entry", entry)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Taxon taxon = (Taxon) o;

        return new EqualsBuilder()
                .append(id, taxon.id)
                .append(entry, taxon.entry)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(entry)
                .toHashCode();
    }
}
