package com.corkili.learningserver.scorm.rte.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Request implements TerminalDataType {

    private static final Set<String> requestTable = new HashSet<>(Arrays.asList(
            "continue", "previous", "choice", "jump", "exit", "exitAll",
            "abandon", "abandonAll", "suspendAll", "_none_"));

    private String value;

    private String request;

    private String target;

    public Request() {
        this.value = "_none_";
        this.request = "";
        this.target = "";
    }

    @Override
    public ScormResult set(String value) {
        if (set0(value)) {
            return new ScormResult("true", ScormError.E_0);
        } else {
            return new ScormResult("false", ScormError.E_406);
        }
    }

    @Override
    public ScormResult get() {
        return new ScormResult(value, ScormError.E_0);
    }


    private boolean set0(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        value = value.trim();
        String request;
        String target;
        if (value.startsWith("{target=")) {
            target = value.substring(8, value.indexOf("}"));
            request = value.substring(value.indexOf("}") + 1);
        } else {
            target = "";
            request = value;
        }
        if (!requestTable.contains(request)) {
            return false;
        }
        if ("choice".equals(request) || "jump".equals(request)) {
            if (StringUtils.isBlank(target)) {
                return false;
            }
        } else {
            if (StringUtils.isNotBlank(target)) {
                return false;
            }
        }
        this.value = value;
        this.request = request.trim();
        this.target = target.trim();
        return true;
    }

    public String getValue() {
        return value;
    }

    public Request setValue(String value) {
        this.value = value;
        return this;
    }

    public String getRequest() {
        return request;
    }

    public Request setRequest(String request) {
        this.request = request;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public Request setTarget(String target) {
        this.target = target;
        return this;
    }
}
