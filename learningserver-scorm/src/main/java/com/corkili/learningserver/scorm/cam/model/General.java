package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class General {

    private List<Identifier> identifier; // 0...n
    private LanguageString title; // 0...1
    private List<String> languageList; //0...n
    private List<LanguageString> descriptionList; // 0...n
    private List<LanguageString> keywordList; // 0...n
    private List<LanguageString> coverageList; // 0...n
    private Vocabulary structure; // 0...1
    private Vocabulary aggregationLevel; // 0...1

}
