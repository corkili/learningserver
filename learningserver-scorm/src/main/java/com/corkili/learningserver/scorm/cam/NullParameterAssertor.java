package com.corkili.learningserver.scorm.cam;

import com.corkili.learningserver.scorm.common.CommonUtils;

public class NullParameterAssertor {

    public static void assertParamemterIsNotNull(String methodName, String parameterName, Object parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException(CommonUtils.format(
                    "The parameter, \"{}\", passed to the method \"{}\" cannot be null.", parameterName, methodName));
        }
    }

}
