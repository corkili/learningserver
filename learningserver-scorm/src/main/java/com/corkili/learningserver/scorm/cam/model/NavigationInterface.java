package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class NavigationInterface {

    // elements
    private List<HideLMSUI> hideLMSUIList; // 0...n

    public NavigationInterface() {
        hideLMSUIList = new ArrayList<>();
    }

    public List<HideLMSUI> getHideLMSUIList() {
        return hideLMSUIList;
    }

    public void setHideLMSUIList(List<HideLMSUI> hideLMSUIList) {
        this.hideLMSUIList = hideLMSUIList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("hideLMSUIList", hideLMSUIList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NavigationInterface that = (NavigationInterface) o;

        return new EqualsBuilder()
                .append(hideLMSUIList, that.hideLMSUIList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(hideLMSUIList)
                .toHashCode();
    }
}
