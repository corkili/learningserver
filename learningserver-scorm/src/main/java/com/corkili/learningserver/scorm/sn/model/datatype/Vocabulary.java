package com.corkili.learningserver.scorm.sn.model.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Vocabulary {

    private final Set<String> vocabularyTable;
    private String value;

    public Vocabulary(String value, String... vocabulary) {
        this.vocabularyTable = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(vocabulary)));
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (vocabularyTable.contains(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Set<String> getVocabularyTable() {
        return vocabularyTable;
    }
}
