package com.corkili.learningserver.scorm.cam.xml;

/**
 * All attributes.
 */
public class Attributes {

    /**
     *
     */
    public static final Attribute IDENTIFIER__M__ID__NO_LIMIT__NO_SPM = new Attribute(
            "identifier",
            true,
            XmlDataType.ID,
            "",
            false,
            Limits.NONE_LIMIT,
            -1);

    public static final Attribute VERSION__O__STRING__NO_LIMIT__20 = new Attribute(
            "version",
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            20);

    public static final Attribute XML_BASE__O__ANY_URI__NO_LIMIT__2000 = new Attribute(
            "xml:base",
            false,
            XmlDataType.ANY_URI,
            "",
            false,
            Limits.NONE_LIMIT,
            2000);

    public static final Attribute DEFAULT__M__IDREF__NO_LIMIT__NO_SPM = new Attribute(
            "default",
            true,
            XmlDataType.IDREF,
            "",
            false,
            Limits.NONE_LIMIT,
            -1);

    public static final Attribute STRUCTURE__O__STRING__NO_LIMIT__200 = new Attribute(
            "structure",
            false,
            XmlDataType.STRING,
            "hierarchical",
            false,
            Limits.NONE_LIMIT,
            200);

    public static final Attribute ADLSEQ_OBJECTIVES_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "adlseq:objectivesGlobalToSystem",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1);

    public static final Attribute ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "adlcp:sharedDataGlobalToSystem",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1);
}
