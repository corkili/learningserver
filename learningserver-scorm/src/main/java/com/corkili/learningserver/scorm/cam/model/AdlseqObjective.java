package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class AdlseqObjective {

    // attributes
    private AnyURI objectiveID; // M

    // elements
    private List<AdlseqMapInfo> mapInfoList; // 1...n

    public AdlseqObjective() {
        mapInfoList = new ArrayList<>();
    }

    public AnyURI getObjectiveID() {
        return objectiveID;
    }

    public void setObjectiveID(AnyURI objectiveID) {
        this.objectiveID = objectiveID;
    }

    public List<AdlseqMapInfo> getMapInfoList() {
        return mapInfoList;
    }

    public void setMapInfoList(List<AdlseqMapInfo> mapInfoList) {
        this.mapInfoList = mapInfoList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("objectiveID", objectiveID)
                .append("mapInfoList", mapInfoList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdlseqObjective that = (AdlseqObjective) o;

        return new EqualsBuilder()
                .append(objectiveID, that.objectiveID)
                .append(mapInfoList, that.mapInfoList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(objectiveID)
                .append(mapInfoList)
                .toHashCode();
    }
}
