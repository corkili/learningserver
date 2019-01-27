package com.corkili.learningserver.scorm.cam.model.util;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.SequencingCollection;

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
