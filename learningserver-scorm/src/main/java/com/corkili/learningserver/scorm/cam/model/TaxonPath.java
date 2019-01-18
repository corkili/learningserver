package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("source", source)
                .append("taxonList", taxonList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TaxonPath taxonPath = (TaxonPath) o;

        return new EqualsBuilder()
                .append(source, taxonPath.source)
                .append(taxonList, taxonPath.taxonList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(source)
                .append(taxonList)
                .toHashCode();
    }
}
