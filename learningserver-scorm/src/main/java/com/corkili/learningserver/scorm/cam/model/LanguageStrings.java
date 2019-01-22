package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LanguageStrings {

    List<LanguageString> languageStringList;

    public LanguageStrings() {
        this.languageStringList = new ArrayList<>();
    }

    public List<LanguageString> getLanguageStringList() {
        return languageStringList;
    }

    public void setLanguageStringList(List<LanguageString> languageStringList) {
        this.languageStringList = languageStringList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("languageStringList", languageStringList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LanguageStrings that = (LanguageStrings) o;

        return new EqualsBuilder()
                .append(languageStringList, that.languageStringList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(languageStringList)
                .toHashCode();
    }
}
