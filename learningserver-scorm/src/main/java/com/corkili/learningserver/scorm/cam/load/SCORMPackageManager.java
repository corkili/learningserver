package com.corkili.learningserver.scorm.cam.load;

import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.common.LMSPersistDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SCORMPackageManager {

    private static SCORMPackageManager instance;

    private Map<String, ContentPackage> contentPackageMap;

    private LMSPersistDriverManager lmsPersistDriverManager;

    private SCORMPackageManager() {
        contentPackageMap = new ConcurrentHashMap<>();
        lmsPersistDriverManager = LMSPersistDriverManager.getInstance();
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

    public ContentPackage launch(String lmsContentPackageID) {
        return launch(lmsContentPackageID, false);
    }


    public ContentPackage launch(String lmsContentPackageID, boolean reloadIfPresent) {
        ContentPackage contentPackage = contentPackageMap.get(lmsContentPackageID);
        if (contentPackage != null && !reloadIfPresent) {
            return contentPackage;
        }
        if (lmsPersistDriverManager.getDriver() == null) {
            log.error("not found lms persist driver");
            return null;
        }
        String zipFilePath = lmsPersistDriverManager.getDriver().querySCORMPackageZipFilePathBy(lmsContentPackageID);
        if (zipFilePath == null) {
            log.error("not found scorm package for {}", lmsContentPackageID);
            return null;
        }
        contentPackage = loadSCORMContentPackageFromZipFile(lmsContentPackageID, zipFilePath);
        if (contentPackage == null) {
            log.error("load scorm content package error");
            return null;
        }
        return contentPackage;
    }

    public void unlaunch(String lmsContentPackageID) {
        ContentPackage contentPackage = contentPackageMap.remove(lmsContentPackageID);
        if (contentPackage != null) {
            try {
                FileUtils.deleteDirectory(new File(contentPackage.getBasePath()));
            } catch (IOException ignored) {

            }
        }
    }

    private ContentPackage loadSCORMContentPackageFromZipFile(String lmsContentPackageID, String zipFilePath) {
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

    public int contentPackageCount() {
        return contentPackageMap.size();
    }

    public static void main(String[] args) {
        System.out.println(SCORMPackageManager.getInstance()
                .loadSCORMContentPackageFromZipFile("1", "learningserver-scorm/scorm-test-pkg.zip")
                .getContent());
    }

}
