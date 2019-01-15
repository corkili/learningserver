package com.corkili.learningserver.scorm.cam.xml;

import static com.corkili.learningserver.scorm.cam.xml.Attributes.ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.ADLSEQ_OBJECTIVES_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.DEFAULT__M__IDREF__NO_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.IDENTIFIER__M__ID__NO_LIMIT__NO_SPM;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.STRUCTURE__O__STRING__NO_LIMIT__200;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.VERSION__O__STRING__NO_LIMIT__20;
import static com.corkili.learningserver.scorm.cam.xml.Attributes.XML_BASE__O__ANY_URI__NO_LIMIT__2000;

import java.util.Arrays;
import java.util.Collections;

/**
 * All Element.
 */
public final class Elements {

    private static final String NAMESPACE_IMSCP = "http://www.imsglobal.org/xsd/imscp_v1p1";
    private static final String PREFIX_IMSCP = "imscp";

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
                    ADLCP_SHARED_DATA_GLOBAL_TO_SYSTEM__O__BOOLEAN__BOOL_LIMIT__NO_SPM),
            Arrays.asList()); // TODO: title, item, metadata, adlcp:completionThreshold, imsss:sequencing

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
            Collections.singletonList(ORGANIZATION__IN_ORGANIZATIONS));

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
            Collections.emptyList());

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
            Collections.emptyList());

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
                    SCHEMA_VERSION__IN_METADATA_IN_MANIFEST)); // TODO: {metadata}

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
                    ORGANIZATIONS__IN_MANIFEST)); // manifest don't implementation. TODO: orgas, ress, imss:seqC


}
