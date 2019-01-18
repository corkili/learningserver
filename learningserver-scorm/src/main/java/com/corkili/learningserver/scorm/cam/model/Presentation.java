package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Presentation {

    private NavigationInterface navigationInterface; // 0...1

    public Presentation() {
    }

    public NavigationInterface getNavigationInterface() {
        return navigationInterface;
    }

    public void setNavigationInterface(NavigationInterface navigationInterface) {
        this.navigationInterface = navigationInterface;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("navigationInterface", navigationInterface)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Presentation that = (Presentation) o;

        return new EqualsBuilder()
                .append(navigationInterface, that.navigationInterface)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(navigationInterface)
                .toHashCode();
    }
}
