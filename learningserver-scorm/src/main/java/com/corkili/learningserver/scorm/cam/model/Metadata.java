package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Metadata {

    private List<LOM> lomList; // 0...n
    private List<String> locationList; // 0...n
    private List<LOM> lomFromLocationList; // 0...n

    public Metadata() {
        lomList = new ArrayList<>();
        locationList = new ArrayList<>();
        lomFromLocationList = new ArrayList<>();
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

    public List<LOM> getLomFromLocationList() {
        return lomFromLocationList;
    }

    public void setLomFromLocationList(List<LOM> lomFromLocationList) {
        this.lomFromLocationList = lomFromLocationList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lomList", lomList)
                .append("locationList", locationList)
                .append("lomFromLocationList", lomFromLocationList)
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
                .append(lomFromLocationList, metadata.lomFromLocationList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lomList)
                .append(locationList)
                .append(lomFromLocationList)
                .toHashCode();
    }
}
