package com.corkili.learningserver.scorm.cam.load;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.model.Annotation;
import com.corkili.learningserver.scorm.cam.model.Classification;
import com.corkili.learningserver.scorm.cam.model.CompletionThreshold;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Data;
import com.corkili.learningserver.scorm.cam.model.Dependency;
import com.corkili.learningserver.scorm.cam.model.Educational;
import com.corkili.learningserver.scorm.cam.model.File;
import com.corkili.learningserver.scorm.cam.model.General;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.LOM;
import com.corkili.learningserver.scorm.cam.model.LifeCycle;
import com.corkili.learningserver.scorm.cam.model.Manifest;
import com.corkili.learningserver.scorm.cam.model.ManifestMetadata;
import com.corkili.learningserver.scorm.cam.model.Map;
import com.corkili.learningserver.scorm.cam.model.MetaMetadata;
import com.corkili.learningserver.scorm.cam.model.Metadata;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Organizations;
import com.corkili.learningserver.scorm.cam.model.Relation;
import com.corkili.learningserver.scorm.cam.model.Resource;
import com.corkili.learningserver.scorm.cam.model.Resources;
import com.corkili.learningserver.scorm.cam.model.Rights;
import com.corkili.learningserver.scorm.cam.model.Technical;
import com.corkili.learningserver.scorm.common.CommonUtils;

public class ContentPackageValidator {

    private static final String[] VT_TIME_LIMIT_ACTION
            = {"exist,message", "exit,no message", "continue,message", "continue,no message"};
    private static final String[] VT_SCORM_TYPE
            = {"sco", "asset"};

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
            result &= (flag = validateLOM(lom));
            if (!flag && isAssert) {
                return false;
            }
        }

        result &= (flag = metadata.getLocationList().size() == metadata.getLomFromLocationList().size());
        if (!flag) {
            recordError("<manifest>...<metadata>.<adlcp:location>", "xml file referenced by \"location\" is not found");
            if (isAssert) {
                return false;
            }
        }

        for (LOM lom : metadata.getLomFromLocationList()) {
            result &= (flag = validateLOM(lom));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateLOM(LOM lom) {
        boolean result = true;
        boolean flag;

        if (lom.getGeneral() != null) {
            result = (flag = validateGeneral(lom.getGeneral()));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getLifeCycle() != null) {
            result &= (flag = validateLifeCycle(lom.getLifeCycle()));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getMetaMetadata() != null) {
            result &= (flag = validateMetaMetadata(lom.getMetaMetadata()));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getTechnical() != null) {
            result &= (flag = validateTechnical(lom.getTechnical()));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Educational educational : lom.getEducationalList()) {
            result &= (flag = validateEducational(educational));
            if (!flag && isAssert) {
                return false;
            }
        }

        if (lom.getRights() != null) {
            result &= (flag = validateRights(lom.getRights()));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Relation relation : lom.getRelationList()) {
            result &= (flag = validateRelation(relation));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Annotation annotation : lom.getAnnotationList()) {
            result &= (flag = validateAnnotation(annotation));
            if (!flag && isAssert) {
                return false;
            }
        }

        for (Classification classification : lom.getClassificationList()) {
            result &= (flag = validateClassification(classification));
            if (!flag && isAssert) {
                return false;
            }
        }

        return result;
    }

    private boolean validateGeneral(General general) {
        return false;
    }

    private boolean validateLifeCycle(LifeCycle lifeCycle) {
        return false;
    }

    private boolean validateMetaMetadata(MetaMetadata metaMetadata) {
        return false;
    }

    private boolean validateTechnical(Technical technical) {
        return false;
    }

    private boolean validateEducational(Educational educational) {
        return false;
    }

    private boolean validateRights(Rights rights) {
        return false;
    }

    private boolean validateRelation(Relation relation) {
        return false;
    }

    private boolean validateAnnotation(Annotation annotation) {
        return false;
    }

    private boolean validateClassification(Classification classification) {
        return false;
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
