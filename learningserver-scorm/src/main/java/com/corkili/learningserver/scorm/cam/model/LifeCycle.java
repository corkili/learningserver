package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LifeCycle {

    private LanguageStrings version; // 0...1
    private Vocabulary status; // 0...1
    private List<Contribute> contributeList; // 0...n

    public LifeCycle() {
        contributeList = new ArrayList<>();
    }

    public LanguageStrings getVersion() {
        return version;
    }

    public void setVersion(LanguageStrings version) {
        this.version = version;
    }

    public Vocabulary getStatus() {
        return status;
    }

    public void setStatus(Vocabulary status) {
        this.status = status;
    }

    public List<Contribute> getContributeList() {
        return contributeList;
    }

    public void setContributeList(List<Contribute> contributeList) {
        this.contributeList = contributeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("version", version)
                .append("status", status)
                .append("contributeList", contributeList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LifeCycle lifeCycle = (LifeCycle) o;

        return new EqualsBuilder()
                .append(version, lifeCycle.version)
                .append(status, lifeCycle.status)
                .append(contributeList, lifeCycle.contributeList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(version)
                .append(status)
                .append(contributeList)
                .toHashCode();
    }
}
