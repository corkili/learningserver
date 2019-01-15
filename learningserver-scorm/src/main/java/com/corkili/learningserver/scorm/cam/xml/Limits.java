package com.corkili.learningserver.scorm.cam.xml;

import java.util.regex.Pattern;

import com.corkili.learningserver.scorm.common.DecimalRangeLimit;
import com.corkili.learningserver.scorm.common.Limit;
import com.corkili.learningserver.scorm.common.VocabularyLimit;

public class Limits {

    public static final Limit<String> NONE_LIMIT = data -> true;

    public static final VocabularyLimit SCHEMA_IN_METADATA_IN_MANIFEST_LIMIT =
            new VocabularyLimit("ADL SCORM");

    public static final VocabularyLimit SCHEMA_VERSION_IN_METADATA_IN_MANIFEST_LIMIT =
            new VocabularyLimit("2004 4th Edition");

    public static final VocabularyLimit BOOL_LIMIT =
            new VocabularyLimit("true", "false");

    public static final Limit<String> PARAMETERS_FORMAT_LIMIT = new Limit<String>() {

        Pattern pattern = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");

        @Override
        public boolean conform(String data) {
            String testData = "http://www.test.com";
            if (data.startsWith("#") || data.endsWith("?")) {
                testData += data;
            } else {
                testData += "?" + data;
            }
            return pattern.matcher(testData).matches();
        }
    };

    public static final VocabularyLimit TIME_LIMIT_ACTION_IN_ITEM_LIMIT =
            new VocabularyLimit("exit,message", "exit,no message",
                    "continue,message", "continue,no message");

    public static final DecimalRangeLimit DECIMAL_0_TO_1_WITH_SCALE_EQUAL_4_LIMIT =
            new DecimalRangeLimit(4, 0, 1);

    public static final VocabularyLimit TYPE_IN_RESOURCE =
            new VocabularyLimit("webcontent");

    public static final VocabularyLimit SCORM_CONTENT_TYPE_LIMIT =
            new VocabularyLimit("sco", "asset");

}
