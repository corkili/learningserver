package com.corkili.learningserver.scorm.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NameAndValue<V> {

    private String name;
    private V value;

}
