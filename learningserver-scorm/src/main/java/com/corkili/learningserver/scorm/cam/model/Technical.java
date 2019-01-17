package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class Technical {

    private List<String> formatList; // 0...n
    private String size; // 0...1
    private List<String> locationList; // 0...n
    private List<Requirement> requirementList; // 0...n
    private LanguageString installationRemarks; // 0...1
    private LanguageString otherPlatformRequirements; // 0...1
    private Duration duration; // 0...1

}
