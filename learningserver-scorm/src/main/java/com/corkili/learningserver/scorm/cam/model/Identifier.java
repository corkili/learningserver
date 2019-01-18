package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Identifier {

    private String catalog; // 0...1
    private String entry; // 0...1

    public Identifier() {
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("catalog", catalog)
                .append("entry", entry)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return new EqualsBuilder()
                .append(catalog, that.catalog)
                .append(entry, that.entry)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(catalog)
                .append(entry)
                .toHashCode();
    }
}
