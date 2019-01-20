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

        // step 1: uncompress
        String saveDir = zipFilePath.substring(0, zipFilePath.length() - 4) + "/";
        if (!ZipUtils.decompressZip(zipFilePath, saveDir)) {
            log.error("uncompress error.");
            return null;
        }

        // step 2: validate all xml file in saveDir
        //noinspection ResultOfMethodCallIgnored
        if (!XmlSchemaValidator.validateAllXmlFileWithSchemaFileInPath(saveDir)) {
            log.error("validate xml schema error.");
            return null;
        }

        // step 3: 读取imsmanifest.xml，生成ContentPackage
        ContentPackage contentPackage = new ContentPackageGenerator().generateContentPackageFromFile(saveDir);
        if (contentPackage == null) {
            log.error("generate content package error.");
            return null;
        }
        System.out.println(contentPackage);

        // step 4: 验证ContentPackage
        ContentPackageValidator validator = new ContentPackageValidator();
        boolean validateResult = validator.validate(contentPackage);
        if (!validateResult) {
            log.error("validate content package error: " + validator.getErrorList());
            return null;
        }
        return null;
    }

    public static void main(String[] args) {
        SCORMPackageManager.getInstance().loadSCORMContentPackageFromZipFile("learningserver-scorm/scorm-test-pkg.zip");
    }

}
