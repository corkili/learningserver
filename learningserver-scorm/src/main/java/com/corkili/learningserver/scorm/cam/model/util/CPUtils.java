package com.corkili.learningserver.scorm.cam.model.util;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.load.ModelUtils;
import com.corkili.learningserver.scorm.cam.model.AdlseqMapInfo;
import com.corkili.learningserver.scorm.cam.model.AdlseqObjective;
import com.corkili.learningserver.scorm.cam.model.AdlseqObjectives;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.MapInfo;
import com.corkili.learningserver.scorm.cam.model.Objective;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.SequencingCollection;
import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public final class CPUtils {

    public static Item findItemByIdentifier(ContentPackage contentPackage, String identifier) {
        if (contentPackage == null) {
            return null;
        }
        for (Organization organization : contentPackage.getManifest().getOrganizations().getOrganizationList()) {
            for (Item item : organization.getItemList()) {
                Item i = findItemByIdentifier(item, identifier);
                if (i != null) {
                    return i;
                }
            }
        }
        return null;
    }

    private static Item findItemByIdentifier(Item item, String identifier) {
        if (StringUtils.equals(identifier, item.getIdentifier().getValue())) {
            return item;
        }
        for (Item i : item.getItemList()) {
            Item it = findItemByIdentifier(i, identifier);
            if (it != null) {
                return it;
            }
        }
        return null;
    }

    public static AdlseqObjective findAdlseqObjectiveByID(AdlseqObjectives adlseqObjectives, AnyURI id) {
        if (adlseqObjectives == null || ModelUtils.isAnyUriEmpty(id)) {
            return null;
        }
        for (AdlseqObjective adlseqObjective : adlseqObjectives.getObjectiveList()) {
            if (adlseqObjective.getObjectiveID() != null && id.getValue().equals(adlseqObjective.getObjectiveID().getValue())) {
                return adlseqObjective;
            }
        }
        return null;
    }

    public static AdlseqMapInfo findAdlseqMapInfoByID(AdlseqObjective adlseqObjective, AnyURI targetObjectiveID) {
        if (adlseqObjective == null || ModelUtils.isAnyUriEmpty(targetObjectiveID)) {
            return null;
        }
        for (AdlseqMapInfo adlseqMapInfo : adlseqObjective.getMapInfoList()) {
            if (adlseqMapInfo.getTargetObjectiveID().getValue().equals(targetObjectiveID.getValue())) {
                return adlseqMapInfo;
            }
        }
        return null;
    }

    public static MapInfo findMapInfoByID(Objective objective, AnyURI targetObjectiveID) {
        if (objective == null || ModelUtils.isAnyUriEmpty(targetObjectiveID)) {
            return null;
        }
        for (MapInfo mapInfo : objective.getMapInfoList()) {
            if (mapInfo.getTargetObjectiveID().getValue().equals(targetObjectiveID.getValue())) {
                return mapInfo;
            }
        }
        return null;
    }

    public static Sequencing findSequencingByID(SequencingCollection sequencingCollection, String id) {
        if (sequencingCollection == null) {
            return null;
        }
        for (Sequencing sequencing : sequencingCollection.getSequencingList()) {
            if (StringUtils.equals(id, sequencing.getIdRef().getValue())) {
                return sequencing;
            }
        }
        return null;
    }

}
