package com.corkili.learningserver.scorm.cam.xml;

import static com.corkili.learningserver.scorm.cam.xml.Attributes.ADLCP_SCORM_TYPE__M__STRING__VOCABULARY_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.ADLSEQ_OBJECTIVES_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.COMPLETED_BY_MEASURE__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.DEFAULT__M__IDREF__NO_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.HREF__M__STRING__NO_LIMIT__2000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.HREF__O__STRING__NO_LIMIT__2000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.IDENTIFIERREF__M__STRING__NO_LIMIT__2000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.IDENTIFIERREF__O__STRING__NO_LIMIT__2000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.IDENTIFIER__M__ID__NO_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.ISVISIBLE__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.MIN_PROGRESS_MEASURE__O__DECIMAL__RANGE_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.PARAMETERS__O__STRING__FORMAT_LIMIT__1000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.PROGRESS_WEIGHT__O__DECIMAL__RANGE_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.READ_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.STRUCTURE__O__STRING__NO_LIMIT__200;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.TARGET_ID__M__ANY_URI__NO_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.TYPE__M__STRING__VOCABULARY_LIMIT__1000;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.VERSION__O__STRING__NO_LIMIT__20;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.WRITE_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.XML_BASE__O__ANY_URI__NO_LIMIT__2000;

import java.util.Arrays;
import java.util.Collections;

/**
 * All Elements described in CAM.
 */
public final class Elements {

    private static final String NAMESPACE_IMSCP = "http://www.imsglobal.org/xsd/imscp_v1p1";
    private static final String PREFIX_IMSCP = "imscp";

    private static final String NAMESPACE_ADLCP = "http://www.adlnet.org/xsd/adlcp_v1p3";
    private static final String PREFIX_ADLCP = "adlcp";

    private static final String NAMESPACE_ADLSEQ = "http://www.adlnet.org/xsd/adlseq_v1p3";
    private static final String PREFIX_ADLSEQ = "adlseq";

    private static final String NAMESPACE_ADLNAV = "http://www.adlnet.org/xsd/adlnav_v1p3";
    private static final String PREFIX_ADLNAV = "adlnav";

    private static final String NAMESPACE_LOM = "http://ltsc.ieee.org/xsd/LOM";
    private static final String PREFIX_LOM = "lom";

    /**
     * The <entry> element represents the value of the identifier within the identification or cataloging scheme
     * (refer to the <catalog> element) that designates or identifies the learning object.
     */
    public static final Element ENTRY__IN_IDENTIFIER = new Element(
            "entry",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO_OR_ONE,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            1000,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <catalog> element represents the name or designator of the identification or cataloging scheme for the
     * entry. There are a variety of cataloging systems available. SCORM does not require the use of any one
     * particular cataloging system. Organizations are free to choose any cataloging scheme that meets their
     * organizations practices or policies.
     */
    public static final Element CATALOG__IN_IDENTIFIER = new Element(
            "catalog",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO_OR_ONE,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            1000,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <identifier> element represents a mechanism for assigning a globally unique label that identifies the
     * SCORM Content Model Component. The notion of assigning a globally unique identifier to a component is
     * important when dealing with multiple facets of learning content development (e.g., versioning, maintenance, etc.).
     */
    public static final Element IDENTIFIER__IN_GENERAL = new Element(
            "identifier",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ZERO_OR_MORE,
            Multiplicity.ZERO_OR_MORE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            10,
            Collections.emptyList(),
            Arrays.asList(
                    CATALOG__IN_IDENTIFIER,
                    ENTRY__IN_IDENTIFIER) // TODO: entry
    );

    /**
     * The General category groups the general information that describes the resource as a whole. The resource
     * in this case is the particular SCORM Content Model Component (asset, SCO, activity, content organization
     * or content aggregation) being described. This general information is sometimes viewed as key information
     * in that it is important for describing the particular component.
     */
    public static final Element GENERAL__IN_LOM = new Element(
            "general",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO_OR_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList(
                    IDENTIFIER__IN_GENERAL) // TODO: title, language, description, keyword, coverage, structure, aggregationLevel
    );

    /**
     * All metadata instances shall have <lom> as the root node. The root node begins to define the metadata
     * used to describe the SCORM Content Model Component.
     */
    public static final Element LOM = new Element(
            "lom",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList(
                    GENERAL__IN_LOM) // TODO: lifeCycle, metaMetadata, technical, educational, rights, relation, annotation, classification
    );

    /**
     * All metadata instances shall have <lom> as the root node. The root node begins to define the metadata
     * used to describe the SCORM Content Model Component.
     */
    public static final Element LOM__IN_METADATA = new Element(
            "lom",
            NAMESPACE_LOM,
            PREFIX_LOM,
            Multiplicity.ZERO_OR_MORE,
            Multiplicity.ZERO_OR_MORE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList() // TODO: general, lifeCycle, metaMetadata, technical, educational, rights, relation, annotation, classification
    );

    public static final Element DEPENDENCY__IN_RESOURCE = new Element(
            "dependency",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_MORE,
            Multiplicity.ZERO_OR_MORE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.singletonList(
                    /*
                    The identifierref attribute references an identifier attribute of a <resource>
                    (within the scope of the <manifest> element for which it is defined) and is used
                    to resolve the ultimate location of the dependent resource. The identifierref is
                    not permitted to reference a resource defined in a (sub)manifest.
                     */
                    IDENTIFIERREF__M__STRING__NO_LIMIT__2000),
            Collections.emptyList()
    );

    /**
     * The <metadata> element is metadata describing the file. It contains relevant information that
     * describes the <file> element(i.e.,asset) as a whole. The <metadata> element is considered the
     * root node for metadata describing the asset. This means that all metadata for the asset is
     * defined as a child of the <metadata> element.
     */
    public static final Element METADATA__IN_FILE = new Element(
            "metadata",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO_OR_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList() // TODO: {Metadata}

    );

    /**
     * The <file> element is a listing of files that a resource described by a <resource> element requires
     * for delivery. This element is repeated as necessary for each file for a given resource. The element
     * acts as an inventory system detailing the set of files used to build the resource. The <file>
     * element represents files that are local to the content package. For all files that are required
     * for delivery of the content package in a SCORM run-time environment and are local to the content
     * package (physically located in the content package), a <file> element shall be used to represent
     * the file relative to the resource in which it is used. If the resource identified by a <resource>
     * element is local to the package, then the resource itself shall be identified as a <file> element.
     * The launch location of the <resource> (<resource>’s href value) shall be used as the href of the
     * file. The value of the <file> element’s href attribute should not contain any parameters that
     * may have been defined on the <resource> element’s href attribute.
     *
     * All of the physical files that are included in the content package should be referenced by a file
     * element. Leaving these references to the physical files out of the manifest may cause a wide range
     * of problems.
     */
    public static final Element FILE__IN_RESOURCE = new Element(
            "file",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_MORE,
            Multiplicity.ZERO_OR_MORE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.singletonList(
                    /*
                    The href attribute identifies the location of the file. This value is affected
                    by the use of xml:base values. Some systems may treat this value as case sensitive,
                    developers should be aware of this and ensure that values used match the resources
                    being referenced.
                     */
                    HREF__M__STRING__NO_LIMIT__2000),
            Collections.singletonList(METADATA__IN_FILE)
    );

    /**
     * The <metadata> element is metadata describing the resource [3]. It contains relevant information
     * that describes the <resource> element as a whole. The<resource> element may represent a SCO or
     * an asset. This depends on the value of the adlcp:scormType attribute. The <metadata> element
     * is considered the root node for metadata describing the resource. This means that all metadata
     * for the resource is defined as a child of the <metadata> element.
     */
    public static final Element METADATA__IN_RESOURCE = new Element(
            "metadata",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO_OR_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList() // TODO: {Metadata}
    );

    /**
     * The <resource> element is a reference to a resource. There are two primary types of resources
     * defined within SCORM: SCOs and assets.
     *
     * A leaf <item> element is required to reference a resource (SCO resource or asset resource).
     * If an <item> references a resource, this resource is subject to being identified for delivery
     * and launch to the learner. If an <item> references a <resource>, then the <resource> element
     * shall meet the following requirements:
     *  - The type attribute should be set to webcontent.
     *  - The adlcp:scormType shall be set to sco or asset.
     *  - The href attribute shall be required.
     */
    public static final Element RESOURCE__IN_RESOURCES = new Element(
            "resource",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_OR_MORE,
            Multiplicity.ONE_OR_MORE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    The identifier attribute represents an identifier, of the resource, that is unique
                    within the scope of its containing manifest file
                     */
                    IDENTIFIER__M__ID__NO_LIMIT__NO_SPM,
                    /*
                    The type attribute indicates the type of resource.
                     */
                    TYPE__M__STRING__VOCABULARY_LIMIT__1000,
                    /*
                    The href attribute is a reference a Uniform Resource Locator (URL). The href attribute
                    represents the “entry point” or “launching point” of this resource. External fully
                    qualified URLs are also permitted. The URL may also contain additional parameters.
                    This value is affected by the use of xml:base values.
                     */
                    HREF__O__STRING__NO_LIMIT__2000,
                    /*
                    The xml:base attribute provides a relative path off set for the files contained in the manifest.
                     */
                    XML_BASE__O__ANY_URI__NO_LIMIT__2000,
                    /*
                    The adlcp:scormType attribute defines the type of SCORM resource. This is an ADL
                    defined extension to the IMS Content Packaging Information Model. The character string
                    is restricted and shall be one of the following characterstring tokens (sco or asset).
                    Where sco indicates that the resource is a SCO resource and asset indicates that
                    the resource is an asset resource.
                     */
                    ADLCP_SCORM_TYPE__M__STRING__VOCABULARY_LIMIT__NO_SPM),
            Arrays.asList(
                    METADATA__IN_RESOURCE,
                    FILE__IN_RESOURCE,
                    DEPENDENCY__IN_RESOURCE)
    );

    /**
     * The <resources> element is a collection of references to resources. There is no assumption of
     * order or hierarchy of the individual <resource> elements that the <resources> element contains.
     */
    public static final Element RESOURCES__IN_MANIFEST = new Element(
            "resources",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.singletonList(
                    /*
                    The xml:base attribute provides a relative path off set for the content file(s).
                     */
                    XML_BASE__O__ANY_URI__NO_LIMIT__2000),
            Collections.singletonList(RESOURCE__IN_RESOURCES)
    );

    /**
     * The <metadata> element is metadata describing the organization. It contains relevant information
     * that describes the <organization> element (i.e., Content Organization) as a whole. The <metadata>
     * element is considered the root node for metadata describing the content organization. This means
     * that all metadata for the content organization is defined as a child of the <metadata> element.
     */
    public static final Element METADATA__IN_ORGANIZATION = new Element(
            "metadata",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList() // TODO: {Metadata}
    );

    /**
     * The <map> element is the container used to describe how an activity will utilize a specific set of shared data.
     */
    public static final Element MAP__IN_DATA = new Element(
            "map",
            NAMESPACE_ADLCP,
            PREFIX_ADLCP,
            Multiplicity.ONE_OR_MORE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    The identifier of shared data targeted for the mapping. The underlying data type for
                    the targetID, is a unique identifier. Since an empty characterstring does not provide
                    sufficient semantic information to uniquely identify which global shared objective
                    is being targeted, then the targetID attribute cannot be an empty characterstring
                    and cannot contain all whitespace characters (which could be transcribed as an empty
                    characterstring by an XML parser).
                     */
                    TARGET_ID__M__ANY_URI__NO_LIMIT__NO_SPM,
                    /*
                    This attribute indicates that currently available shared data will be utilized
                    by the activity while it is active.
                     */
                    READ_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM,
                    /*
                    This attribute indicates that shared data should be persisted (true or false) upon
                    termination ( Terminate(“”) ) of the attempt on the activity.
                     */
                    WRITE_SHARED_DATA__O__BOOLEAN__BOOL_LIMIT__NO_SPM),
            Collections.emptyList()
    );

    /**
     * The <data> element is the container used to define sets of data shared associated with an activity.
     * This element is an ADL defined extension to the IMS Content Packaging Specification. The element
     * shall only appear, if needed, as a child of a leaf <item> element that references a SCO resource.
     */
    public static final Element DATA__IN_ITEM = new Element(
            "data",
            NAMESPACE_ADLCP,
            PREFIX_ADLCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Collections.singletonList(MAP__IN_DATA)
    );

    /**
     * The <completionThreshold> element enables the definition of a threshold value to be applied
     * when evaluating an activty’s completion status. It also provides a means to designate the
     * degree of contribution (weight) the activity’s completion imposes relative to its siblings.
     * This element is an ADL defined extension to the IMS Content Packaging Specification.
     * The element shall only appear, if needed, as a child of an <organization> element or <item> element.
     */
    public static final Element COMPLETION_THRESHOLD__IN_ITEM = new Element(
            "completionThreshold",
            NAMESPACE_ADLCP,
            PREFIX_ADLCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    This attribute indicates whether the minProgressMeasure attribute’s data value
                    shall be used in place of any other method to determine if the activity is completed.
                     */
                    COMPLETED_BY_MEASURE__O__BOOLEAN__BOOL_LIMIT__NO_SPM,
                    /*
                    The value used as a threshold during measure-based evaluations of the activity’s completion status.

                    For a leaf <item> that references a SCO resource, the LMS shall use the minProgressMeasure value,
                    if provided, to initialize the cmi.completion_threshold data model element. This value will be used
                    by the SCO to determine completion when completedByMeasure is true.
                     */
                    MIN_PROGRESS_MEASURE__O__DECIMAL__RANGE_LIMIT__NO_SPM,
                    /*
                    This attribute indicates the weighting factor applied to the activity’s progress measure
                    used during completion rollup of the parent activity
                     */
                    PROGRESS_WEIGHT__O__DECIMAL__RANGE_LIMIT__NO_SPM),
            Collections.emptyList()
    );

    /**
     * The <dataFromLMS> element provides initialization data expected by the resource (i.e., SCO)
     * represented by the <item> after launch. This data is opaque to the LMS and only has functional
     * meaning to the SCO. This element shall not be used for parameters that the SCO may need during
     * the launch (query string parameters). If this type of functionality is required, then the
     * developer should use the parameters attribute of the item referencing the SCO resource.
     *
     * The LMS shall use the value of the <dataFromLMS> element, if provided, to initialize
     * the cmi.launch_data data model element
     */

    public static final Element DATA_FROM_LMS__IN_ITEM = new Element(
            "dataFromLMS",
            NAMESPACE_ADLCP,
            PREFIX_ADLCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            4000,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <timeLimitAction> element defines the action that should be taken when the maximum time
     * allowed in the current attempt of the activity is exceeded. All time tracking and time limit
     * actions are controlled by the SCO.
     *
     * The LMS shall use the value of the <timeLimitAction> element, if provided, to initialize the
     * cmi.time_limit_action data model element.
     */
    public static final Element TIME_LIMIT_ACTION__IN_ITEM = new Element(
            "timeLimitAction",
            NAMESPACE_ADLCP,
            PREFIX_ADLCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.TIME_LIMIT_ACTION_IN_ITEM_LIMIT,
            -1,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <metadata> element contains metadata describing the item. It contains relevant information
     * that describes the <item> element (i.e., activity) as a whole. The <metadata> element is
     * considered the root node for metadata describing the activity. This means that all metadata for
     * the activity is defined as a child of the <metadata> element.
     */
    public static final Element METADATA__IN_ITEM = new Element(
            "metadata",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_ONE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList() // TODO: {Metadata}
    );

    /**
     * The <title> element describes the title of the item.
     */
    public static final Element TITLE__IN_ITEM = new Element(
            "title",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ZERO,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            200,
            Collections.emptyList(),
            Collections.emptyList());

    /**
     * The <item> element can be nested an arbitrarily number of levels. This is typically based on
     * the content structure of the aggregation. The <item> element can appear 0 or More times as a
     * child of an <item> element
     */
    public static final Element ITEM__IN_ITEM = new Element(
            "item",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ZERO_OR_MORE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    An identifier attribute is an identifier,for the item,
                    that is unique within the manifest.
                     */
                    IDENTIFIER__M__ID__NO_LIMIT__NO_SPM,
                    /*
                    The identifierref attribute is a reference to an identifier
                    in the resources section or a (sub)manifest.
                     */
                    IDENTIFIERREF__O__STRING__NO_LIMIT__2000,
                    /*
                    The isvisible attribute indicates whether or not this item
                    is displayed when the structure of the package is displayed or rendered.
                    The value only affects the item for which it is defined and not the
                    children of the item or a resource associated with an item.
                     */
                    ISVISIBLE__O__BOOLEAN__BOOL_LIMIT__NO_SPM,
                    /*
                    The parameters attribute contains the static parameters to be passed to
                    the resource at launch time. The parameters attribute should only be used
                    for <item> elements that reference <resource> elements.
                     */
                    PARAMETERS__O__STRING__FORMAT_LIMIT__1000),
            Arrays.asList(
                    TITLE__IN_ITEM,
                    METADATA__IN_ITEM,
                    TIME_LIMIT_ACTION__IN_ITEM,
                    DATA_FROM_LMS__IN_ITEM,
                    COMPLETION_THRESHOLD__IN_ITEM,
                    DATA__IN_ITEM) // TODO: imsss:sequencing, adlnav:presentation
    );
    static {
        ITEM__IN_ITEM.getElements().add(ITEM__IN_ITEM);
    }

    /**
     * The <item> element is a node that describes the hierarchical structure of the organization.
     * The <item> element represents an activity in the content organization. The <item> element
     * describes a node within the organization’s structure. The <item> element can be nested and
     * repeated within other <item> elements to any number of levels. This structuring of <item>
     * elements shapes the content organization and describes the relationships between parts
     * of the learning content.
     *
     * The <item> element can act as a container of other <item> elements or as a leaf node. If an
     * <item> is a leaf node, then the <item> shall reference a <resource> element. If an <item>
     * element is a parent element, the <item> itself is not permitted to reference a <resource>
     * element (only leaf <item> elements are permitted to reference resources).
     */
    public static final Element ITEM__IN_ORGANIZATION = new Element(
            "item",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_OR_MORE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    An identifier attribute is an identifier,for the item,
                    that is unique within the manifest.
                     */
                    IDENTIFIER__M__ID__NO_LIMIT__NO_SPM,
                    /*
                    The identifierref attribute is a reference to an identifier
                    in the resources section or a (sub)manifest.
                     */
                    IDENTIFIERREF__O__STRING__NO_LIMIT__2000,
                    /*
                    The isvisible attribute indicates whether or not this item
                    is displayed when the structure of the package is displayed or rendered.
                    The value only affects the item for which it is defined and not the
                    children of the item or a resource associated with an item.
                     */
                    ISVISIBLE__O__BOOLEAN__BOOL_LIMIT__NO_SPM,
                    /*
                    The parameters attribute contains the static parameters to be passed to
                    the resource at launch time. The parameters attribute should only be used
                    for <item> elements that reference <resource> elements.
                     */
                    PARAMETERS__O__STRING__FORMAT_LIMIT__1000),
            Arrays.asList(
                    TITLE__IN_ITEM,
                    ITEM__IN_ITEM,
                    METADATA__IN_ITEM,
                    TIME_LIMIT_ACTION__IN_ITEM,
                    DATA_FROM_LMS__IN_ITEM,
                    COMPLETION_THRESHOLD__IN_ITEM,
                    DATA__IN_ITEM) // TODO: imsss:sequencing, adlnav:presentation
    );

    /**
     * The <title> element describes the title of the organization. This element could be used to
     * help a learner decide which organization to choose. Depending on what the organization
     * is describing, this title could be for a course, module, lesson, etc.
     */
    public static final Element TITLE__IN_ORGANIZATION = new Element(
            "title",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ZERO,
            false,
            XmlDataType.STRING,
            "",
            false,
            Limits.NONE_LIMIT,
            200,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <organization> element describes a particular hierarchical organization. The content
     * organization is defined by the <organization> element. The content organization is a conceptual
     * term. The content organization can be a lesson, module, course, chapter, etc. What a content
     * organization defines is dependent on an organization’scurriculartaxonomy. The <organization>
     * element represents an Activity in the terms of IMS SS.
     */
    public static final Element ORGANIZATION__IN_ORGANIZATIONS = new Element(
            "organization",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_OR_MORE,
            Multiplicity.ZERO,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    An identifier for the organization that is unique within the manifest file.
                     */
                    IDENTIFIER__M__ID__NO_LIMIT__NO_SPM,
                    /*
                    Describes the shape of the organization.
                     */
                    STRUCTURE__O__STRING__NO_LIMIT__200,
                    /*
                    This attribute indicates that any mapped global shared objectives defined in
                    sequencing information (refer to <sequencing> Element) are either global to
                    the learner and one experience of a content organization (false) or global for
                    the lifetime of the learner within the LMS (true) across all content organizations.
                     */
                    ADLSEQ_OBJECTIVES_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM,
                    /*
                    This attribute indicates that any shared data mapped across SCOs (refer to <adlcp:data>
                    Element) are either global to the learner and one experience of a content organization
                    (false) or global for the lifetime of the learner within the LMS (true).
                     */
                    ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM),
            Arrays.asList(
                    TITLE__IN_ORGANIZATION,
                    ITEM__IN_ORGANIZATION,
                    METADATA__IN_ORGANIZATION) // TODO: adlcp:completionThreshold, imsss:sequencing
    );

    /**
     * The <organizations> element describes one or more structures or organizations for the content package.
     * SCORM places a requirement that when building a Resource Content Package, this element is required
     * to be represented in the manifest as an empty element (i.e., <organizations/>). When building a
     * Content Aggregation Content Package, this element is required to contain at least one <organization>
     * sub-element.
     */
    public static final Element ORGANIZATIONS__IN_MANIFEST = new Element(
            "organizations",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.singletonList(DEFAULT__M__IDREF__NO_LIMIT__NO_SPM),
            Collections.singletonList(ORGANIZATION__IN_ORGANIZATIONS)
    );

    /**
     * The <schema> element describes the schema that defines and controls the manifest element.
     * Since this element is a child of the metadata describing the package, the element is used
     * to describe the schema that controls the requirements of the manifest.
     */
    public static final Element SCHEMA__IN_METADATA_IN_MANIFEST = new Element(
            "schema",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            false,
            XmlDataType.STRING,
            "",
            true,
            Limits.SCHEMA_IN_METADATA_IN_MANIFEST_LIMIT,
            -1,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <schemaversion> element describes the version of the above schema(<schema>)
     */
    public static final Element SCHEMA_VERSION__IN_METADATA_IN_MANIFEST = new Element(
            "schemaversion",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            false,
            XmlDataType.STRING,
            "",
            true,
            Limits.SCHEMA_VERSION_IN_METADATA_IN_MANIFEST_LIMIT,
            -1,
            Collections.emptyList(),
            Collections.emptyList()
    );

    /**
     * The <metadata> element contains metadata describing the manifest. It contains relevant information
     * that describes the content package (i.e., Content Aggregation) as a whole. The <metadata> element
     * is considered the root node for metadata defined in a content package. This means that all metadata
     * for a content package is defined as a child of the <metadata> element.
     */
    public static final Element METADATA__IN_MANIFEST = new Element(
            "metadata",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Collections.emptyList(),
            Arrays.asList(
                    SCHEMA__IN_METADATA_IN_MANIFEST,
                    SCHEMA_VERSION__IN_METADATA_IN_MANIFEST) // TODO: {Metadata}
    );

    /**
     * The <manifest> element represents a reusable unit of instruction that encapsulates metadata,
     * organizations and resource references. The <manifest> element is the root element node
     * in the imsmanifest.xml file. Subsequent occurrences of the <manifest> elements inside the
     * root <manifest> are used to compartmentalize files, metadata and organization structure
     * for aggregation, disaggregation and reuse. These child <manifest> elements are referred
     * to as (sub)manifests. In this version, ADL recommends not to use (sub)manifests.
     */
    public static final Element MANIFEST = new Element(
            "manifest",
            NAMESPACE_IMSCP,
            PREFIX_IMSCP,
            Multiplicity.ONE_AND_ONLY_ONE,
            Multiplicity.ONE_AND_ONLY_ONE,
            true,
            XmlDataType.CONTAINER,
            "",
            false,
            Limits.NONE_LIMIT,
            -1,
            Arrays.asList(
                    /*
                    The attribute identifies the manifest. The identifier
                    is unique within the manifest element
                     */
                    IDENTIFIER__M__ID__NO_LIMIT__NO_SPM,
                    /*
                    The version attribute identifies the version of the manifest.
                    It is used to distinguish between manifests with the same identifier.
                     */
                    VERSION__O__STRING__NO_LIMIT__20,
                    /*
                    The xml:base attribute provides a relative path offset for
                    the content file(s) contained in the manifest.
                     */
                    XML_BASE__O__ANY_URI__NO_LIMIT__2000),
            Arrays.asList(
                    METADATA__IN_MANIFEST,
                    ORGANIZATIONS__IN_MANIFEST,
                    RESOURCES__IN_MANIFEST) // manifest don't implementation. TODO: imsss:sequencingCollection
    );
    static {
        MANIFEST.getElements().add(MANIFEST);
    }

}
