package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Classification {

    private Vocabulary purpose; // 0...1
    private List<TaxonPath> taxonPathList; // 0...n
    private LanguageString description; // 0...1
    private List<LanguageString> keywordList; // 0...n

    public Classification() {
        taxonPathList = new ArrayList<>();
        keywordList = new ArrayList<>();
    }

    public Vocabulary getPurpose() {
        return purpose;
    }

    public void setPurpose(Vocabulary purpose) {
        this.purpose = purpose;
    }

    public List<TaxonPath> getTaxonPathList() {
        return taxonPathList;
    }

    public void setTaxonPathList(List<TaxonPath> taxonPathList) {
        this.taxonPathList = taxonPathList;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }

    public List<LanguageString> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<LanguageString> keywordList) {
        this.keywordList = keywordList;
    }
}
