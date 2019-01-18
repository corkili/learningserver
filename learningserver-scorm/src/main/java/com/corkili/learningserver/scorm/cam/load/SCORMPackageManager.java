package com.corkili.learningserver.scorm.cam.load;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;

@Slf4j
public class SCORMPackageManager {

    private static SCORMPackageManager instance;

    private SCORMPackageManager() {

    }

    public static SCORMPackageManager getInstance() {
        if (instance == null) {
            synchronized (SCORMPackageManager.class) {
                if (instance == null) {
                    instance = new SCORMPackageManager();
                }
            }
        }
        return instance;
    }

    public ContentPackage loadSCORMContentPackageFromZipFile(String zipFilePath) {
        if (!ZipUtils.isEndWithZip(zipFilePath)) {
            return null;
        }
        String saveDir = zipFilePath.substring(0, zipFilePath.length() - 4);
        if (ZipUtils.decompressZip(zipFilePath, saveDir)) {
            log.error("compress error.");
            return null;
        }

        return null;
    }

    public static void main(String[] args) {
        SCORMPackageManager.getInstance().loadSCORMContentPackageFromZipFile("learningserver-scorm/scorm.zip");
    }

}
