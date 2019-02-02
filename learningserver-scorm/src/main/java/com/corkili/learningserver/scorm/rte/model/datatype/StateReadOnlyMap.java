package com.corkili.learningserver.scorm.rte.model.datatype;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class StateReadOnlyMap implements MapDataType {

    private final Set<String> stateTable;

    private final Map<String, String> map;

    public StateReadOnlyMap(String... states) {
        this.stateTable = new HashSet<>(Arrays.asList(states));
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public ScormResult set(String key, String value) {
        return new ScormResult("false", ScormError.E_404);
    }

    @Override
    public ScormResult get(String key) {
        String value = map.get(key);
        if (key == null || value == null) {
            return new ScormResult("", ScormError.E_301);
        }
        return new ScormResult(value, ScormError.E_0);
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public String getValue(String key) {
        return map.get(key);
    }


}
