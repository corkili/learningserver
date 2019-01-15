package com.corkili.learningserver.scorm.cam.xml;

/**
 * Describe XML Data Type.
 */
public enum XmlDataType {
    CONTAINER("__container__"),
    ID("xs:ID"),
    STRING("xs:String"),
    ANY_URI("xs:anyURI"),
    IDREF("xs:IDREF"),
    BOOLEAN("xs:boolean");

    private String dataTypeName;

    XmlDataType(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }
}