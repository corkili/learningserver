package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Data {

    // elements
    private List<Map> mapList; // 1...n

    public Data() {
        mapList = new ArrayList<>();
    }

    public List<Map> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map> mapList) {
        this.mapList = mapList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("mapList", mapList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        return new EqualsBuilder()
                .append(mapList, data.mapList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mapList)
                .toHashCode();
    }
}
