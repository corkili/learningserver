package com.corkili.learningserver.scorm.cam.xml;

/**
 * All attributes.
 */
public class Attributes {

    public static final Attribute IDENTIFIER__M__ID__NO_LIMIT__NO_SPM = new Attribute(
            "identifier",
            true,
            XmlDataType.ID,
            "",
            false,
            Limits.NONE_LIMIT,
            -1
    );

    public static final Attribute VERSION__O__STRING__NO_LIMIT__20 = new Attribute(
            "version",
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            20
    );

    public static final Attribute XML_BASE__O__ANY_URI__NO_LIMIT__2000 = new Attribute(
            "xml:base",
            false,
            XmlDataType.ANY_URI,
            "",
            false,
            Limits.NONE_LIMIT,
            2000
    );

    public static final Attribute DEFAULT__M__IDREF__NO_LIMIT__NO_SPM = new Attribute(
            "default",
            true,
            XmlDataType.IDREF,
            "",
            false,
            Limits.NONE_LIMIT,
            -1
    );

    public static final Attribute STRUCTURE__O__STRING__NO_LIMIT__200 = new Attribute(
            "structure",
            false,
            XmlDataType.STRING,
            "hierarchical",
            false,
            Limits.NONE_LIMIT,
            200
    );

    public static final Attribute ADLSEQ_OBJECTIVES_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "adlseq:objectivesGlobalToSystem",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1
    );

    public static final Attribute ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "adlcp:sharedDataGlobalToSystem",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1
    );

    public static final Attribute IDENTIFIERREF__O__STRING__NO_LIMIT__2000 = new Attribute(
            "identifierref",
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            2000
    );

    public static final Attribute ISVISIBLE__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "isvisible",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1
    );

    public static final Attribute PARAMETERS__O__STRING__FORMAT_LIMIT__1000 = new Attribute(
            "parameters",
            false,
            XmlDataType.STRING,
            "",
            true,
            Limits.PARAMETERS_FORMAT_LIMIT,
            1000
    );

    public static final Attribute COMPLETED_BY_MEASURE__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "completedByMeasure",
            false,
            XmlDataType.BOOLEAN,
            "false",
            true,
            Limits.BOOL_LIMIT,
            -1
    );

    public static final Attribute MIN_PROGRESS_MEASURE__O__DECIMAL__RANGE_LIMIT__NO_SPM = new Attribute(
            "minProgressMeasure",
            false,
            XmlDataType.DECIMAL,
            "1.0",
            true,
            Limits.DECIMAL_0_TO_1_WITH_SCALE_EQUAL_4_LIMIT,
            -1
    );

    public static final Attribute PROGRESS_WEIGHT__O__DECIMAL__RANGE_LIMIT__NO_SPM = new Attribute(
            "progressWeight",
            false,
            XmlDataType.DECIMAL,
            "1.0",
            true,
            Limits.DECIMAL_0_TO_1_WITH_SCALE_EQUAL_4_LIMIT,
            -1
    );

    public static final Attribute TARGET_ID__M__ANY_URI__NO_LIMIT__NO_SPM = new Attribute(
            "targetID",
            true,
            XmlDataType.ANY_URI,
            "",
            false,
            Limits.NONE_LIMIT,
            -1
    );

    public static final Attribute READ_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "readSharedData",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1
    );

    public static final Attribute WRITE_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM = new Attribute(
            "writeSharedData",
            false,
            XmlDataType.BOOLEAN,
            "true",
            true,
            Limits.BOOL_LIMIT,
            -1
    );
}
