package com.corkili.learningserver.scorm.cam.load;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class ModelUtils {

    public static boolean isAnyUriEmpty(AnyURI anyURI) {
        if (anyURI != null) {
            return StringUtils.isBlank(anyURI.getValue());
        } else {
            return false;
        }
    }

}
