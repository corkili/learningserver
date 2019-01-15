package com.corkili.learningserver.scorm.common;

public interface Limit<T> {

    boolean conform(T data);

}
