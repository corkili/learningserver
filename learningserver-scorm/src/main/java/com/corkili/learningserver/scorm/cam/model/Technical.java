package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Technical {

    private List<String> formatList; // 0...n
    private String size; // 0...1
    private List<String> locationList; // 0...n
    private List<Requirement> requirementList; // 0...n
    private LanguageString installationRemarks; // 0...1
    private LanguageString otherPlatformRequirements; // 0...1
    private Duration duration; // 0...1

    public Technical() {
        formatList = new ArrayList<>();
        locationList = new ArrayList<>();
        requirementList = new ArrayList<>();
    }

    public List<String> getFormatList() {
        return formatList;
    }

    public void setFormatList(List<String> formatList) {
        this.formatList = formatList;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

    public List<Requirement> getRequirementList() {
        return requirementList;
    }

    public void setRequirementList(List<Requirement> requirementList) {
        this.requirementList = requirementList;
    }

    public LanguageString getInstallationRemarks() {
        return installationRemarks;
    }

    public void setInstallationRemarks(LanguageString installationRemarks) {
        this.installationRemarks = installationRemarks;
    }

    public LanguageString getOtherPlatformRequirements() {
        return otherPlatformRequirements;
    }

    public void setOtherPlatformRequirements(LanguageString otherPlatformRequirements) {
        this.otherPlatformRequirements = otherPlatformRequirements;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("formatList", formatList)
                .append("size", size)
                .append("locationList", locationList)
                .append("requirementList", requirementList)
                .append("installationRemarks", installationRemarks)
                .append("otherPlatformRequirements", otherPlatformRequirements)
                .append("duration", duration)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Technical technical = (Technical) o;

        return new EqualsBuilder()
                .append(formatList, technical.formatList)
                .append(size, technical.size)
                .append(locationList, technical.locationList)
                .append(requirementList, technical.requirementList)
                .append(installationRemarks, technical.installationRemarks)
                .append(otherPlatformRequirements, technical.otherPlatformRequirements)
                .append(duration, technical.duration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(formatList)
                .append(size)
                .append(locationList)
                .append(requirementList)
                .append(installationRemarks)
                .append(otherPlatformRequirements)
                .append(duration)
                .toHashCode();
    }
}
