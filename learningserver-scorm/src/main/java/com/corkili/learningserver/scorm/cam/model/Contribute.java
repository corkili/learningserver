package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.VCard;

public class Contribute {

    private Vocabulary role; // 0...1
    private List<VCard> entityList; // 0...n
    private DateTime date; // 0...1

}
