package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

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
}
