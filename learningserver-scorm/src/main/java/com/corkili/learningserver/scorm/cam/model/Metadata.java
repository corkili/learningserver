package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Metadata {

    private List<LOM> lomList; // 0...n
    private List<String> locationList; // 0...n

    public Metadata() {
        lomList = new ArrayList<>();
        locationList = new ArrayList<>();
    }

    public List<LOM> getLomList() {
        return lomList;
    }

    public void setLomList(List<LOM> lomList) {
        this.lomList = lomList;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lomList", lomList)
                .append("locationList", locationList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Metadata metadata = (Metadata) o;

        return new EqualsBuilder()
                .append(lomList, metadata.lomList)
                .append(locationList, metadata.locationList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lomList)
                .append(locationList)
                .toHashCode();
    }
}
