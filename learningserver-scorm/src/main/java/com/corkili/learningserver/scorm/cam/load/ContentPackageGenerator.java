package com.corkili.learningserver.scorm.cam.load;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.model.AdlseqMapInfo;
import com.corkili.learningserver.scorm.cam.model.AdlseqObjective;
import com.corkili.learningserver.scorm.cam.model.AdlseqObjectives;
import com.corkili.learningserver.scorm.cam.model.Annotation;
import com.corkili.learningserver.scorm.cam.model.Classification;
import com.corkili.learningserver.scorm.cam.model.CompletionThreshold;
import com.corkili.learningserver.scorm.cam.model.ConditionRule;
import com.corkili.learningserver.scorm.cam.model.ConstrainedChoiceConsiderations;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Contribute;
import com.corkili.learningserver.scorm.cam.model.ControlMode;
import com.corkili.learningserver.scorm.cam.model.Data;
import com.corkili.learningserver.scorm.cam.model.DateTime;
import com.corkili.learningserver.scorm.cam.model.DeliveryControls;
import com.corkili.learningserver.scorm.cam.model.Dependency;
import com.corkili.learningserver.scorm.cam.model.Duration;
import com.corkili.learningserver.scorm.cam.model.Educational;
import com.corkili.learningserver.scorm.cam.model.General;
import com.corkili.learningserver.scorm.cam.model.HideLMSUI;
import com.corkili.learningserver.scorm.cam.model.Identifier;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.LOM;
import com.corkili.learningserver.scorm.cam.model.LanguageString;
import com.corkili.learningserver.scorm.cam.model.LanguageStrings;
import com.corkili.learningserver.scorm.cam.model.LifeCycle;
import com.corkili.learningserver.scorm.cam.model.LimitConditions;
import com.corkili.learningserver.scorm.cam.model.Manifest;
import com.corkili.learningserver.scorm.cam.model.ManifestMetadata;
import com.corkili.learningserver.scorm.cam.model.MapInfo;
import com.corkili.learningserver.scorm.cam.model.MetaMetadata;
import com.corkili.learningserver.scorm.cam.model.Metadata;
import com.corkili.learningserver.scorm.cam.model.NavigationInterface;
import com.corkili.learningserver.scorm.cam.model.Objective;
import com.corkili.learningserver.scorm.cam.model.Objectives;
import com.corkili.learningserver.scorm.cam.model.OrComposite;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Organizations;
import com.corkili.learningserver.scorm.cam.model.Presentation;
import com.corkili.learningserver.scorm.cam.model.RandomizationControls;
import com.corkili.learningserver.scorm.cam.model.Relation;
import com.corkili.learningserver.scorm.cam.model.RelationResource;
import com.corkili.learningserver.scorm.cam.model.Requirement;
import com.corkili.learningserver.scorm.cam.model.Resource;
import com.corkili.learningserver.scorm.cam.model.Resources;
import com.corkili.learningserver.scorm.cam.model.Rights;
import com.corkili.learningserver.scorm.cam.model.RollupCondition;
import com.corkili.learningserver.scorm.cam.model.RollupConditions;
import com.corkili.learningserver.scorm.cam.model.RollupConsiderations;
import com.corkili.learningserver.scorm.cam.model.RollupRule;
import com.corkili.learningserver.scorm.cam.model.RollupRules;
import com.corkili.learningserver.scorm.cam.model.RuleAction;
import com.corkili.learningserver.scorm.cam.model.RuleCondition;
import com.corkili.learningserver.scorm.cam.model.RuleConditions;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.SequencingCollection;
import com.corkili.learningserver.scorm.cam.model.SequencingRules;
import com.corkili.learningserver.scorm.cam.model.Taxon;
import com.corkili.learningserver.scorm.cam.model.TaxonPath;
import com.corkili.learningserver.scorm.cam.model.Technical;
import com.corkili.learningserver.scorm.cam.model.Vocabulary;
import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;
import com.corkili.learningserver.scorm.cam.model.datatype.ID;
import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;
import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;
import com.corkili.learningserver.scorm.cam.model.datatype.VCard;
import com.corkili.learningserver.scorm.common.CommonUtils;

@Slf4j
public class ContentPackageGenerator {

    private static final String NAMESPACE_IMSCP = "http://www.imsglobal.org/xsd/imscp_v1p1";
    private static final String PREFIX_IMSCP = "imscp";

    private static final String NAMESPACE_ADLCP = "http://www.adlnet.org/xsd/adlcp_v1p3";
    private static final String PREFIX_ADLCP = "adlcp";

    private static final String NAMESPACE_ADLSEQ = "http://www.adlnet.org/xsd/adlseq_v1p3";
    private static final String PREFIX_ADLSEQ = "adlseq";

    private static final String NAMESPACE_ADLNAV = "http://www.adlnet.org/xsd/adlnav_v1p3";
    private static final String PREFIX_ADLNAV = "adlnav";

    private static final String NAMESPACE_IMSSS = "http://www.imsglobal.org/xsd/imsss";
    private static final String PREFIX_IMSSS = "imsss";

    private static final String NAMESPACE_LOM = "http://ltsc.ieee.org/xsd/LOM";
    private static final String PREFIX_LOM = "lom";

    private static final String NAMESPACE_XML = "http://www.w3.org/XML/1998/namespace";
    private static final String PREFIX_XML = "xml";

    private static final String MANIFEST_FILE_NAME = "imsmanifest.xml";

    private QNameGenerator rootQNameGenerator;
    private ContentPackage contentPackage;
    private String scormPkgDir;

    public  ContentPackage generateContentPackageFromFile(String scormPkgDir) {
        if (scormPkgDir == null) {
            log.error("scorm package directory is null");
            return contentPackage;
        }

        this.scormPkgDir = scormPkgDir + (scormPkgDir.endsWith("/") ? "" : "/");

        File scormPkg = new File(scormPkgDir);
        if (!scormPkg.exists() || !scormPkg.isDirectory()) {
            log.error("Not found directory: {}", scormPkgDir);
            return contentPackage;
        }

        File manifestXmlFile = new File(this.scormPkgDir + MANIFEST_FILE_NAME);
        if (!manifestXmlFile.exists()) {
            log.error("Not found {} in {}", MANIFEST_FILE_NAME, scormPkgDir);
            return contentPackage;
        }

        Document manifestXml;
        try {
            manifestXml = new SAXReader().read(manifestXmlFile);
        } catch (DocumentException e) {
            log.error("read {} exception: {}", MANIFEST_FILE_NAME, CommonUtils.stringifyError(e));
            return contentPackage;
        }
        if (manifestXml == null) {
            log.error("parse {} error", MANIFEST_FILE_NAME);
            return contentPackage;
        }

        contentPackage = new ContentPackage();
        Element manifestNode = manifestXml.getRootElement();

        // #1: 确定本次解析的名称空间
        rootQNameGenerator = parseNamespace(manifestNode);

        if (rootQNameGenerator == null) {
            log.error("inner error: parse namespace failed");
            return contentPackage;
        }

        // #2: manifest
        parseManifest(manifestNode);

        // #3: manifest.metadata
        parseManifestMetadata(manifestNode.element(rootQNameGenerator.imscp("metadata")));

        // #4: manifest.organizations
        parseOrganizations(manifestNode.element(rootQNameGenerator.imscp("organizations")));

        // #5: manifest.resources
        parseResources(manifestNode.element(rootQNameGenerator.imscp("resources")));

        // #6: manifest.sequencingCollection
        parseSequencingCollection(manifestNode.element(rootQNameGenerator.imsss("sequencingCollection")));

        // #7: generate dependency
        generateDependency();

        return contentPackage;
    }

    private void generateDependency() {
        for (Organization organization : contentPackage.getManifest().getOrganizations().getOrganizationList()) {
            for (Item item : organization.getItemList()) {
                item.setParentOrganization(organization);
                generateDependency(item);
            }
        }
    }

    private void generateDependency(Item item) {
        for (Item childItem : item.getItemList()) {
            childItem.setParentItem(item);
            generateDependency(childItem);
        }
    }

    private QNameGenerator parseNamespace(Element node) {
        if (node == null) {
            return null;
        }
        Map<String, Namespace> namespaceMap = new HashMap<>();
        node.content().forEach(obj -> {
            if (obj instanceof Namespace) {
                Namespace namespace = (Namespace) obj;
                namespaceMap.put(namespace.getURI(), namespace);
            }
        });
        return new QNameGenerator(namespaceMap);
    }

    private void parseManifest(Element manifestNode) {
        Manifest manifest = new Manifest();
        Namespace pns = manifestNode.getNamespace();
        String id = manifestNode.attributeValue(rootQNameGenerator.imscp("identifier", pns));
        manifest.setIdentifier(id == null ? null : new ID(id));
        manifest.setVersion(manifestNode.attributeValue(
                rootQNameGenerator.imscp("version", pns)));
        String base = manifestNode.attributeValue(rootQNameGenerator.xml("base", pns));
        manifest.setXmlBase(base == null ? null : new AnyURI(base));
        contentPackage.setManifest(manifest);
    }

    private void parseManifestMetadata(Element manifestMetadataNode) {
        if (manifestMetadataNode != null) {
            ManifestMetadata manifestMetadata = new ManifestMetadata();
            manifestMetadata.setSchema(manifestMetadataNode.elementTextTrim(
                    rootQNameGenerator.imscp("schema")));
            manifestMetadata.setSchemaVersion(manifestMetadataNode.elementText(
                    rootQNameGenerator.imscp("schemaversion")));
            manifestMetadata.setMetadata(parseMetadata(manifestMetadataNode));
            contentPackage.getManifest().setMetadata(manifestMetadata);
        }
    }

    private void parseOrganizations(Element organizationsNode) {
        if (organizationsNode != null) {
            Organizations organizations = new Organizations();
            String doi = organizationsNode.attributeValue(
                    rootQNameGenerator.imscp("default", organizationsNode.getNamespace()));
            organizations.setDefaultOrganizationID(doi == null ? null : new IDRef(doi));
            organizationsNode.elements(rootQNameGenerator.imscp("organization"))
                    .forEach(element -> parseOrganization(organizations, (Element) element));
            contentPackage.getManifest().setOrganizations(organizations);
        }
    }

    private void parseSequencingCollection(Element sequencingCollectionNode) {
        if (sequencingCollectionNode != null) {
            SequencingCollection sequencingCollection = new SequencingCollection();
            for (Object element : sequencingCollectionNode.elements("sequencing")) {
                Sequencing sequencing = parseSequencing((Element) element);
                if (sequencing != null) {
                    sequencingCollection.getSequencingList().add(sequencing);
                }
            }
            contentPackage.getManifest().setSequencingCollection(sequencingCollection);
        }
    }

    private void parseOrganization(Organizations organizations, Element organizationNode) {
        if (organizationNode != null) {
            Organization organization = new Organization();
            Namespace pns = organizationNode.getNamespace();
            String id = organizationNode.attributeValue(rootQNameGenerator.imscp("identifier", pns));
            organization.setIdentifier(id == null ? null : new ID(id));
            String structure = organizationNode.attributeValue(rootQNameGenerator.imscp("structure", pns));
            if (structure != null) {
                organization.setStructure(structure);
            }
            Boolean ogts = parseBoolean(organizationNode.attributeValue(
                    rootQNameGenerator.adlseq("objectivesGlobalToSystem", pns)));
            if (ogts != null) {
                organization.setObjectivesGlobalToSystem(ogts);
            }
            Boolean sdgts = parseBoolean(organizationNode.attributeValue(
                    rootQNameGenerator.adlcp("sharedDataGlobalToSystem", pns)));
            if (sdgts != null) {
                organization.setSharedDataGlobalToSystem(sdgts);
            }

            organization.setTitle(organizationNode.elementTextTrim(rootQNameGenerator.imscp("title")));
            organizationNode.elements(rootQNameGenerator.imscp("item"))
                    .forEach(element -> parseItem(organization, (Element) element));
            parseCompletionThreshold(organization, organizationNode.element(
                    rootQNameGenerator.adlcp("completionThreshold")));
            organization.setMetadata(parseMetadata(organizationNode.element(rootQNameGenerator.imscp("metadata"))));
            organization.setSequencing(parseSequencing(organizationNode.element(
                    rootQNameGenerator.imsss("sequencing"))));
            organizations.getOrganizationList().add(organization);
        }
    }

    private void parseItem(Organization organization, Element itemNode) {
        if (itemNode != null) {
            Item item = parseItem(itemNode);
            if (item != null) {
                organization.getItemList().add(item);
            }
        }
    }

    private void parseItem(Item item, Element itemNode) {
        if (itemNode != null) {
            Item childItem = parseItem(itemNode);
            if (childItem != null) {
                item.getItemList().add(childItem);
            }
        }
    }

    private Item parseItem(Element itemNode) {
        if (itemNode != null) {
            Item item = new Item();
            Namespace pns = itemNode.getNamespace();
            String id = itemNode.attributeValue(rootQNameGenerator.imscp("identifier", pns));
            item.setIdentifier(id == null ? null : new ID(id));
            item.setIdentifierref(itemNode.attributeValue(rootQNameGenerator.imscp("identifierref", pns)));
            Boolean isvisible = parseBoolean(itemNode.attributeValue(rootQNameGenerator.imscp("isvisible", pns)));
            if (isvisible != null) {
                item.setIsvisible(isvisible);
            }
            item.setParameters(itemNode.attributeValue(rootQNameGenerator.imscp("parameters", pns)));
            item.setTitle(itemNode.elementTextTrim(rootQNameGenerator.imscp("title")));
            itemNode.elements(rootQNameGenerator.imscp("item"))
                    .forEach(element -> parseItem(item, (Element) element));
            item.setTimeLimitAction(itemNode.elementTextTrim(rootQNameGenerator.adlcp("timeLimitAction")));
            item.setDataFromLMS(itemNode.elementTextTrim(rootQNameGenerator.adlcp("dataFromLMS")));
            parseCompletionThreshold(item, itemNode.element(rootQNameGenerator.adlcp("completionThreshold")));
            parseData(item, itemNode.element(rootQNameGenerator.adlcp("data")));
            item.setMetadata(parseMetadata(itemNode.element(rootQNameGenerator.imscp("metadata"))));
            item.setSequencing(parseSequencing(itemNode.element(rootQNameGenerator.imsss("sequencing"))));
            parsePresentation(item, itemNode.element(rootQNameGenerator.adlnav("presentation")));
            return item;
        }
        return null;
    }

    private void parsePresentation(Item item, Element presentationNode) {
        if (presentationNode != null) {
            Presentation presentation = new Presentation();
            parseNavigationInterface(presentation, presentationNode.element(
                    rootQNameGenerator.adlnav("navigationInterface")));
            item.setPresentation(presentation);
        }
    }

    private void parseNavigationInterface(Presentation presentation, Element navigationInterfaceNode) {
        if (navigationInterfaceNode != null) {
            NavigationInterface navigationInterface = new NavigationInterface();
            for (Object element : navigationInterfaceNode.elements(rootQNameGenerator.adlnav("hideLMSUI"))) {
                parseHideLMSUI(navigationInterface, (Element) element);
            }
            presentation.setNavigationInterface(navigationInterface);
        }
    }

    private void parseHideLMSUI(NavigationInterface navigationInterface, Element hideLMSUINode) {
        if (hideLMSUINode != null) {
            HideLMSUI hideLMSUI = new HideLMSUI();
            hideLMSUI.setValue(hideLMSUINode.getTextTrim());
            navigationInterface.getHideLMSUIList().add(hideLMSUI);
        }
    }

    private void parseCompletionThreshold(Organization organization, Element completionThresholdNode) {
        if (completionThresholdNode != null) {
            organization.setCompletionThreshold(parseCompletionThreshold(completionThresholdNode));
        }
    }

    private void parseCompletionThreshold(Item item, Element completionThresholdNode) {
        if (completionThresholdNode != null) {
            item.setCompletionThreshold(parseCompletionThreshold(completionThresholdNode));
        }
    }

    private CompletionThreshold parseCompletionThreshold(Element completionThresholdNode) {
        if (completionThresholdNode != null) {
            CompletionThreshold completionThreshold = new CompletionThreshold();
            Namespace pns = completionThresholdNode.getNamespace();
            Boolean cbm = parseBoolean(completionThresholdNode.attributeValue(
                    rootQNameGenerator.adlcp("completedByMeasure", pns)));
            if (cbm != null) {
                completionThreshold.setCompletedByMeasure(cbm);
            }
            Decimal mpm = parseDecimal(completionThresholdNode.attributeValue(
                    rootQNameGenerator.adlcp("minProgressMeasure", pns)), 4);
            if (mpm != null) {
                completionThreshold.setMinProgressMeasure(mpm);
            }
            Decimal pw = parseDecimal(completionThresholdNode.attributeValue(
                    rootQNameGenerator.adlcp("progressWeight", pns)), 4);
            if (pw != null) {
                completionThreshold.setProgressWeight(pw);
            }
            return completionThreshold;
        }
        return null;
    }

    private void parseData(Item item, Element dataNode) {
        if (dataNode != null) {
            Data data = new Data();
            dataNode.elements(rootQNameGenerator.adlcp("map"))
                    .forEach(element -> parseMap(data, (Element) element));
            item.setData(data);
        }
    }

    private void parseMap(Data data, Element mapNode) {
        if (mapNode != null) {
            com.corkili.learningserver.scorm.cam.model.Map map = new com.corkili.learningserver.scorm.cam.model.Map();
            Namespace pns = mapNode.getNamespace();
            String tid = mapNode.attributeValue(rootQNameGenerator.adlcp("targetID", pns));
            map.setTargetID(tid == null ? null : new AnyURI(tid));
            Boolean rsd = parseBoolean(mapNode.attributeValue(rootQNameGenerator.adlcp("readSharedData", pns)));
            if (rsd != null) {
                map.setReadSharedData(rsd);
            }
            Boolean wsd = parseBoolean(mapNode.attributeValue(rootQNameGenerator.adlcp("writeSharedData", pns)));
            if (wsd != null) {
                map.setWriteSharedData(wsd);
            }
            data.getMapList().add(map);
        }
    }

    private void parseResources(Element resourcesNode) {
        if (resourcesNode != null) {
            Resources resources = new Resources();
            String base = resourcesNode.attributeValue(
                    rootQNameGenerator.xml("base",  resourcesNode.getNamespace()));
            resources.setXmlBase(base == null ? null : new AnyURI(base));
            resourcesNode.elements(rootQNameGenerator.imscp("resource"))
                    .forEach(element -> parseResource(resources, (Element) element));
            contentPackage.getManifest().setResources(resources);
        }
    }

    private void parseResource(Resources resources, Element resourceNode) {
        if (resourceNode != null) {
            Resource resource = new Resource();
            Namespace pns = resourceNode.getNamespace();
            String id = resourceNode.attributeValue(rootQNameGenerator.imscp("identifier", pns));
            resource.setIdentifier(id == null ? null : new ID(id));
            resource.setType(resourceNode.attributeValue(rootQNameGenerator.imscp("type", pns)));
            resource.setHref(resourceNode.attributeValue(rootQNameGenerator.imscp("href", pns)));
            String base = resourceNode.attributeValue(rootQNameGenerator.xml("base", pns));
            resource.setXmlBase(base == null ? null : new AnyURI(base));
            resource.setScormType(resourceNode.attributeValue(rootQNameGenerator.adlcp("scormType", pns)));
            QName qNameForFile = rootQNameGenerator.imscp("file");
            QName qNameForDependency = rootQNameGenerator.imscp("dependency");
            for (Object obj : resourceNode.elements()) {
                Element element = (Element) obj;
                if (element.getQName().equals(qNameForFile)) {
                    parseFile(resource, element);
                } else if (element.getQName().equals(qNameForDependency)) {
                    parseDependency(resource, element);
                }
            }
            resource.setMetadata(parseMetadata(resourceNode.element(rootQNameGenerator.imscp("metadata"))));
            resources.getResourceList().add(resource);
        }
    }

    private void parseFile(Resource resource, Element fileNode) {
        if (fileNode != null) {
            com.corkili.learningserver.scorm.cam.model.File file = new com.corkili.learningserver.scorm.cam.model.File();
            file.setHref(fileNode.attributeValue(rootQNameGenerator.imscp("href", fileNode.getNamespace())));
            file.setMetadata(parseMetadata(fileNode.element(rootQNameGenerator.imscp("metadata"))));
            resource.getFileList().add(file);
        }
    }

    private void parseDependency(Resource resource, Element dependencyNode) {
        if (dependencyNode != null) {
            Dependency dependency = new Dependency();
            dependency.setIdentifierref(dependencyNode.attributeValue(
                    rootQNameGenerator.imscp("identifierref", dependencyNode.getNamespace())));
            resource.getDependencyList().add(dependency);
        }
    }

    private Metadata parseMetadata(Element metadataNode) {
        if (metadataNode != null) {
            Metadata metadata = new Metadata();
            // inner lom
            for (Object element : metadataNode.elements(rootQNameGenerator.lom("lom"))) {
                Element lomNode = (Element) element;
                Map<String, Namespace> namespaceMap = new HashMap<>();
                for (Object obj : lomNode.content()) {
                    if (obj instanceof Namespace) {
                        namespaceMap.put(((Namespace) obj).getURI(), (Namespace) obj);
                    }
                }
                // <lom:lom></lom:lom> : map.size == 0
                // <lom xmlns="..."></lom> : map.size > 0
                QNameGenerator qNameGenerator = new QNameGenerator(rootQNameGenerator, namespaceMap);
                LOM lom = parseLOM(lomNode, qNameGenerator);
                metadata.getLomList().add(lom);
            }
            // location
            for (Object obj : metadataNode.elements(rootQNameGenerator.adlcp("location"))) {
                Element locationNode = (Element) obj;
                parseLocation(metadata, locationNode);
            }
            return metadata;
        }
        return null;
    }

    private void parseLocation(Metadata metadata, Element locationNode) {
        if (locationNode != null) {
            String location = locationNode.getTextTrim();
            if ("".equals(location)) {
                return;
            }
            metadata.getLocationLomMap().put(location, null);
            File lomXmlFile = new File(this.scormPkgDir + location);
            if (!lomXmlFile.exists()) {
                log.error("Not found {} in {}", location, this.scormPkgDir);
                return;
            }
            Document lomXml;
            try {
                lomXml = new SAXReader().read(lomXmlFile);
            } catch (DocumentException e) {
                log.error("read {} exception: {}", location, CommonUtils.stringifyError(e));
                return;
            }
            if (lomXml == null) {
                return;
            }
            Element lomNode = lomXml.getRootElement();
            QNameGenerator qNameGenerator = parseNamespace(lomNode);
            LOM lom = parseLOM(lomNode, qNameGenerator);
            if (lom != null) {
                metadata.getLocationLomMap().put(location, lom);
            }
        }
    }

    private LOM parseLOM(Element lomNode, QNameGenerator qNameGenerator) {
        if (lomNode != null && qNameGenerator != null) {
            LOM lom = new LOM();
            parseGeneral(lom, lomNode.element(qNameGenerator.lom("general")), qNameGenerator);
            parseLifeCycle(lom, lomNode.element(qNameGenerator.lom("lifeCycle")), qNameGenerator);
            parseMetaMetadata(lom, lomNode.element(qNameGenerator.lom("metaMetadata")), qNameGenerator);
            parseTechnical(lom, lomNode.element(qNameGenerator.lom("technical")), qNameGenerator);
            for (Object element : lomNode.elements(qNameGenerator.lom("educational"))) {
                parseEducational(lom, (Element) element, qNameGenerator);
            }
            parseRights(lom, lomNode.element(qNameGenerator.lom("rights")), qNameGenerator);
            for (Object element : lomNode.elements(qNameGenerator.lom("relation"))) {
                parseRelation(lom, (Element) element, qNameGenerator);
            }
            for (Object element : lomNode.elements(qNameGenerator.lom("annotation"))) {
                parseAnnotation(lom, (Element) element, qNameGenerator);
            }
            for (Object element : lomNode.elements(qNameGenerator.lom("classification"))) {
                parseClassification(lom, (Element) element, qNameGenerator);
            }
        }
        return null;
    }

    private void parseGeneral(LOM lom, Element generalNode, QNameGenerator qNameGenerator) {
        if (generalNode != null) {
            General general = new General();
            for (Object element : generalNode.elements(qNameGenerator.lom("identifier"))) {
                Identifier identifier = parseIdentifier((Element) element, qNameGenerator);
                if (identifier != null) {
                    general.getIdentifierList().add(identifier);
                }
            }
            general.setTitle(parseLanguageStrings(generalNode.element(
                    qNameGenerator.lom("title")), qNameGenerator));
            for (Object element : generalNode.elements(qNameGenerator.lom("language"))) {
                String language = ((Element) element).getTextTrim();
                if (language != null) {
                    general.getLanguageList().add(language);
                }
            }
            for (Object element : generalNode.elements(qNameGenerator.lom("description"))) {
                LanguageStrings description = parseLanguageStrings((Element) element, qNameGenerator);
                if (description != null) {
                    general.getDescriptionList().add(description);
                }
            }
            for (Object element : generalNode.elements(qNameGenerator.lom("keyword"))) {
                LanguageStrings keyword = parseLanguageStrings((Element) element, qNameGenerator);
                if (keyword != null) {
                    general.getKeywordList().add(keyword);
                }
            }
            for (Object element : generalNode.elements(qNameGenerator.lom("coverage"))) {
                LanguageStrings coverage = parseLanguageStrings((Element) element, qNameGenerator);
                if (coverage != null) {
                    general.getCoverageList().add(coverage);
                }
            }
            general.setStructure(parseVocabulary(generalNode.element(
                    qNameGenerator.lom("structure")), qNameGenerator));
            general.setAggregationLevel(parseVocabulary(generalNode.element(
                    qNameGenerator.lom("structure")), qNameGenerator));
            lom.setGeneral(general);
        }
    }

    private void parseLifeCycle(LOM lom, Element lifeCycleNode, QNameGenerator qNameGenerator) {
        if (lifeCycleNode != null) {
            LifeCycle lifeCycle = new LifeCycle();
            lifeCycle.setVersion(parseLanguageStrings(lifeCycleNode.element(
                    qNameGenerator.lom("version")), qNameGenerator));
            lifeCycle.setStatus(parseVocabulary(lifeCycleNode.element(
                    qNameGenerator.lom("status")), qNameGenerator));
            for (Object element : lifeCycleNode.elements(qNameGenerator.lom("contribute"))) {
                Contribute contribute = parseContribute((Element) element, qNameGenerator);
                if (contribute != null) {
                    lifeCycle.getContributeList().add(contribute);
                }
            }
            lom.setLifeCycle(lifeCycle);
        }
    }

    private void parseMetaMetadata(LOM lom, Element metaMetadataNode, QNameGenerator qNameGenerator) {
        if (metaMetadataNode != null) {
            MetaMetadata metaMetadata = new MetaMetadata();
            for (Object element : metaMetadataNode.elements(qNameGenerator.lom("identifier"))) {
                Identifier identifier = parseIdentifier((Element) element, qNameGenerator);
                if (identifier != null) {
                    metaMetadata.getIdentifierList().add(identifier);
                }
            }
            for (Object element : metaMetadataNode.elements(qNameGenerator.lom("contribute"))) {
                Contribute contribute = parseContribute((Element) element, qNameGenerator);
                if (contribute != null) {
                    metaMetadata.getContributeList().add(contribute);
                }
            }
            for (Object element : metaMetadataNode.elements(qNameGenerator.lom("metadataSchema"))) {
                String schema = ((Element) element).getTextTrim();
                if (schema != null) {
                    metaMetadata.getMetadataSchema().add(schema);
                }
            }
            metaMetadata.setLanguage(metaMetadataNode.elementTextTrim(qNameGenerator.lom("language")));
            lom.setMetaMetadata(metaMetadata);
        }
    }

    private void parseTechnical(LOM lom, Element technicalNode, QNameGenerator qNameGenerator) {
        if (technicalNode != null) {
            Technical technical = new Technical();
            for (Object element : technicalNode.elements(qNameGenerator.lom("format"))) {
                String format = ((Element) element).getTextTrim();
                if (format != null) {
                    technical.getFormatList().add(format);
                }
            }
            technical.setSize(technicalNode.elementTextTrim(qNameGenerator.lom("size")));
            for (Object element : technicalNode.elements(qNameGenerator.lom("location"))) {
                String location = ((Element) element).getTextTrim();
                if (location != null) {
                    technical.getLocationList().add(location);
                }
            }
            for (Object element : technicalNode.elements(qNameGenerator.lom("requirement"))) {
                Requirement requirement = parseRequirement((Element) element, qNameGenerator);
                if (requirement != null) {
                    technical.getRequirementList().add(requirement);
                }
            }
            technical.setInstallationRemarks(parseLanguageStrings(technicalNode.element(
                    qNameGenerator.lom("installationRemarks")), qNameGenerator));
            technical.setOtherPlatformRequirements(parseLanguageStrings(technicalNode.element(
                    qNameGenerator.lom("otherPlatformRequirements")), qNameGenerator));
            technical.setDuration(parseDuration(technicalNode.element(
                    qNameGenerator.lom("duration")), qNameGenerator));
            lom.setTechnical(technical);
        }
    }

    private void parseEducational(LOM lom, Element educationalNode, QNameGenerator qNameGenerator) {
        if (educationalNode != null) {
            Educational educational = new Educational();
            educational.setInteractivityType(parseVocabulary(educationalNode.element(
                    qNameGenerator.lom("interactivityType")), qNameGenerator));
            for (Object element : educationalNode.elements(qNameGenerator.lom("learningResourceType"))) {
                Vocabulary lrt = parseVocabulary((Element) element, qNameGenerator);
                if (lrt != null) {
                    educational.getLearningResourceTypeList().add(lrt);
                }
            }
            educational.setInteractivityLevel(parseVocabulary(educationalNode.element(
                    qNameGenerator.lom("interactivityLevel")), qNameGenerator));
            educational.setSemanticDensity(parseVocabulary(educationalNode.element(
                    qNameGenerator.lom("semanticDensity")), qNameGenerator));
            for (Object element : educationalNode.elements(qNameGenerator.lom("intendedEndUserRole"))) {
                Vocabulary ieur = parseVocabulary((Element) element, qNameGenerator);
                if (ieur != null) {
                    educational.getIntendedEndUserRoleList().add(ieur);
                }
            }
            for (Object element : educationalNode.elements(qNameGenerator.lom("context"))) {
                Vocabulary context = parseVocabulary((Element) element, qNameGenerator);
                if (context != null) {
                    educational.getContextList().add(context);
                }
            }
            for (Object element : educationalNode.elements(qNameGenerator.lom("typicalAgeRange"))) {
                LanguageStrings tar = parseLanguageStrings((Element) element, qNameGenerator);
                if (tar != null) {
                    educational.getTypicalAgeRangeList().add(tar);
                }
            }
            educational.setDifficulty(parseVocabulary(educationalNode.element(
                    qNameGenerator.lom("difficulty")), qNameGenerator));
            educational.setTypicalLearningTime(parseDuration(educationalNode.element(
                    qNameGenerator.lom("typicalLearningTime")), qNameGenerator));
            for (Object element : educationalNode.elements(qNameGenerator.lom("description"))) {
                LanguageStrings description = parseLanguageStrings((Element) element, qNameGenerator);
                if (description != null) {
                    educational.getDescriptionList().add(description);
                }
            }
            for (Object element : educationalNode.elements(qNameGenerator.lom("language"))) {
                String language = ((Element) element).getTextTrim();
                if (language != null) {
                    educational.getLanguageList().add(language);
                }
            }
            lom.getEducationalList().add(educational);
        }
    }

    private void parseRights(LOM lom, Element rightsNode, QNameGenerator qNameGenerator) {
        if (rightsNode != null) {
            Rights rights = new Rights();
            rights.setCost(parseVocabulary(rightsNode.element(qNameGenerator.lom("cost")), qNameGenerator));
            rights.setCopyrightAndOtherRestrictions(parseVocabulary(rightsNode.element(
                    qNameGenerator.lom("copyrightAndOtherRestrictions")), qNameGenerator));
            rights.setDescription(parseLanguageStrings(rightsNode.element(
                    qNameGenerator.lom("description")), qNameGenerator));
            lom.setRights(rights);
        }
    }

    private void parseRelation(LOM lom, Element relationNode, QNameGenerator qNameGenerator) {
        if (relationNode != null) {
            Relation relation = new Relation();
            relation.setKind(parseVocabulary(relationNode.element(
                    qNameGenerator.lom("kind")), qNameGenerator));
            relation.setResource(parseRelationResource(relationNode.element(
                    qNameGenerator.lom("resource")), qNameGenerator));
            lom.getRelationList().add(relation);
        }
    }

    private void parseAnnotation(LOM lom, Element annotationNode, QNameGenerator qNameGenerator) {
        if (annotationNode != null) {
            Annotation annotation = new Annotation();
            String entity = annotationNode.elementTextTrim(qNameGenerator.lom("entity"));
            annotation.setEntity(entity == null ? null : new VCard(entity));
            annotation.setDate(parseDateTime(annotationNode.element(qNameGenerator.lom("date")), qNameGenerator));
            annotation.setDescription(parseLanguageStrings(annotationNode.element(
                    qNameGenerator.lom("description")), qNameGenerator));
            lom.getAnnotationList().add(annotation);
        }
    }

    private void parseClassification(LOM lom, Element classificationNode, QNameGenerator qNameGenerator) {
        if (classificationNode != null) {
            Classification classification = new Classification();
            classification.setPurpose(parseVocabulary(classificationNode.element(
                    qNameGenerator.lom("purpose")), qNameGenerator));
            for (Object element : classificationNode.elements(qNameGenerator.lom("taxonPath"))) {
                TaxonPath taxonPath = parseTaxonPath((Element) element, qNameGenerator);
                if (taxonPath != null) {
                    classification.getTaxonPathList().add(taxonPath);
                }
            }
            classification.setDescription(parseLanguageStrings(classificationNode.element(
                    qNameGenerator.lom("description")), qNameGenerator));
            for (Object element : classificationNode.elements(qNameGenerator.lom("keyword"))) {
                LanguageStrings keyword = parseLanguageStrings((Element) element, qNameGenerator);
                if (keyword != null) {
                    classification.getKeywordList().add(keyword);
                }
            }
            lom.getClassificationList().add(classification);
        }
    }

    private Identifier parseIdentifier(Element identifierNode, QNameGenerator qNameGenerator) {
        if (identifierNode != null) {
            Identifier identifier = new Identifier();
            identifier.setCatalog(identifierNode.elementTextTrim(qNameGenerator.lom("catalog")));
            identifier.setEntry(identifierNode.elementTextTrim(qNameGenerator.lom("entry")));
            return identifier;
        }
        return null;
    }

    private Contribute parseContribute(Element contributeNode, QNameGenerator qNameGenerator) {
        if (contributeNode != null) {
            Contribute contribute = new Contribute();
            contribute.setRole(parseVocabulary(contributeNode.element(
                    qNameGenerator.lom("role")), qNameGenerator));
            for (Object element : contributeNode.elements(qNameGenerator.lom("entity"))) {
                String entity = ((Element) element).getTextTrim();
                if (entity != null) {
                    contribute.getEntityList().add(new VCard(entity));
                }
            }
            contribute.setDate(parseDateTime(contributeNode.element(
                    qNameGenerator.lom("date")), qNameGenerator));
            return contribute;
        }
        return null;
    }

    private Requirement parseRequirement(Element requirementNode, QNameGenerator qNameGenerator) {
        if (requirementNode != null) {
            Requirement requirement = new Requirement();
            for (Object element : requirementNode.elements(qNameGenerator.lom("orComposite"))) {
                OrComposite orComposite = parseOrComposite((Element) element, qNameGenerator);
                if (orComposite != null) {
                    requirement.getOrCompositeList().add(orComposite);
                }
            }
            return requirement;
        }
        return null;
    }

    private OrComposite parseOrComposite(Element orCompositeNode, QNameGenerator qNameGenerator) {
        if (orCompositeNode != null) {
            OrComposite orComposite = new OrComposite();
            orComposite.setType(parseVocabulary(orCompositeNode.element(
                    qNameGenerator.lom("type")), qNameGenerator));
            orComposite.setName(parseVocabulary(orCompositeNode.element(
                    qNameGenerator.lom("name")), qNameGenerator));
            orComposite.setMinimumVersion(orCompositeNode.elementTextTrim(
                    qNameGenerator.lom("minimumVersion")));
            orComposite.setMaximumVersion(orCompositeNode.elementTextTrim(
                    qNameGenerator.lom("maximumVersion")));
            return orComposite;
        }
        return null;
    }

    private RelationResource parseRelationResource(Element relationResourceNode, QNameGenerator qNameGenerator) {
        if (relationResourceNode != null) {
            RelationResource relationResource = new RelationResource();
            for (Object element : relationResourceNode.elements(qNameGenerator.lom("identifier"))) {
                Identifier identifier = parseIdentifier((Element) element, qNameGenerator);
                if (identifier != null) {
                    relationResource.getIdentifierList().add(identifier);
                }
            }
            for (Object element : relationResourceNode.elements(qNameGenerator.lom("description"))) {
                LanguageStrings description = parseLanguageStrings((Element) element, qNameGenerator);
                if (description != null) {
                    relationResource.getDescriptionList().add(description);
                }
            }
            return relationResource;
        }
        return null;
    }

    private TaxonPath parseTaxonPath(Element taxonPathNode, QNameGenerator qNameGenerator) {
        if (taxonPathNode != null) {
            TaxonPath taxonPath = new TaxonPath();
            taxonPath.setSource(parseLanguageStrings(taxonPathNode.element(
                    qNameGenerator.lom("taxonPath")), qNameGenerator));
            for (Object element : taxonPathNode.elements(qNameGenerator.lom("taxon"))) {
                Taxon taxon = parseTaxon((Element) element, qNameGenerator);
                if (taxon != null) {
                    taxonPath.getTaxonList().add(taxon);
                }
            }
            return taxonPath;
        }
        return null;
    }

    private Taxon parseTaxon(Element taxonNode, QNameGenerator qNameGenerator) {
        if (taxonNode != null) {
            Taxon taxon = new Taxon();
            taxon.setId(taxonNode.elementTextTrim(qNameGenerator.lom("id")));
            taxon.setEntry(parseLanguageStrings(taxonNode.element(qNameGenerator.lom("entry")), qNameGenerator));
            return taxon;
        }
        return null;
    }

    private LanguageStrings parseLanguageStrings(Element pNode, QNameGenerator qNameGenerator) {
        if (pNode != null) {
            LanguageStrings languageStrings = new LanguageStrings();
            for (Object element : pNode.elements("string")) {
                Element stringNode = (Element) element;
                String language = stringNode.attributeValue(qNameGenerator.lom("language", stringNode.getNamespace()));
                String text = stringNode.getTextTrim();
                languageStrings.getLanguageStringList().add(new LanguageString(language, text));
            }
            return languageStrings;
        }
        return null;
    }

    private Vocabulary parseVocabulary(Element vocabularyNode, QNameGenerator qNameGenerator) {
        if (vocabularyNode != null) {
            String source = vocabularyNode.elementTextTrim(qNameGenerator.lom("source"));
            String value = vocabularyNode.elementTextTrim(qNameGenerator.lom("value"));
            return new Vocabulary(source, value);
        }
        return null;
    }

    private DateTime parseDateTime(Element dateNode, QNameGenerator qNameGenerator) {
        if (dateNode != null) {
            DateTime dateTime = new DateTime();
            dateTime.setDateTime(dateNode.elementTextTrim(qNameGenerator.lom("dateTime")));
            dateTime.setDescription(parseLanguageStrings(dateNode.element(
                    qNameGenerator.lom("description")), qNameGenerator));
            return dateTime;
        }
        return null;
    }

    private Duration parseDuration(Element durationNode, QNameGenerator qNameGenerator) {
        if (durationNode != null) {
            Duration duration = new Duration();
            duration.setDuration(durationNode.elementTextTrim(qNameGenerator.lom("duration")));
            duration.setDescription(parseLanguageStrings(durationNode.element(
                    qNameGenerator.lom("description")), qNameGenerator));
            return duration;
        }
        return null;
    }

    private Sequencing parseSequencing(Element sequencingNode) {
        if (sequencingNode != null) {
            Sequencing sequencing = new Sequencing();
            Namespace pns = sequencingNode.getNamespace();
            String id = sequencingNode.attributeValue(rootQNameGenerator.imsss("ID", pns));
            sequencing.setId(id == null ? null : new ID(id));
            String idRef = sequencingNode.attributeValue(rootQNameGenerator.imsss("IDRef", pns));
            sequencing.setIdRef(idRef == null ? null : new IDRef(idRef));
            parseControlMode(sequencing, sequencingNode.element(rootQNameGenerator.imsss("controlMode")));
            parseSequencingRules(sequencing, sequencingNode.element(rootQNameGenerator.imsss("sequencingRules")));
            parseLimitConditions(sequencing, sequencingNode.element(rootQNameGenerator.imsss("limitConditions")));
            parseAuxiliaryResource(sequencing, sequencingNode.element(rootQNameGenerator.imsss("auxiliaryResources")));
            parseRollupRules(sequencing, sequencingNode.element(rootQNameGenerator.imsss("rollupRules")));
            parseObjectives(sequencing, sequencingNode.element(rootQNameGenerator.imsss("objectives")));
            parseRandomizationControls(sequencing, sequencingNode.element(rootQNameGenerator.imsss("randomizationControls")));
            parseDeliveryControls(sequencing, sequencingNode.element(rootQNameGenerator.imsss("deliveryControls")));
            parseConstrainedChoiceConsiderations(sequencing, sequencingNode.element(rootQNameGenerator.adlseq("constrainedChoiceConsiderations")));
            parseRollupConsiderations(sequencing, sequencingNode.element(rootQNameGenerator.adlseq("rollupConsiderations")));
            parseAdlseqObjectives(sequencing, sequencingNode.element(rootQNameGenerator.adlseq("objectives")));
            return sequencing;
        }
        return null;
    }

    private void parseControlMode(Sequencing sequencing, Element controlModeNode) {
        if (controlModeNode != null) {
            ControlMode controlMode = new ControlMode();
            Namespace pns = controlModeNode.getNamespace();
            Boolean choice = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("choice", pns)));
            if (choice != null) {
                controlMode.setChoice(choice);
            }
            Boolean choiceExit = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("choiceExit", pns)));
            if (choiceExit != null) {
                controlMode.setChoiceExit(choiceExit);
            }
            Boolean flow = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("flow", pns)));
            if (flow != null) {
                controlMode.setFlow(flow);
            }
            Boolean forwardOnly = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("forwardOnly", pns)));
            if (forwardOnly != null) {
                controlMode.setForwardOnly(forwardOnly);
            }
            Boolean useCurrentAttemptObjectiveInfo = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("useCurrentAttemptObjectiveInfo", pns)));
            if (useCurrentAttemptObjectiveInfo != null) {
                controlMode.setUseCurrentAttemptObjectiveInfo(useCurrentAttemptObjectiveInfo);
            }
            Boolean useCurrentAttemptProgressInfo = parseBoolean(controlModeNode.attributeValue(
                    rootQNameGenerator.imsss("useCurrentAttemptProgressInfo", pns)));
            if (useCurrentAttemptProgressInfo != null) {
                controlMode.setUseCurrentAttemptProgressInfo(useCurrentAttemptProgressInfo);
            }
            sequencing.setControlMode(controlMode);
        }
    }

    private void parseSequencingRules(Sequencing sequencing, Element sequencingRulesNode) {
        if (sequencingRulesNode != null) {
            SequencingRules sequencingRules = new SequencingRules();
            QName pre = rootQNameGenerator.imsss("preConditionRule");
            QName exit = rootQNameGenerator.imsss("exitConditionRule");
            QName post = rootQNameGenerator.imsss("postConditionRule");
            for (Object element : sequencingRulesNode.elements()) {
                Element conditionRuleNode = (Element) element;
                QName qn = conditionRuleNode.getQName();
                if (qn.equals(pre)) {
                    ConditionRule conditionRule = parseConditionRule(conditionRuleNode);
                    if (conditionRule != null) {
                        sequencingRules.getPreConditionRuleList().add(conditionRule);
                    }
                } else if (qn.equals(exit)) {
                    ConditionRule conditionRule = parseConditionRule(conditionRuleNode);
                    if (conditionRule != null) {
                        sequencingRules.getExitConditionRuleList().add(conditionRule);
                    }
                } else if (qn.equals(post)) {
                    ConditionRule conditionRule = parseConditionRule(conditionRuleNode);
                    if (conditionRule != null) {
                        sequencingRules.getPostConditionRuleList().add(conditionRule);
                    }
                }
            }
            sequencing.setSequencingRules(sequencingRules);
        }
    }

    private void parseLimitConditions(Sequencing sequencing, Element limitConditionsNode) {
        if (limitConditionsNode != null) {
            LimitConditions limitConditions = new LimitConditions();
            Namespace pns = limitConditionsNode.getNamespace();
            limitConditions.setAttemptLimit(parseNonNegativeInteger(limitConditionsNode.attributeValue(
                    rootQNameGenerator.imsss("attemptLimit", pns))));
            limitConditions.setAttemptAbsoluteDurationLimit(limitConditionsNode.attributeValue(
                    rootQNameGenerator.imsss("attemptAbsoluteDurationLimit", pns)));
            sequencing.setLimitConditions(limitConditions);
        }
    }

    private void parseAuxiliaryResource(Sequencing sequencing, Element auxiliaryResourcesNode) {
        // don't implementation
    }

    private void parseRollupRules(Sequencing sequencing, Element rollupRulesNode) {
        if (rollupRulesNode != null) {
            RollupRules rollupRules = new RollupRules();
            Namespace pns = rollupRulesNode.getNamespace();
            Boolean ros = parseBoolean(rollupRulesNode.attributeValue(
                    rootQNameGenerator.imsss("rollupObjectiveSatisfied", pns)));
            if (ros != null) {
                rollupRules.setRollupObjectiveSatisfied(ros);
            }
            Boolean rpc = parseBoolean(rollupRulesNode.attributeValue(
                    rootQNameGenerator.imsss("rollupProgressCompletion", pns)));
            if (rpc != null) {
                rollupRules.setRollupProgressCompletion(rpc);
            }
            Decimal omw = parseDecimal(rollupRulesNode.attributeValue(
                    rootQNameGenerator.imsss("objectiveMeasureWeight", pns)), 4);
            if (omw != null) {
                rollupRules.setObjectiveMeasureWeight(omw);
            }
            for (Object element : rollupRulesNode.elements(rootQNameGenerator.imsss("rollupRule"))) {
                parseRollupRule(rollupRules, (Element) element);
            }
            sequencing.setRollupRules(rollupRules);
        }
    }

    private void parseObjectives(Sequencing sequencing, Element objectivesNode) {
        if (objectivesNode != null) {
            Objectives objectives = new Objectives();
            objectives.setPrimaryObjective(parseObjective(objectivesNode.element(
                    rootQNameGenerator.imsss("primaryObjective"))));
            for (Object element : objectivesNode.elements(rootQNameGenerator.imsss("objective"))) {
                Objective objective = parseObjective((Element) element);
                if (objective != null) {
                    objectives.getObjectiveList().add(objective);
                }
            }
            sequencing.setObjectives(objectives);
        }
    }

    private void parseRandomizationControls(Sequencing sequencing, Element randomizationControlsNode) {
        if (randomizationControlsNode != null) {
            RandomizationControls randomizationControls = new RandomizationControls();
            Namespace pns = randomizationControlsNode.getNamespace();
            String rt = randomizationControlsNode.attributeValue(
                    rootQNameGenerator.imsss("randomizationTiming", pns));
            if (rt != null) {
                randomizationControls.setRandomizationTiming(new Token(rt));
            }
            randomizationControls.setSelectCount(parseNonNegativeInteger(randomizationControlsNode.attributeValue(
                    rootQNameGenerator.imsss("selectCount", pns))));
            Boolean rc = parseBoolean(randomizationControlsNode.attributeValue(
                    rootQNameGenerator.imsss("reorderChildren", pns)));
            if (rc != null) {
                randomizationControls.setReorderChildren(rc);
            }
            String st = randomizationControlsNode.attributeValue(
                    rootQNameGenerator.imsss("selectionTiming", pns));
            if (st != null) {
                randomizationControls.setSelectionTiming(new Token(st));
            }
            sequencing.setRandomizationControls(randomizationControls);
        }
    }

    private void parseDeliveryControls(Sequencing sequencing, Element deliveryControlsNode) {
        if (deliveryControlsNode != null) {
            DeliveryControls deliveryControls = new DeliveryControls();
            Namespace pns = deliveryControlsNode.getNamespace();
            Boolean tracked = parseBoolean(deliveryControlsNode.attributeValue(
                    rootQNameGenerator.imsss("tracked", pns)));
            if (tracked != null) {
                deliveryControls.setTracked(tracked);
            }
            Boolean csbc = parseBoolean(deliveryControlsNode.attributeValue(
                    rootQNameGenerator.imsss("completionSetByContent", pns)));
            if (csbc != null) {
                deliveryControls.setCompletionSetByContent(csbc);
            }
            Boolean osbc = parseBoolean(deliveryControlsNode.attributeValue(
                    rootQNameGenerator.imsss("objectiveSetByContent", pns)));
            if (osbc != null) {
                deliveryControls.setObjectiveSetByContent(osbc);
            }
            sequencing.setDeliveryControls(deliveryControls);
        }
    }

    private void parseConstrainedChoiceConsiderations(Sequencing sequencing, Element constrainedChoiceConsiderationsNode) {
        if (constrainedChoiceConsiderationsNode != null) {
            ConstrainedChoiceConsiderations constrainedChoiceConsiderations = new ConstrainedChoiceConsiderations();
            Namespace pns = constrainedChoiceConsiderationsNode.getNamespace();
            Boolean pa = parseBoolean(constrainedChoiceConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("preventActivation", pns)));
            if (pa != null) {
                constrainedChoiceConsiderations.setPreventActivation(pa);
            }
            Boolean cc = parseBoolean(constrainedChoiceConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("constrainChoice", pns)));
            if (cc != null) {
                constrainedChoiceConsiderations.setConstrainChoice(cc);
            }
            sequencing.setConstrainedChoiceConsiderations(constrainedChoiceConsiderations);
        }
    }

    private void parseRollupConsiderations(Sequencing sequencing, Element rollupConsiderationsNode) {
        if (rollupConsiderationsNode != null) {
            RollupConsiderations rollupConsiderations = new RollupConsiderations();
            Namespace pns = rollupConsiderationsNode.getNamespace();
            String rfs = rollupConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("requiredForSatisfied", pns));
            if (rfs != null) {
                rollupConsiderations.setRequiredForSatisfied(new Token(rfs));
            }
            String rfns = rollupConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("requiredForNotSatisfied", pns));
            if (rfns != null) {
                rollupConsiderations.setRequiredForNotSatisfied(new Token(rfns));
            }
            String rfc = rollupConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("requiredForCompleted", pns));
            if (rfc != null) {
                rollupConsiderations.setRequiredForCompleted(new Token(rfc));
            }
            String rfi = rollupConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("requiredForIncomplete", pns));
            if (rfi != null) {
                rollupConsiderations.setRequiredForIncomplete(new Token(rfi));
            }
            Boolean msia = parseBoolean(rollupConsiderationsNode.attributeValue(
                    rootQNameGenerator.adlseq("measureSatisfactionIfActive", pns)));
            if (msia != null) {
                rollupConsiderations.setMeasureSatisfactionIfActive(msia);
            }
            sequencing.setRollupConsiderations(rollupConsiderations);
        }
    }

    private void parseAdlseqObjectives(Sequencing sequencing, Element adlseqObjectivesNode) {
        if (adlseqObjectivesNode != null) {
            AdlseqObjectives adlseqObjectives = new AdlseqObjectives();
            for (Object element : adlseqObjectivesNode.elements(rootQNameGenerator.adlseq("objective"))) {
                parseAdlseqObjective(adlseqObjectives, (Element) element);
            }
            sequencing.setAdlseqObjectives(adlseqObjectives);
        }
    }

    private ConditionRule parseConditionRule(Element conditionRuleNode) {
        if (conditionRuleNode != null) {
            ConditionRule conditionRule = new ConditionRule();
            parseRuleConditions(conditionRule, conditionRuleNode.element(
                    rootQNameGenerator.imsss("ruleConditions")));
            Element actionNode = conditionRuleNode.element(rootQNameGenerator.imsss("ruleAction"));
            String action = actionNode == null ? null : actionNode.attributeValue(
                    rootQNameGenerator.imsss("action", conditionRuleNode.getNamespace()));
            RuleAction ruleAction = action == null ? null : new RuleAction();
            if (ruleAction != null) {
                ruleAction.setAction(new Token(action));
            }
            conditionRule.setRuleAction(ruleAction);
            return conditionRule;
        }
        return null;
    }

    private void parseRuleConditions(ConditionRule conditionRule, Element ruleConditionsNode) {
        if (ruleConditionsNode != null) {
            RuleConditions ruleConditions = new RuleConditions();
            String cc = ruleConditionsNode.attributeValue(
                    rootQNameGenerator.imsss("conditionCombination", ruleConditionsNode.getNamespace()));
            if (cc != null) {
                ruleConditions.setConditionCombination(new Token(cc));
            }
            for (Object element : ruleConditionsNode.elements(rootQNameGenerator.imsss("ruleCondition"))) {
                parseRuleCondition(ruleConditions, (Element) element);
            }
            conditionRule.setRuleConditions(ruleConditions);
        }
    }

    private void parseRuleCondition(RuleConditions ruleConditions, Element ruleConditionNode) {
        if (ruleConditionNode != null) {
            RuleCondition ruleCondition = new RuleCondition();
            Namespace pns = ruleConditionNode.getNamespace();
            String condition = ruleConditionNode.attributeValue(rootQNameGenerator.imsss("condition", pns));
            ruleCondition.setCondition(condition == null ? null : new Token(condition));
            ruleCondition.setReferencedObjective(ruleConditionNode.attributeValue(
                    rootQNameGenerator.imsss("referencedObjective", pns)));
            ruleCondition.setMeasureThreshold(parseDecimal(ruleConditionNode.attributeValue(
                    rootQNameGenerator.imsss("measureThreshold", pns)), 4));
            String op = ruleConditionNode.attributeValue(rootQNameGenerator.imsss("operator", pns));
            if (op != null) {
                ruleCondition.setOperator(new Token(op));
            }
            ruleConditions.getRuleConditionList().add(ruleCondition);
        }
    }

    private void parseRollupRule(RollupRules rollupRules, Element rollupRuleNode) {
        if (rollupRuleNode != null) {
            RollupRule rollupRule = new RollupRule();
            Namespace pns = rollupRuleNode.getNamespace();
            String cas = rollupRuleNode.attributeValue(rootQNameGenerator.imsss("childActivitySet", pns));
            if (cas != null) {
                rollupRule.setChildActivitySet(new Token(cas));
            }
            NonNegativeInteger mc = parseNonNegativeInteger(rollupRuleNode.attributeValue(
                    rootQNameGenerator.imsss("minimumCount", pns)));
            if (mc != null) {
                rollupRule.setMinimumCount(mc);
            }
            Decimal mp = parseDecimal(rollupRuleNode.attributeValue(
                    rootQNameGenerator.imsss("minimumPercent", pns)), 4);
            if (mp != null) {
                rollupRule.setMinimumPercent(mp);
            }
            parseRollupConditions(rollupRule, rollupRuleNode.element(rootQNameGenerator.imsss("rollupConditions")));
            Element actionNode = rollupRuleNode.element(rootQNameGenerator.imsss("rollupAction"));
            String action = actionNode == null ? null : actionNode.attributeValue(
                    rootQNameGenerator.imsss("action", pns));
            if (action != null) {
                rollupRule.setRollupAction(new Token(action));
            }
            rollupRules.getRollupRuleList().add(rollupRule);
        }
    }

    private void parseRollupConditions(RollupRule rollupRule, Element rollupConditionsNode) {
        if (rollupConditionsNode != null) {
            RollupConditions rollupConditions = new RollupConditions();
            String cc = rollupConditionsNode.attributeValue(
                    rootQNameGenerator.imsss("conditionCombination", rollupConditionsNode.getNamespace()));
            if (cc != null) {
                rollupConditions.setConditionCombination(new Token(cc));
            }
            parseRollupCondition(rollupConditions, rollupConditionsNode.element(
                    rootQNameGenerator.imsss("rollupCondition")));
            rollupRule.setRollupConditions(rollupConditions);
        }
    }

    private void parseRollupCondition(RollupConditions rollupConditions, Element rollupConditionNode) {
        if (rollupConditionNode != null) {
            RollupCondition rollupCondition = new RollupCondition();
            Namespace pns = rollupConditionNode.getNamespace();
            String condition = rollupConditionNode.attributeValue(rootQNameGenerator.imsss("condition", pns));
            rollupCondition.setCondition(condition == null ? null : new Token(condition));
            String op = rollupConditionNode.attributeValue(rootQNameGenerator.imsss("operator", pns));
            if (op != null) {
                rollupCondition.setOperator(new Token(op));
            }
            rollupConditions.getRollupConditionList().add(rollupCondition);
        }
    }

    private Objective parseObjective(Element objectiveNode) {
        if (objectiveNode != null) {
            Objective objective = new Objective();
            Namespace pns = objectiveNode.getNamespace();
            Boolean sbm = parseBoolean(objectiveNode.attributeValue(
                    rootQNameGenerator.imsss("satisfiedByMeasure", pns)));
            if (sbm != null) {
                objective.setSatisfiedByMeasure(sbm);
            }
            String oid = objectiveNode.attributeValue(rootQNameGenerator.imsss("objectiveID", pns));
            if (oid != null) {
                objective.setObjectiveID(new AnyURI(oid));
            }
            Decimal mnm = parseDecimal(objectiveNode.elementTextTrim(
                    rootQNameGenerator.imsss("minNormalizedMeasure")), 4);
            if (mnm != null) {
                objective.setMinNormalizedMeasure(mnm);
            }
            for (Object element : objectiveNode.elements(rootQNameGenerator.imsss("mapInfo"))) {
                parseMapInfo(objective, (Element) element);
            }
            return objective;
        }
        return null;
    }

    private void parseMapInfo(Objective objective, Element mapInfoNode) {
        if (mapInfoNode != null) {
            MapInfo mapInfo = new MapInfo();
            Namespace pns = mapInfoNode.getNamespace();
            String toid = mapInfoNode.attributeValue(rootQNameGenerator.imsss("targetObjectiveID", pns));
            mapInfo.setTargetObjectiveID(toid == null ? null : new AnyURI(toid));
            Boolean rss = parseBoolean(mapInfoNode.attributeValue(
                    rootQNameGenerator.imsss("readSatisfiedStatus", pns)));
            if (rss != null) {
                mapInfo.setReadSatisfiedStatus(rss);
            }
            Boolean rnm = parseBoolean(mapInfoNode.attributeValue(
                    rootQNameGenerator.imsss("readNormalizedMeasure", pns)));
            if (rnm != null) {
                mapInfo.setReadNormalizedMeasure(rnm);
            }
            Boolean wss = parseBoolean(mapInfoNode.attributeValue(
                    rootQNameGenerator.imsss("writeSatisfiedStatus", pns)));
            if (wss != null) {
                mapInfo.setWriteSatisfiedStatus(wss);
            }
            Boolean wnm = parseBoolean(mapInfoNode.attributeValue(
                    rootQNameGenerator.imsss("writeNormalizedMeasure", pns)));
            if (wnm != null) {
                mapInfo.setWriteNormalizedMeasure(wnm);
            }
            objective.getMapInfoList().add(mapInfo);
        }
    }

    private void parseAdlseqObjective(AdlseqObjectives adlseqObjectives, Element adlseqObjectiveNode) {
        if (adlseqObjectiveNode != null) {
            AdlseqObjective adlseqObjective = new AdlseqObjective();
            String oid = adlseqObjectiveNode.attributeValue(
                    rootQNameGenerator.adlseq("objectiveID", adlseqObjectiveNode.getNamespace()));
            adlseqObjective.setObjectiveID(oid == null ? null : new AnyURI(oid));
            for (Object element : adlseqObjectiveNode.elements(rootQNameGenerator.adlseq("mapInfo"))) {
                parseAdlSeqMapInfo(adlseqObjective, (Element) element);
            }
            adlseqObjectives.getObjectiveList().add(adlseqObjective);
        }
    }

    private void parseAdlSeqMapInfo(AdlseqObjective adlseqObjective, Element adlseqMapInfoNode) {
        if (adlseqMapInfoNode != null) {
            AdlseqMapInfo adlseqMapInfo = new AdlseqMapInfo();
            Namespace pns = adlseqMapInfoNode.getNamespace();
            String toid = adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("targetObjectiveID", pns));
            adlseqMapInfo.setTargetObjectiveID(toid == null ? null : new AnyURI(toid));
            Boolean readRawScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("readRawScore", pns)));
            if (readRawScore != null) {
                adlseqMapInfo.setReadRawScore(readRawScore);
            }
            Boolean readMinScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("readMinScore", pns)));
            if (readMinScore != null) {
                adlseqMapInfo.setReadMinScore(readMinScore);
            }
            Boolean readMaxScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("readMaxScore", pns)));
            if (readMaxScore != null) {
                adlseqMapInfo.setReadMaxScore(readMaxScore);
            }
            Boolean readCompletionStatus = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("readCompletionStatus", pns)));
            if (readCompletionStatus != null) {
                adlseqMapInfo.setReadCompletionStatus(readCompletionStatus);
            }
            Boolean readProgressMeasure = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("readProgressMeasure", pns)));
            if (readProgressMeasure != null) {
                adlseqMapInfo.setReadProgressMeasure(readProgressMeasure);
            }
            Boolean writeRawScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("writeRawScore", pns)));
            if (writeRawScore != null) {
                adlseqMapInfo.setWriteRawScore(writeRawScore);
            }
            Boolean writeMinScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("writeMinScore", pns)));
            if (writeMinScore != null) {
                adlseqMapInfo.setWriteMinScore(writeMinScore);
            }
            Boolean writeMaxScore = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("writeMaxScore", pns)));
            if (writeMaxScore != null) {
                adlseqMapInfo.setWriteMaxScore(writeMaxScore);
            }
            Boolean writeCompletionStatus = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("writeCompletionStatus", pns)));
            if (writeCompletionStatus != null) {
                adlseqMapInfo.setWriteCompletionStatus(writeCompletionStatus);
            }
            Boolean writeProgressMeasure = parseBoolean(adlseqMapInfoNode.attributeValue(
                    rootQNameGenerator.adlseq("writeProgressMeasure", pns)));
            if (writeProgressMeasure != null) {
                adlseqMapInfo.setWriteProgressMeasure(writeProgressMeasure);
            }
            adlseqObjective.getMapInfoList().add(adlseqMapInfo);
        }
    }

    private Boolean parseBoolean(String s) {
        if ("true".equalsIgnoreCase(s)) {
            return true;
        } else if ("false".equalsIgnoreCase(s)) {
            return false;
        } else {
            return null;
        }
    }

    private Decimal parseDecimal(String s, int scale) {
        if (s == null || "".equals(s)) {
            return null;
        }
        try {
            Double.parseDouble(s);
        } catch (Exception e) {
            return null;
        }
        return new Decimal(s, scale);
    }

    private NonNegativeInteger parseNonNegativeInteger(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }
        try {
            if (Integer.parseInt(s) < 0) {
                return null;
            }
            return new NonNegativeInteger(s);
        } catch (Exception e) {
            return null;
        }
    }

    private class QNameGenerator {
        private Namespace imscpNamespace;
        private Namespace adlcpNamespace;
        private Namespace adlseqNamespace;
        private Namespace adlnavNamespace;
        private Namespace imsssNamespace;
        private Namespace lomNamespace;
        private Namespace xmlNamespace;

        QNameGenerator(Map<String, Namespace> namespaceMap) {
            imscpNamespace = namespaceMap.getOrDefault(NAMESPACE_IMSCP, new Namespace(PREFIX_IMSCP, NAMESPACE_IMSCP));
            adlcpNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLCP, new Namespace(PREFIX_ADLCP, NAMESPACE_ADLCP));
            adlseqNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLSEQ, new Namespace(PREFIX_ADLSEQ, NAMESPACE_ADLSEQ));
            adlnavNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLNAV, new Namespace(PREFIX_ADLNAV, NAMESPACE_ADLNAV));
            imsssNamespace = namespaceMap.getOrDefault(NAMESPACE_IMSSS, new Namespace(PREFIX_IMSSS, NAMESPACE_IMSSS));
            lomNamespace = namespaceMap.getOrDefault(NAMESPACE_LOM, new Namespace(PREFIX_LOM, NAMESPACE_LOM));
            xmlNamespace = namespaceMap.getOrDefault(NAMESPACE_XML, new Namespace(PREFIX_XML, NAMESPACE_XML));
        }

        QNameGenerator(QNameGenerator baseQNameGenerator, Map<String, Namespace> namespaceMap) {
            imscpNamespace = namespaceMap.getOrDefault(NAMESPACE_IMSCP, baseQNameGenerator.imscpNamespace);
            adlcpNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLCP, baseQNameGenerator.adlcpNamespace);
            adlseqNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLSEQ, baseQNameGenerator.adlseqNamespace);
            adlnavNamespace = namespaceMap.getOrDefault(NAMESPACE_ADLNAV, baseQNameGenerator.adlnavNamespace);
            imsssNamespace = namespaceMap.getOrDefault(NAMESPACE_IMSSS, baseQNameGenerator.imsssNamespace);
            lomNamespace = namespaceMap.getOrDefault(NAMESPACE_LOM, baseQNameGenerator.lomNamespace);
            xmlNamespace = namespaceMap.getOrDefault(NAMESPACE_XML, baseQNameGenerator.xmlNamespace);
        }
        
        QName imscp(String elementName) {
            return new QName(elementName, imscpNamespace);
        }

        QName adlcp(String elementName) {
            return new QName(elementName, adlcpNamespace);
        }

        QName adlseq(String elementName) {
            return new QName(elementName, adlseqNamespace);
        }

        QName adlnav(String elementName) {
            return new QName(elementName, adlnavNamespace);
        }

        QName imsss(String elementName) {
            return new QName(elementName, imsssNamespace);
        }

        QName lom(String elementName) {
            return new QName(elementName, lomNamespace);
        }

        QName xml(String elementName) {
            return new QName(elementName, xmlNamespace);
        }

        QName imscp(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, imscpNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, imscpNamespace);
            }
        }

        QName adlcp(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, adlcpNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, adlcpNamespace);
            }
        }

        QName adlseq(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, adlseqNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, adlseqNamespace);
            }
        }

        QName adlnav(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, adlnavNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, adlnavNamespace);
            }
        }

        QName imsss(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, imsssNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, imsssNamespace);
            }
        }

        QName lom(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, lomNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, lomNamespace);
            }
        }

        QName xml(String attributeName, Namespace elementNamespace) {
            if (Objects.equals(elementNamespace, xmlNamespace)) {
                return new QName(attributeName, Namespace.NO_NAMESPACE);
            } else {
                return new QName(attributeName, xmlNamespace);
            }
        }
    }

    public static void main(String[] args) {
        new ContentPackageGenerator().generateContentPackageFromFile("learningserver-scorm/scorm-test-pkg");
    }

}
