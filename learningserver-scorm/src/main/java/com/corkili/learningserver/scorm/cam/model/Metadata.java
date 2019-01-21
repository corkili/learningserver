package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Metadata {

    private List<LOM> lomList; // 0...n
    private Map<String, LOM> locationLomMap; // 0...n

    public Metadata() {
        lomList = new ArrayList<>();
        locationLomMap = new HashMap<>();
    }

    public List<LOM> getLomList() {
        return lomList;
    }

    public void setLomList(List<LOM> lomList) {
        this.lomList = lomList;
    }

    public Map<String, LOM> getLocationLomMap() {
        return locationLomMap;
    }

    public void setLocationLomMap(Map<String, LOM> locationLomMap) {
        this.locationLomMap = locationLomMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lomList", lomList)
                .append("locationLomMap", locationLomMap)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Metadata metadata = (Metadata) o;

        return new EqualsBuilder()
                .append(lomList, metadata.lomList)
                .append(locationLomMap, metadata.locationLomMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lomList)
                .append(locationLomMap)
                .toHashCode();
    }
}
