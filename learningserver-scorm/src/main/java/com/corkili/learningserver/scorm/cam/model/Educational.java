package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class Educational {

    private Vocabulary interactivityType; // 0...1
    private List<Vocabulary> learningResourceTypeList; // 0...n
    private Vocabulary interactivityLevel; // 0...1
    private Vocabulary semanticDensity; // 0...1
    private List<Vocabulary> intendedEndUserRoleList; // 0...n
    private List<Vocabulary> contextList; // 0...n
    private List<LanguageString> typicalAgeRangeList; // 0...n
    private Vocabulary difficulty; // 0...1
    private Duration typicalLearningTime; // 0...1
    private List<LanguageString> descriptionList; // 0...n
    private List<String> languageList; // 0...n

}
