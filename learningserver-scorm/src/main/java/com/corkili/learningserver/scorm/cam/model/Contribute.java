package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.VCard;

public class Contribute {

    private Vocabulary role; // 0...1
    private List<VCard> entityList; // 0...n
    private DateTime date; // 0...1

    public Contribute() {
        entityList = new ArrayList<>();
    }

    public Vocabulary getRole() {
        return role;
    }

    public void setRole(Vocabulary role) {
        this.role = role;
    }

    public List<VCard> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<VCard> entityList) {
        this.entityList = entityList;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("role", role)
                .append("entityList", entityList)
                .append("date", date)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Contribute that = (Contribute) o;

        return new EqualsBuilder()
                .append(role, that.role)
                .append(entityList, that.entityList)
                .append(date, that.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(role)
                .append(entityList)
                .append(date)
                .toHashCode();
    }
}
