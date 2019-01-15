package com.corkili.learningserver.scorm.common;

import java.util.Arrays;

public class VocabularyLimit implements Limit<String> {

    private String[] vocabularyTable;

    public VocabularyLimit(String... vocabularyTable) {
        this.vocabularyTable = vocabularyTable;
    }

    @Override
    public boolean conform(String data) {
        return Arrays.binarySearch(vocabularyTable, data) >= 0;
    }
}
