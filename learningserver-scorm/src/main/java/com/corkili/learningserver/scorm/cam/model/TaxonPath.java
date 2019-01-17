package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class TaxonPath {

    private LanguageString source; // 0...1
    private List<Taxon> taxonList; // 0...n

    public TaxonPath() {
        taxonList = new ArrayList<>();
    }

    public LanguageString getSource() {
        return source;
    }

    public void setSource(LanguageString source) {
        this.source = source;
    }

    public List<Taxon> getTaxonList() {
        return taxonList;
    }

    public void setTaxonList(List<Taxon> taxonList) {
        this.taxonList = taxonList;
    }
}
