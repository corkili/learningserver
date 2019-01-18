package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrComposite {

    private Vocabulary type; // 0...1
    private Vocabulary name; // 0...1
    private String minimumVersion; // 0...1
    private String maximumVersion; // 0...1

    public OrComposite() {
    }

    public Vocabulary getType() {
        return type;
    }

    public void setType(Vocabulary type) {
        this.type = type;
    }

    public Vocabulary getName() {
        return name;
    }

    public void setName(Vocabulary name) {
        this.name = name;
    }

    public String getMinimumVersion() {
        return minimumVersion;
    }

    public void setMinimumVersion(String minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    public String getMaximumVersion() {
        return maximumVersion;
    }

    public void setMaximumVersion(String maximumVersion) {
        this.maximumVersion = maximumVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("name", name)
                .append("minimumVersion", minimumVersion)
                .append("maximumVersion", maximumVersion)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        OrComposite that = (OrComposite) o;

        return new EqualsBuilder()
                .append(type, that.type)
                .append(name, that.name)
                .append(minimumVersion, that.minimumVersion)
                .append(maximumVersion, that.maximumVersion)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(name)
                .append(minimumVersion)
                .append(maximumVersion)
                .toHashCode();
    }
}
