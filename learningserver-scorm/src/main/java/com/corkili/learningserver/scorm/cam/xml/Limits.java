package com.corkili.learningserver.scorm.cam.xml;

import com.corkili.learningserver.scorm.common.Limit;
import com.corkili.learningserver.scorm.common.VocabularyLimit;

public class Limits {

    public static final Limit<String> NONE_LIMIT = data -> true;

    public static final VocabularyLimit SCHEMA_IN_METADATA_IN_MANIFEST_LIMIT =
            new VocabularyLimit("ADL SCORM");

    public static final VocabularyLimit SCHEMA_VERSION_IN_METADATA_IN_MANIFEST_LIMIT =
            new VocabularyLimit(("2004 4th Edition"));

    public static final VocabularyLimit BOOL_LIMIT =
            new VocabularyLimit("true", "false");

}
