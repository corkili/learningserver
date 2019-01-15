package com.corkili.learningserver.scorm.common;

public class NoneLimit implements Limit<Object> {

    @Override
    public boolean conform(Object data) {
        return true;
    }
}
