package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class LifeCycle {

    private LanguageString version; // 0...1
    private Vocabulary status; // 0...1
    private List<Contribute> contributeList; // 0...n

}
