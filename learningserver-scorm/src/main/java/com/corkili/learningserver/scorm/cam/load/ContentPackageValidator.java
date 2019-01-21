package com.corkili.learningserver.scorm.cam.load;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.model.Annotation;
import com.corkili.learningserver.scorm.cam.model.Classification;
import com.corkili.learningserver.scorm.cam.model.CompletionThreshold;
import com.corkili.learningserver.scorm.cam.model.ConditionRule;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Contribute;
import com.corkili.learningserver.scorm.cam.model.Data;
import com.corkili.learningserver.scorm.cam.model.DateTime;
import com.corkili.learningserver.scorm.cam.model.Dependency;
import com.corkili.learningserver.scorm.cam.model.Duration;
import com.corkili.learningserver.scorm.cam.model.Educational;
import com.corkili.learningserver.scorm.cam.model.File;
import com.corkili.learningserver.scorm.cam.model.General;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.LOM;
import com.corkili.learningserver.scorm.cam.model.LanguageString;
import com.corkili.learningserver.scorm.cam.model.LanguageStrings;
import com.corkili.learningserver.scorm.cam.model.LifeCycle;
import com.corkili.learningserver.scorm.cam.model.Manifest;
import com.corkili.learningserver.scorm.cam.model.ManifestMetadata;
import com.corkili.learningserver.scorm.cam.model.Map;
import com.corkili.learningserver.scorm.cam.model.MetaMetadata;
import com.corkili.learningserver.scorm.cam.model.Metadata;
import com.corkili.learningserver.scorm.cam.model.OrComposite;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Organizations;
import com.corkili.learningserver.scorm.cam.model.Relation;
import com.corkili.learningserver.scorm.cam.model.Requirement;
import com.corkili.learningserver.scorm.cam.model.Resource;
import com.corkili.learningserver.scorm.cam.model.Resources;
import com.corkili.learningserver.scorm.cam.model.Rights;
import com.corkili.learningserver.scorm.cam.model.RuleCondition;
import com.corkili.learningserver.scorm.cam.model.RuleConditions;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.SequencingRules;
import com.corkili.learningserver.scorm.cam.model.Taxon;
import com.corkili.learningserver.scorm.cam.model.TaxonPath;
import com.corkili.learningserver.scorm.cam.model.Technical;
import com.corkili.learningserver.scorm.cam.model.Vocabulary;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;
import com.corkili.learningserver.scorm.cam.model.datatype.VCard;
import com.corkili.learningserver.scorm.common.CommonUtils;

public class ContentPackageValidator {

    private static final String[] VT_TIME_LIMIT_ACTION
            = {"exist,message", "exit,no message", "continue,message", "continue,no message"};
    private static final String[] VT_SCORM_TYPE
            = {"sco", "asset"};
    private static final String[] VT_GENERAL_STRUCTURE
            = {"atomic", "collection", "networked", "hierarchical", "linear"};
    private static final String[] VT_GENERAL_AGGREGATION_LEVEL
            = {"1", "2", "3", "4"};
    private static final String[] VT_LIFE_CYCLE_STATUS
            = {"draft", "final", "revised", "unavailable"};
    private static final String[] VT_LIFE_CYCLE_CONTRIBUTE_ROLE
            = {"author", "publisher", "unknown", "initiator", "terminator", "validator", "editor", "graphical designer",
            "technical implementer", "content provider", "technical validator", "educational validator", "script write",
            "instructional designer", "subject matter expert"};
    private static final String[] VT_META_METADATA_CONTRIBUTE_ROLE
            = {"creator", "validator"};
    private static final String[] VT_OR_COMPOSITE_TYPE
            = {"operating system", "browser"};
    private static final String[] VT_OR_COMPOSITE_NAME_OS
            = {"pc-dos", "ms-windows", "macos", "unix", "multi-os", "none"};
    private static final String[] VT_OR_COMPOSITE_NAME_B
            = {"any", "netscape communicator", "ms-internet explorer", "opera", "amaya"};
    private static final String[] VT_EDUCATIONAL_INTERACTIVITY_TYPE
            = {"active", "expositive", "mixed"};
    private static final String[] VT_EDUCATIONAL_LEARNING_RESOURCE_TYPE
            = {"exercise", "simulation", "questionnaire", "diagram", "figure", "graph", "index", "slide", "table",
            "narrative text", "exam", "experiment", "problem statement", "self assessment", "lecture"};
    private static final String[] VT_EDUCATIONAL_INTERACTIVITY_LEVEL
            = {"very low", "low", "medium", "high", "very high"};
    private static final String[] VT_EDUCATIONAL_SEMANTIC_DENSITY
            = {"very low", "low", "medium", "hight", "very high"};
    private static final String[] VT_EDUCATIONAL_INTENDED_END_USER_ROLE
            = {"teacher", "author", "learner", "manager"};
    private static final String[] VT_EDUCATIONAL_CONTEXT
            = {"higher education", "training", "other"};
    private static final String[] VT_EDUCATIONAL_DIFFICULTY
            = {"very easy", "easy", "medium" ,"difficult", "very difficulty"};
    private static final String[] VT_RIGHTS_COST
            = {"yes", "no"};
    private static final String[] VT_RIGHTS_COPYRIGHT_AND_OTHER_RESTRICTIONS
            = {"yes", "no"};
    private static final String[] VT_RELATION_KIND
            = {"ispartof", "haspart", "isversionof", "hasversion", "isformatof", "hasformat", "references",
            "isreferencedby", "isbasedon", "isbasisfor", "requires", "isrequiredby"};
    private static final String[] VT_CLASSIFICATION_PURPOSE
            = {"discipline", "idea", "prerequisite", "educational objective", "accessibility restrictions",
            "educational level", "skill level", "security level", "competency"};
    private static final String[] VT_SEQUENCING_RULES_CONDITION_COMBINATION
            = {"all", "any"};
    private static final String[] VT_SEQUENCING_RULES_RULE_CONDITION_OPERATOR
            = {"not", "noOp"};
    private static final String[] VT_SEQUENCING_RULES_RULE_CONDTION_CONDITION
            = {"satisfied", "objectiveStatusKnown", "objectiveMeasureKnown", "objectiveMeasureGreaterThan",
            "objectiveMeasureLessThan", "completed", "activityProgressKnown", "attempted", "attemptLimitExceeded",
            "timeLimitExceeded", "outsideAvailableTimeRange", "always"};
    private static final String[] VT_SEQUENCING_RULES_RULE_ACTION_PRE
            = {"skip", "disabled", "hiddenFromChoice", "stopForwardTraversal"};
    private static final String[] VT_SEQUENCING_RULES_RULE_ACTION_POST
            = {"exitParent", "exitAll", "retry", "retryAll", "continue", "previous"};
    private static final String[] VT_SEQUENCING_RULES_RULE_ACTION_EXIT
            = {"exit"};

    private boolean isAssert;
    private java.util.Map<String, List<String>> errors;
    private java.util.Map<String, List<String>> idMap;

    public ContentPackageValidator() {
        this(true);
    }

    public ContentPackageValidator(boolean isAssert) {
        this.isAssert = isAssert;
        this.errors = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    public boolean validate(ContentPackage contentPackage) {
        this.errors.clear();
        this.idMap.clear();
        if (contentPackage == null) {
            recordError("ContentPackage", "cannot be null");
            return false;
        }

        boolean result;

        // independent validation
        result = validateManifest(contentPackage.getManifest());
        if (!result && isAssert) {
            return false;
        }

        // dependent validation

        return result;
    }

    private boolean validateManifest(Manifest manifest) {
        if (manifest == null) {
            recordError("<manifest>", "must exist 1 and only 1 time");
            return false;
        }

        boolean result;
        boolean flag;

        result = (flag = !ModelUtils.isIDEmpty(manifest.getIdentifier()));
        if (!flag) {
            recordError("<manifest>.identifier", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            // TODO need to dv <manifest>.identifier -> unique within the manifest element
            saveId("<manifest>.identifier", manifest.getIdentifier().getValue());
        }

        if (!ModelUtils.isAnyUriEmpty(manifest.getXmlBase())) {
            result &= (flag = ModelUtils.isAnyUriFormatCorrect(manifest.getXmlBase()));
            if (!flag) {
                recordError("<manifest>.xml:base", "value format incorrect");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = validateManifestMetadata(manifest.getMetadata()));
        if (!flag) {
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = validateOrganizations(manifest.getOrganizations()));
        if (!flag) {
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = validateResources(manifest.getResources()));
        if (!flag) {
            if (isAssert) {
                return false;
            }
        }

        // TODO imsss:sequencingCollection

        return result;
    }

    private boolean validateManifestMetadata(ManifestMetadata manifestMetadata) {
        if (manifestMetadata == null) {
            recordError("<manifest>.<metadata>", "must exist 1 and only 1 time");
            return false;
        }

        boolean result;
        boolean flag;

        result = (flag = StringUtils.isNotBlank(manifestMetadata.getSchema()));
        if (!flag) {
            recordError("<manifest>.<metadata>.<schema>", "must exist 1 and only 1 time");
            if (isAssert) {
                return false;
            }
        } else {
            result = (flag = StringUtils.equals(manifestMetadata.getSchema(), "ADL SCORM"));
            if (!flag) {
                recordError("<manifest>.<metadata>.<schema>", "value must be \"ADL SCORM\"");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = StringUtils.isNotBlank(manifestMetadata.getSchemaVersion()));
        if (!flag) {
            recordError("<manifest>.<metadata>.<schemaversion>", "must exist 1 and only 1 time");
            if (isAssert) {
                return false;
            }
        } else {
            result &= (flag = StringUtils.equals(manifestMetadata.getSchemaVersion(), "2004 4th Edition"));
            if (!flag) {
                recordError("<manifest>.<metadata>.<schemaversion>", "value must be \"2004 4th Edition\"");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = valdiateMetadata(manifestMetadata.getMetadata()));
        if (!flag && isAssert) {
            return false;
        }

        return result;
    }

    private boolean validateOrganizations(Organizations organizations) {
        if (organizations == null) {
            recordError("<manifest>.<organizations>", "must exist 1 and only 1 time");
            return false;
        }

        boolean result;
        boolean flag;

        result = (flag = !ModelUtils.isIDRefEmpty(organizations.getDefaultOrganizationID()));
        if (!flag) {
            recordError("<manifest>.<organizations>.default", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            // TODO need to dv <manifest>.<organizations>.default -> must reference an identifier attribute of an <organization>
            // element that is a direct descendent of the <organizations> element
            saveId("<manifest>.<organizations>.default", organizations.getDefaultOrganizationID().getValue());
        }

        for (Organization organization : organizations.getOrganizationList()) {
            result &= (flag = validateOrganization(organization));
            if (!flag) {
                if (!isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateOrganization(Organization organization) {
        boolean result;
        boolean flag;

        // TODO need to dv <manifest>.<organizations>.<organization>.identifier -> unique within the manifest file
        result = (flag = !ModelUtils.isIDEmpty(organization.getIdentifier()));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.identifier", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            saveId("<manifest>.<organizations>.<organization>.identifier", organization.getIdentifier().getValue());
        }

        result &= (flag = StringUtils.isNotBlank(organization.getTitle()));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.<title>",
                    "must exist 1 and only 1 time and value not empty");
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = !organization.getItemList().isEmpty());
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.<item>", "must exist 1 or more times");
            if (isAssert) {
                return false;
            }
        }

        for (Item item : organization.getItemList()) {
            result &= (flag = validateItem(item));
            if (!flag) {
                if (isAssert) {
                    return false;
                }
            }
        }

        if (organization.getCompletionThreshold() != null) {
            result &= (flag = validateCompletionThreshold(organization.getCompletionThreshold()));
            if (!flag) {
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = valdiateMetadata(organization.getMetadata()));
        if (!flag && isAssert) {
            return false;
        }

        // TODO imsss:sequencing

        return result;
    }

    private boolean validateItem(Item item) {
        boolean result;
        boolean flag;

        result = (flag = !ModelUtils.isIDEmpty(item.getIdentifier()));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].identifier", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            // TODO need to dv <manifest>.<organizations>.<organization>.<item>[.<item>].identifier -> unique within the manifest
            saveId("<manifest>.<organizations>.<organization>.<item>[.<item>].identifier", item.getIdentifier().getValue());
        }

        if (item.getItemList().isEmpty()) { // is a leaf <item>
            result &= (flag = StringUtils.isNotBlank(item.getIdentifierref()));
            if (!flag) {
                recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].identifierref",
                        "a leaf <item> is required to reference a resource");
                if (isAssert) {
                    return false;
                }
            } else {
                // TODO need to dv <manifest>.<organizations>.<organization>.<item>[.<item>].identifierref -> reference to an
                // identifier in resources section
                // TODO need to dv the resource must be type=webcontent,scormType=sco|asset,href is required
                saveId("<manifest>.<organizations>.<organization>.<item>[.<item>].identifierref", item.getIdentifierref());
            }
        } else { // not a leaf <item>
            result &= (flag = StringUtils.isBlank(item.getIdentifierref()));
            if (!flag) {
                recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].identifierref",
                        "only a leaf <item> can reference a resource");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (StringUtils.isNotBlank(item.getParameters())) {
            result &= (flag = ModelUtils.isParametersFormatCorrect(item.getParameters()));
            if (!flag) {
                recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].parameters",
                        "invalid format defined in RFC 3986");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = StringUtils.isNotBlank(item.getTitle()));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].<title>",
                    "must exist 1 and only 1 time");
            if (isAssert) {
                return false;
            }
        }

        for (Item nestedItem : item.getItemList()) {
            result &= (flag = validateItem(nestedItem));
            if (!flag & isAssert) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(item.getTimeLimitAction())) {
            result &= (flag = ModelUtils.isLegalVocabulary(item.getTimeLimitAction(), VT_TIME_LIMIT_ACTION));
            if (!flag) {
                recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].<adlcp:timeLimitAction>",
                        CommonUtils.format("value must be one of following tokens: {}", (Object) VT_TIME_LIMIT_ACTION));
                if (isAssert) {
                    return false;
                }
            }
        }

        if (item.getCompletionThreshold() != null) {
            result &= (flag = validateCompletionThreshold(item.getCompletionThreshold()));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (item.getData() != null) {
            result &= (flag = validateData(item.getData()));
            if (!flag & isAssert) {
                return false;
            }
        }

        result &= (flag = valdiateMetadata(item.getMetadata()));
        if (!flag && isAssert) {
            return false;
        }

        // TODO imsss:sequencing adlnav:presentation

        return result;
    }

    private boolean validateCompletionThreshold(CompletionThreshold completionThreshold) {
        boolean result;
        boolean flag;

        result = (flag = ModelUtils.isDecimalInRange(completionThreshold.getMinProgressMeasure(),
                0.0000, 1.0000, 4));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>[.<item>[.<item>]].<adlcp:completionThreshold>.minProgressMeasure",
                    "must in range [0.0000, 1.0000], with a precisions to at least 4 significant digits");
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = ModelUtils.isDecimalInRange(completionThreshold.getProgressWeight(),
                0.0000, 1.0000, 4));
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>[.<item>[.<item>]].<adlcp:completionThreshold>.progressWeight",
                    "must in range [0.0000, 1.0000], with a precisions to at least 4 significant digits");
            if (isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateData(Data data) {
        boolean result;
        boolean flag;

        result = (flag = !data.getMapList().isEmpty());
        if (!flag) {
            recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].<adlcp:data>.<adlcp:map>",
                    "must exist 1 or more times");
            if (isAssert) {
                return false;
            }
        }

        for (Map map : data.getMapList()) {
            result &= (flag = ModelUtils.isAnyUriFormatCorrect(map.getTargetID()));
            if (!flag) {
                recordError("<manifest>.<organizations>.<organization>.<item>[.<item>].<adlcp:data>.<adlcp:map>.targetID",
                        "must exist and has correct format");
                if (isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateResources(Resources resources) {
        if (resources == null) {
            recordError("<manifest>.<resources>", "must exist 1 and only 1 time");
            return false;
        }

        boolean result = true;
        boolean flag;

        if (!ModelUtils.isAnyUriEmpty(resources.getXmlBase())) {
            result = (flag = ModelUtils.isAnyUriFormatCorrect(resources.getXmlBase()));
            if (!flag) {
                recordError("<manifest>.<resources>.xml:base", "value format incorrect");
                if (isAssert) {
                    return false;
                }
            }
        }

        for (Resource resource : resources.getResourceList()) {
            result &= (flag = validateResource(resource));
            if (!flag & isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateResource(Resource resource) {
        boolean result;
        boolean flag;

        result = (flag = !ModelUtils.isIDEmpty(resource.getIdentifier()));
        if (!flag) {
            recordError("<manifest>.<resources>.<resource>.identifier", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            // TODO need to dv <manifest>.<resources>.<resource>.identifier -> unique within the scope of its containing manifest
            saveId("<manifest>.<resources>.<resource>.identifier", resource.getIdentifier().getValue());
        }

        result &= (flag = StringUtils.isNotBlank(resource.getType()));
        if (!flag) {
            recordError("<manifest>.<resources>.<resource>.type", "must exist");
            if (isAssert) {
                return false;
            }
        }

        if (!ModelUtils.isAnyUriEmpty(resource.getXmlBase())) {
            result = (flag = ModelUtils.isAnyUriFormatCorrect(resource.getXmlBase()));
            if (!flag) {
                recordError("<manifest>.<resources>.<resource>.xml:base", "value format incorrect");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = StringUtils.isNotBlank(resource.getScormType()));
        if (!flag) {
            recordError("<manifest>.<resources>.<resource>.adlcp:scormType", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            result &= (flag = ModelUtils.isLegalVocabulary(resource.getScormType(), VT_SCORM_TYPE));
            if (!flag) {
                recordError("<manifest>.<resources>.<resource>.adlcp:scormType", CommonUtils.format(
                        "value must be one of the following tokens: {}", (Object) VT_SCORM_TYPE));
            }
        }

        for (File file : resource.getFileList()) {
            result &= (flag = validateFile(file));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Dependency dependency : resource.getDependencyList()) {
            result &= (flag = StringUtils.isNotBlank(dependency.getIdentifierref()));
            if (!flag) {
                recordError("<manifest>.<resources>.<resource>.<dependency>.identifierref", "must exist");
                if (isAssert) {
                    return false;
                }
            } else {
                // TODO need to dv <manifest>.<resources>.<resource>.<dependency>.identifierref -> reference a <resource>
                saveId("<manifest>.<resources>.<resource>.<dependency>.identifierref", dependency.getIdentifierref());
            }
        }

        result &= (flag = valdiateMetadata(resource.getMetadata()));
        if (!flag && isAssert) {
            return false;
        }

        return result;
    }

    private boolean validateFile(File file) {
        boolean result;
        boolean flag;

        result = (flag = StringUtils.isNotBlank(file.getHref()));
        if (!flag) {
            recordError("<manifest>.<resources>.<resource>.<file>.href", "must exist");
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = valdiateMetadata(file.getMetadata()));
        if (!flag && isAssert) {
            return false;
        }

        return result;
    }

    private boolean valdiateMetadata(Metadata metadata) {
        if (metadata == null) {
            return false;
        }

        boolean result = true;
        boolean flag;

        for (LOM lom : metadata.getLomList()) {
            result &= (flag = validateLOM(lom, "<manifest>...<metadata>.<lom>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        result &= (flag = !metadata.getLocationLomMap().containsValue(null));
        if (!flag) {
            recordError("<manifest>...<metadata>.<adlcp:location>", "xml file referenced by \"location\" is not found");
            if (isAssert) {
                return false;
            }
        }

        for (java.util.Map.Entry<String, LOM> entry : metadata.getLocationLomMap().entrySet()) {
            result &= (flag = validateLOM(entry.getValue(), "<lom>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateLOM(LOM lom, String baseTag) {
        boolean result = true;
        boolean flag;

        if (lom.getGeneral() != null) {
            result = (flag = validateGeneral(lom.getGeneral(), baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getLifeCycle() != null) {
            result &= (flag = validateLifeCycle(lom.getLifeCycle(), baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getMetaMetadata() != null) {
            result &= (flag = validateMetaMetadata(lom.getMetaMetadata(), baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getTechnical() != null) {
            result &= (flag = validateTechnical(lom.getTechnical(), baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Educational educational : lom.getEducationalList()) {
            result &= (flag = validateEducational(educational, baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getRights() != null) {
            result &= (flag = validateRights(lom.getRights(), baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Relation relation : lom.getRelationList()) {
            result &= (flag = validateRelation(relation, baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Annotation annotation : lom.getAnnotationList()) {
            result &= (flag = validateAnnotation(annotation, baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Classification classification : lom.getClassificationList()) {
            result &= (flag = validateClassification(classification, baseTag));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateGeneral(General general, String baseTag) {
        boolean result = true;
        boolean flag;

        if (general.getTitle() != null) {
            result = (flag = validateLanguageStrings(general.getTitle(), baseTag + ".<general>.<title>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (String language : general.getLanguageList()) {
            result &= (flag = ModelUtils.isLegalLanguage(language));
            if (!flag) {
                recordError(baseTag + ".<general>.<language>", "invalid language code");
                if (isAssert) {
                    return false;
                }
            }
        }

        for (LanguageStrings languageStrings : general.getDescriptionList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<general>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : general.getKeywordList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<general>.<keyword>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : general.getCoverageList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<general>.<coverage>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (general.getStructure() != null) {
            result &= (flag = validateVocabulary(general.getStructure(), VT_GENERAL_STRUCTURE,
                    baseTag + ".<general>.<structure>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (general.getAggregationLevel() != null) {
            result &= (flag = validateVocabulary(general.getAggregationLevel(), VT_GENERAL_AGGREGATION_LEVEL,
                    baseTag + ".<general>.<aggregationLevel>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateLifeCycle(LifeCycle lifeCycle, String baseTag) {
        boolean result = true;
        boolean flag;

        if (lifeCycle.getVersion() != null) {
            result = (flag = validateLanguageStrings(lifeCycle.getVersion(), baseTag + ".<lifeCycle>.<version>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lifeCycle.getStatus() != null) {
            result &= (flag = validateVocabulary(lifeCycle.getStatus(), VT_LIFE_CYCLE_STATUS,
                    baseTag + ".<lifeCycle>.<status>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Contribute contribute : lifeCycle.getContributeList()) {
            result &= (flag = validateContribute(contribute, VT_LIFE_CYCLE_CONTRIBUTE_ROLE,
                    baseTag + ".<lifeCycle>.<contribute>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateMetaMetadata(MetaMetadata metaMetadata, String baseTag) {
        boolean result = true;
        boolean flag;

        for (Contribute contribute : metaMetadata.getContributeList()) {
            result &= (flag = validateContribute(contribute, VT_META_METADATA_CONTRIBUTE_ROLE,
                    baseTag + ".<metaMetadata>.<contribute>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (StringUtils.isNotBlank(metaMetadata.getLanguage())) {
            result &= (flag = ModelUtils.isLegalLanguage(metaMetadata.getLanguage()));
            if (!flag) {
                recordError(baseTag + ".<metaMetadata>.<language>", "value invalid language code");
                if (isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateTechnical(Technical technical, String baseTag) {
        boolean result = true;
        boolean flag;

        if (technical.getSize() != null) {
            result = (flag = technical.getSize().matches("^[0-9]\\d*$"));
            if (!flag) {
                recordError(baseTag + ".<technical>.<size>", "must be a non-negative-integer");
                if (isAssert) {
                    return false;
                }
            }
        }

        for (Requirement requirement : technical.getRequirementList()) {
            result &= (flag = validateRequirement(requirement, baseTag + ".<technical>.<requirement>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (technical.getInstallationRemarks() != null) {
            result &= (flag = validateLanguageStrings(technical.getInstallationRemarks(),
                    baseTag + ".<technical>.<installationRemarks>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (technical.getOtherPlatformRequirements() != null) {
            result &= (flag = validateLanguageStrings(technical.getOtherPlatformRequirements(),
                    baseTag + ".<technical>.<otherPlatformRequirements>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (technical.getDuration() != null) {
            result &= (flag = validateDuration(technical.getDuration(), baseTag + ".<technical>.<duration>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateEducational(Educational educational, String baseTag) {
        boolean result = true;
        boolean flag;

        if (educational.getInteractivityType() != null) {
            result = (flag = validateVocabulary(educational.getInteractivityType(), VT_EDUCATIONAL_INTERACTIVITY_TYPE,
                    baseTag + ".<educational>.<interactivityType>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Vocabulary vocabulary : educational.getLearningResourceTypeList()) {
            result &= (flag = validateVocabulary(vocabulary, VT_EDUCATIONAL_LEARNING_RESOURCE_TYPE,
                    baseTag + ".<educational>.<learningResourceType>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (educational.getInteractivityLevel() != null) {
            result &= (flag = validateVocabulary(educational.getInteractivityType(), VT_EDUCATIONAL_INTERACTIVITY_LEVEL,
                    baseTag + ".<educational>.<interactivityLevel>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (educational.getSemanticDensity() != null) {
            result &= (flag = validateVocabulary(educational.getInteractivityType(), VT_EDUCATIONAL_SEMANTIC_DENSITY,
                    baseTag + ".<educational>.<semanticDensity>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Vocabulary vocabulary : educational.getIntendedEndUserRoleList()) {
            result &= (flag = validateVocabulary(vocabulary, VT_EDUCATIONAL_INTENDED_END_USER_ROLE,
                    baseTag + ".<educational>.<intendedEndUserRole>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Vocabulary vocabulary : educational.getContextList()) {
            result &= (flag = validateVocabulary(vocabulary, VT_EDUCATIONAL_CONTEXT,
                    baseTag + ".<educational>.<context>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : educational.getTypicalAgeRangeList()) {
            result &= (flag = validateLanguageStrings(languageStrings,
                    baseTag + ".<educational>.<typicalAgeRange>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (educational.getDifficulty() != null) {
            result &= (flag = validateVocabulary(educational.getDifficulty(), VT_EDUCATIONAL_DIFFICULTY,
                    baseTag + ".<educational>.<difficulty>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (educational.getTypicalLearningTime() != null) {
            result &= (flag = validateDuration(educational.getTypicalLearningTime(),
                    baseTag + ".<educational>.<typicalLearningTime>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : educational.getDescriptionList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<educational>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (String language : educational.getLanguageList()) {
            result &= (flag = ModelUtils.isLegalLanguage(language));
            if (!flag) {
                recordError(baseTag + ".<educational>.<language>", "value invalid language code");
                if (isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateRights(Rights rights, String baseTag) {
        boolean result = true;
        boolean flag;

        if (rights.getCost() != null) {
            result = (flag = validateVocabulary(rights.getCost(), VT_RIGHTS_COST, baseTag + ".<rights>.<cost>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (rights.getCopyrightAndOtherRestrictions() != null) {
            result &= (flag = validateVocabulary(rights.getCost(), VT_RIGHTS_COPYRIGHT_AND_OTHER_RESTRICTIONS,
                    baseTag + ".<rights>.<copyrightAndOtherRestrictions>"));
            if (!flag && isAssert) {
                return false;
            }
        }


        if (rights.getDescription() != null) {
            result &= (flag = validateLanguageStrings(rights.getDescription(), baseTag + ".<rights>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateRelation(Relation relation, String baseTag) {
        boolean result = true;
        boolean flag;

        if (relation.getKind() != null) {
            result = (flag = validateVocabulary(relation.getKind(), VT_RELATION_KIND,
                    baseTag + ".<relation>.<kind>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : relation.getResource().getDescriptionList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<relation>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateAnnotation(Annotation annotation, String baseTag) {
        boolean result = true;
        boolean flag;

        if (ModelUtils.isVCardEmpty(annotation.getEntity())) {
            result = (flag = ModelUtils.isLegalVCard(annotation.getEntity()));
            if (!flag) {
                recordError(baseTag + ".<annotation>.<entity>", "value format incorrect");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (annotation.getDate() != null) {
            result &= (flag = validateDateTime(annotation.getDate(), baseTag + ".<annotation>.<date>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (annotation.getDescription() != null) {
            result &= (flag = validateLanguageStrings(annotation.getDescription(),
                    baseTag + ".<annotation>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateClassification(Classification classification, String baseTag) {
        boolean result = true;
        boolean flag;

        if (classification.getPurpose() != null) {
            result = (flag = validateVocabulary(classification.getPurpose(), VT_CLASSIFICATION_PURPOSE,
                    baseTag + ".<classification>.<purpose>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (TaxonPath taxonPath : classification.getTaxonPathList()) {
            result &= (flag = validateTaxonPath(taxonPath, baseTag + ".<classification>.<taxonPath>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (classification.getDescription() != null) {
            result &= (flag = validateLanguageStrings(classification.getDescription(),
                    baseTag + ".<classification>.<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (LanguageStrings languageStrings : classification.getKeywordList()) {
            result &= (flag = validateLanguageStrings(languageStrings, baseTag + ".<classification>.<keyword>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateContribute(Contribute contribute, String[] roleVT, String baseTag) {
        boolean result = true;
        boolean flag;

        if (contribute.getRole() != null) {
            result = (flag = validateVocabulary(contribute.getRole(), roleVT, baseTag + ".<role>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (VCard vCard : contribute.getEntityList()) {
            result &= (flag = ModelUtils.isLegalVCard(vCard));
            if (!flag) {
                recordError(baseTag + ".<entity>", "invalid vCard format");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (contribute.getDate() != null) {
            result &= (flag = validateDateTime(contribute.getDate(), baseTag + ".<date>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateRequirement(Requirement requirement, String baseTag) {
        boolean result = true;
        boolean flag;

        for (OrComposite orComposite : requirement.getOrCompositeList()) {
            result &= (flag = validateOrComposite(orComposite, baseTag + ".<orComposite>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateOrComposite(OrComposite orComposite, String baseTag) {
        boolean result = true;
        boolean flag;

        if (orComposite.getType() != null) {
            result = (flag = validateVocabulary(orComposite.getType(), VT_OR_COMPOSITE_TYPE,
                    baseTag + ".<type>"));
            if (!flag && isAssert) {
                return false;
            } else if (orComposite.getName() != null) {
                String[] vt = orComposite.getType().getValue().equals("browser") ?
                        VT_OR_COMPOSITE_NAME_B : VT_OR_COMPOSITE_NAME_OS;
                result &= (flag = validateVocabulary(orComposite.getName(), vt,
                        CommonUtils.format(baseTag + ".<name>{type={}}", orComposite.getType().getValue())));
                if (!flag && isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateTaxonPath(TaxonPath taxonPath, String baseTag) {
        boolean result = true;
        boolean flag;

        if (taxonPath.getSource() != null) {
            result = (flag = validateLanguageStrings(taxonPath.getSource(), baseTag + ".<source>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Taxon taxon : taxonPath.getTaxonList()) {
            result &= (flag = validateTaxon(taxon, baseTag + ".<taxon>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateTaxon(Taxon taxon, String baseTag) {
        boolean result = true;
        boolean flag;

        if (taxon.getEntry() != null) {
            result = (flag = validateLanguageStrings(taxon.getEntry(), baseTag + ".<entry>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateLanguageStrings(LanguageStrings languageStrings, String baseTag) {
        boolean result = true;
        boolean flag;

        for (LanguageString languageString : languageStrings.getLanguageStringList()) {
            result &= (flag = ModelUtils.isLegalLanguage(languageString.getLanguage()));
            if (!flag) {
                recordError(baseTag + ".<string>.language", "invalid value");
                if (isAssert) {
                    return false;
                }
            }
        }

        return result;
    }

    private boolean validateVocabulary(Vocabulary vocabulary, String[] vt, String baseTag) {
        boolean result;
        boolean flag;

        result = (flag = StringUtils.equals("LOMv1.0", vocabulary.getSource()));
        if (!flag) {
            recordError(baseTag + ".<source>", "value must be LOMv1.0");
            if (isAssert) {
                return false;
            }
        }

        result &= (flag = ModelUtils.isLegalVocabulary(vocabulary.getValue(), vt));
        if (!flag) {
            recordError(baseTag + ".<value>", CommonUtils.format(
                    "value must be one of the following tokens:", (Object) vt));
            if (isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateDateTime(DateTime dateTime, String baseTag) {
        boolean result = true;
        boolean flag;

        if (StringUtils.isNotBlank(dateTime.getDateTime())) {
            result = (flag = ModelUtils.isDateTimeFormatCorrect(dateTime.getDateTime()));
            if (!flag) {
                recordError(baseTag + ".<dateTime>", "invalid dateTime format");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (dateTime.getDescription() != null) {
            result &= (flag = validateLanguageStrings(dateTime.getDescription(), baseTag + ".<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateDuration(Duration duration, String baseTag) {
        boolean result = true;
        boolean flag;

        if (StringUtils.isNotBlank(duration.getDuration())) {
            result = (flag = ModelUtils.isDurationFormatCorrect(duration.getDuration()));
            if (!flag) {
                recordError(baseTag + ".<duration>", "invalid duration format");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (duration.getDescription() != null) {
            result &= (flag = validateLanguageStrings(duration.getDescription(), baseTag + ".<description>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateSequencing(Sequencing sequencing, boolean isInSequencingCollection) {
        boolean result;
        boolean flag;

        String baseTag = isInSequencingCollection ? "<manifest>.<imsss:sequencingCollection>.<imsss:sequencing>" :
                "<manifest>.<organizations>.<organization>[.<item>[.<item>]].<imsss:sequencing>";

        result = (flag = ModelUtils.isIDEmpty(sequencing.getId()));
        if (isInSequencingCollection) {
            if (flag) {
                recordError(baseTag + ".ID", "must exist");
                if (isAssert) {
                    return false;
                }
            } else {
                // TODO need to dv <manifest>.<imsss:sequencingCollection>.<imsss:sequencing>.ID -> unique
                saveId(baseTag + ".ID", sequencing.getId().getValue());
            }
        } else {
            if (!flag) {
                recordError(baseTag + ".ID", "cannot exist");
                if (isAssert) {
                    return false;
                }
            }
        }

        if (!ModelUtils.isIDRefEmpty(sequencing.getIdRef())) {
            // TODO need to dv baseTag.IDRef -> reference sequencing id
            saveId(baseTag + ".IDRef", sequencing.getIdRef().getValue());
        }

        if (sequencing.getSequencingRules() != null) {
            result &= (flag = validateSequencingRules(sequencing.getSequencingRules(),
                    baseTag + ".<sequencingRules>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateSequencingRules(SequencingRules sequencingRules, String baseTag) {
        boolean result = true;
        boolean flag;

        for (ConditionRule conditionRule : sequencingRules.getPreConditionRuleList()) {
            result &= (flag = validateConditionRule(conditionRule, VT_SEQUENCING_RULES_RULE_ACTION_PRE,
                    baseTag + ".<preConditionRule>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (ConditionRule conditionRule : sequencingRules.getPostConditionRuleList()) {
            result &= (flag = validateConditionRule(conditionRule, VT_SEQUENCING_RULES_RULE_ACTION_POST,
                    baseTag + ".<postConditionRule>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (ConditionRule conditionRule : sequencingRules.getExitConditionRuleList()) {
            result &= (flag = validateConditionRule(conditionRule, VT_SEQUENCING_RULES_RULE_ACTION_EXIT,
                    baseTag + ".<exitConditionRule>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateConditionRule(ConditionRule conditionRule, String[] actionVT, String baseTag) {
        boolean result;
        boolean flag;

        result = (flag = validateRuleConditions(conditionRule.getRuleConditions(), baseTag + ".<ruleConditions>"));
        if (!flag && isAssert) {
            return false;
        }

        result &= (flag = conditionRule.getRuleAction() != null && conditionRule.getRuleAction().getAction() != null);
        if (!flag) {
            recordError(baseTag + ".<ruleAction>.action", "must exist 1 and only 1 time");
            if (isAssert) {
                return false;
            }
        } else {
            result &= (flag = validateToken(conditionRule.getRuleAction().getAction(), actionVT,
                    baseTag + ".<ruleAction>.action"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateRuleConditions(RuleConditions ruleConditions, String baseTag) {
        if (ruleConditions == null) {
            recordError(baseTag, "must exist 1 and only 1 time");
            return false;
        }
        boolean result = true;
        boolean flag;

        if (!ModelUtils.isTokenEmpty(ruleConditions.getConditionCombination())) {
            result = (flag = validateToken(ruleConditions.getConditionCombination(),
                    VT_SEQUENCING_RULES_CONDITION_COMBINATION, baseTag + ".conditionCombination"));
            if (!flag && isAssert) {
                return false;
            }
        }

        result &= (flag = !ruleConditions.getRuleConditionList().isEmpty());
        if (!flag) {
            recordError(baseTag + ".<ruleCondition>", "must exist 1 or more times");
            if (isAssert) {
                return false;
            }
        }

        for (RuleCondition ruleCondition : ruleConditions.getRuleConditionList()) {
            result &= (flag = validateRuleCondition(ruleCondition, baseTag + ".<ruleCondition>"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateRuleCondition(RuleCondition ruleCondition, String baseTag) {
        boolean result = true;
        boolean flag;

        if (StringUtils.isNotBlank(ruleCondition.getReferencedObjective())) {
            // TODO need to dv
            saveId(baseTag + ".referencedObjective", ruleCondition.getReferencedObjective());
        }

        if (!ModelUtils.isDecimalEmpty(ruleCondition.getMeasureThreshold())) {
            result = (flag = ModelUtils.isDecimalInRange(ruleCondition.getMeasureThreshold(),
                    -1.0000, 1.0000, 4));
            if (!flag) {
                recordError(baseTag + ".measureThreshold",
                        "must in range [-1.0000, 1.0000], with a precision of at least 4 decimal places");
                if (isAssert) {
                    return false;
                }
            }
        }

        result &= (flag = validateToken(ruleCondition.getOperator(), VT_SEQUENCING_RULES_RULE_CONDITION_OPERATOR,
                baseTag + ".operator"));
        if (!flag && isAssert) {
            return false;
        }

        result &= (flag = !ModelUtils.isTokenEmpty(ruleCondition.getCondition()));
        if (!flag) {
            recordError(baseTag + ".condition", "must exist");
            if (isAssert) {
                return false;
            }
        } else {
            result &= (flag = validateToken(ruleCondition.getCondition(), VT_SEQUENCING_RULES_RULE_CONDTION_CONDITION,
                    baseTag + ".condition"));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateToken(Token token, String[] vt, String tag) {
        boolean result = ModelUtils.isLegalToken(token, vt);
        if (!result) {
            recordError(tag, CommonUtils.format("must be one of the following tokens: {}", (Object) vt));
        }
        return result;
    }

    private void recordError(String tag, String errorMsg) {
        errors.getOrDefault(tag, new LinkedList<>())
                .add(CommonUtils.format("{} -> {}.", tag, errorMsg));
    }

    private void saveId(String tag, String id) {
        idMap.getOrDefault(tag, new LinkedList<>()).add(id);
    }

    public boolean isAssert() {
        return isAssert;
    }

    public void setAssert(boolean anAssert) {
        isAssert = anAssert;
    }

    public java.util.Map<String, List<String>> getErrors() {
        java.util.Map<String, List<String>> res = new HashMap<>();
        errors.forEach((k, v) -> res.put(k, Collections.unmodifiableList(v)));
        return Collections.unmodifiableMap(res);
    }

}
