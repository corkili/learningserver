package com.corkili.learningserver.scorm.cam.load;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;

@Slf4j
public class SCORMPackageManager {

    private static SCORMPackageManager instance;

    private Map<String, ContentPackage> contentPackageMap;

    private SCORMPackageManager() {
        contentPackageMap = new ConcurrentHashMap<>();
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

    public ContentPackage loadSCORMContentPackageFromZipFile(String lmsContentPackageID,String zipFilePath) {
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

        // step 4: 验证ContentPackage
        ContentPackageValidator validator = new ContentPackageValidator(false);
        boolean validateResult = validator.validate(contentPackage, saveDir);
        if (!validateResult) {
            log.error("validate content package error: " + validator.getErrors());
            return null;
        }

        contentPackageMap.put(lmsContentPackageID, contentPackage);

        return contentPackage;
    }

    public ContentPackage getContentPackage(String lmsContentPackageID) {
        return contentPackageMap.get(lmsContentPackageID);
    }

    public int contentPackageCount() {
        return contentPackageMap.size();
    }

    public static void main(String[] args) {
        System.out.println(SCORMPackageManager.getInstance()
                .loadSCORMContentPackageFromZipFile("1", "learningserver-scorm/scorm-test-pkg.zip"));
    }

}
