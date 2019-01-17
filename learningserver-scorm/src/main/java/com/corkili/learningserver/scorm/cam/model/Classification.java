package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class Classification {

    private Vocabulary purpose; // 0...1
    private List<TaxonPath> taxonPathList; // 0...n
    private LanguageString description; // 0...1
    private List<LanguageString> keywordList; // 0...n

}
