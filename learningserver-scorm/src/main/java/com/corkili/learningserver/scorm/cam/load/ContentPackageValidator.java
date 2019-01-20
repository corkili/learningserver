package com.corkili.learningserver.scorm.cam.load;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;

public class ContentPackageValidator {

    private boolean isAssert;
    private List<String> errorList;

    public ContentPackageValidator() {
        this(true);
    }

    public ContentPackageValidator(boolean isAssert) {
        this.isAssert = isAssert;
        this.errorList = new LinkedList<>();
    }

    public boolean validate(ContentPackage contentPackage) {
        return true;
    }

    public boolean isAssert() {
        return isAssert;
    }

    public void setAssert(boolean anAssert) {
        isAssert = anAssert;
    }

    public List<String> getErrorList() {
        return Collections.unmodifiableList(errorList);
    }

}
