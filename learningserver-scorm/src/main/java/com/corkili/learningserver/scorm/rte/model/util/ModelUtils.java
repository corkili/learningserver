package com.corkili.learningserver.scorm.rte.model.util;

public class ModelUtils {

    public static boolean isDelimiterFormatCorrect(String delimiter, String name) {
        if (!delimiter.startsWith("{") || !delimiter.endsWith("}")) {
            return false;
        }
        String[] content = delimiter.substring(1, delimiter.length() - 1).split("=");
        if (content.length != 2) {
            return false;
        }
        if (!content[1].equals(name)) {
            return false;
        }
        return true;
    }

}
