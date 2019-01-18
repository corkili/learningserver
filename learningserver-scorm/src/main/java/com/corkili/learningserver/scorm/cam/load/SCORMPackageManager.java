package com.corkili.learningserver.scorm.cam.load;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;

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
        System.out.println(ZipUtils.decompressZip(zipFilePath, saveDir));
        return null;
    }

    public static void main(String[] args) {
        SCORMPackageManager.getInstance().loadSCORMContentPackageFromZipFile("learningserver-scorm/scorm.zip");
    }

}
