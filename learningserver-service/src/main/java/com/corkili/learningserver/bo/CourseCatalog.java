package com.corkili.learningserver.bo;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.HideLMSUI;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Organizations;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class CourseCatalog {

    private String defaultItemId;
    private int maxLevel;
    private int itemNumber;
    private final Map<Integer, List<CourseCatalogItem>> items; // level -> list

    private CourseCatalog() {
        defaultItemId = "";
        maxLevel = 0;
        itemNumber = 0;
        items = new HashMap<>();
    }

    public static CourseCatalog generateFromContentPackage(ContentPackage contentPackage) {
        if (contentPackage == null || contentPackage.getManifest() == null) {
            return null;
        }
        Organizations organizations = contentPackage.getManifest().getOrganizations();
        CourseCatalog courseCatalog = new CourseCatalog();
        if (organizations == null || organizations.getOrganizationList().isEmpty()) {
            return courseCatalog;
        }
        courseCatalog.defaultItemId = organizations.getDefaultOrganizationID().getValue();
        List<Organization> organizationList = organizations.getOrganizationList();
        // level 1
        for (int i = 0; i < organizationList.size(); i++) {
            Organization organization = organizationList.get(i);
            CourseCatalogItem courseCatalogItem = new CourseCatalogItem();
            courseCatalogItem.level = 1;
            courseCatalogItem.index = i;
            courseCatalogItem.itemId = organization.getIdentifier().getValue();
            courseCatalogItem.itemTitle = organization.getTitle();
            courseCatalogItem.selectable = false;
            courseCatalogItem.visible = true;
            courseCatalogItem.parentItem = null;
            if (organization.getItemList() != null) {
                List<Item> itemList = organization.getItemList();
                for (int j = 0; j < itemList.size(); j++) {
                    Item item = itemList.get(j);
                    courseCatalogItem.nextLevelItems.add(generateFrom(item, 2, j, courseCatalogItem, courseCatalog));
                }
            }
            // sync
            if (!courseCatalog.items.containsKey(1)) {
                courseCatalog.items.put(1, new ArrayList<>());
            }
            courseCatalog.items.get(1).add(courseCatalogItem);
            courseCatalog.maxLevel = Math.max(courseCatalog.maxLevel, 1);
            courseCatalog.itemNumber++;
        }
        return courseCatalog;
    }

    private static CourseCatalogItem generateFrom(Item item, int currentLevel, int currentIndex,
                                                  CourseCatalogItem parent, CourseCatalog courseCatalog) {
        CourseCatalogItem courseCatalogItem = new CourseCatalogItem();
        courseCatalogItem.level = currentLevel;
        courseCatalogItem.index = currentIndex;
        courseCatalogItem.itemId = item.getIdentifier().getValue();
        courseCatalogItem.itemTitle = item.getTitle();
        courseCatalogItem.selectable = item.getIdentifierref() != null && (item.getItemList() == null || item.getItemList().isEmpty());
        courseCatalogItem.visible = item.isIsvisible();
        if (item.getPresentation() != null && item.getPresentation().getNavigationInterface() != null
                && item.getPresentation().getNavigationInterface().getHideLMSUIList() != null) {
            for (HideLMSUI hideLMSUI : item.getPresentation().getNavigationInterface().getHideLMSUIList()) {
                courseCatalogItem.hideLmsUI.add(hideLMSUI.getValue());
            }
        }
        courseCatalogItem.parentItem = parent;
        if (item.getItemList() != null) {
            List<Item> itemList = item.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                Item childItem = itemList.get(i);
                courseCatalogItem.nextLevelItems.add(generateFrom(childItem, currentLevel + 1, i,
                        courseCatalogItem, courseCatalog));
            }
        }
        // sync
        if (!courseCatalog.items.containsKey(currentLevel)) {
            courseCatalog.items.put(currentLevel, new ArrayList<>());
        }
        courseCatalog.items.get(currentLevel).add(courseCatalogItem);
        courseCatalog.maxLevel = Math.max(courseCatalog.maxLevel, currentLevel);
        courseCatalog.itemNumber++;
        return courseCatalogItem;
    }

    @Getter
    @Setter
    public static class CourseCatalogItem implements Comparable<CourseCatalogItem> {
        private int level; // from 1
        private int index;
        private String itemId;
        private String itemTitle;
        private boolean selectable;
        private boolean visible;
        private final Set<String> hideLmsUI;
        private CourseCatalogItem parentItem;
        private final List<CourseCatalogItem> nextLevelItems;

        private CourseCatalogItem() {
            level = 0;
            index = -1;
            itemId = "";
            itemTitle = "";
            selectable = false;
            visible = true;
            hideLmsUI = new HashSet<>();
            parentItem = null;
            nextLevelItems = new ArrayList<>();
        }

        @Override
        public int compareTo(CourseCatalogItem o) {
            int arg = this.level - o.level;
            if (arg != 0) {
                return arg;
            } else {
                return this.index - o.index;
            }
        }
    }

}
