package com.corkili.learningserver.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@Slf4j
public class ScormZipUtils {

    private static final String basePath = "../scormPackages/";

    private static final String zipSuffix = ".zip";

    public static String getScormZipPath(String scormZipName, long userId) {
        while (scormZipName.startsWith("/")) {
            scormZipName = scormZipName.substring(1);
        }
        while (scormZipName.endsWith("/")) {
            scormZipName = scormZipName.substring(0, scormZipName.length() - 1);
        }
        if (!scormZipName.toLowerCase().endsWith(zipSuffix)) {
            scormZipName += zipSuffix;
        }
        return ServiceUtils.format("scorm-{}-{}-{}", userId,
                UUID.randomUUID().toString().replaceAll("-", ""), scormZipName);
    }

    public static boolean storeScormZips(Map<String, byte[]> scormZips) {
        if (scormZips == null) {
            return true;
        }
        boolean storeSuccess = true;
        for (Entry<String, byte[]> entry : scormZips.entrySet()) {
            if (!ScormZipUtils.storeScormZip(entry.getKey(), entry.getValue())) {
                storeSuccess = false;
            }
        }
        if (!storeSuccess) {
            for (String path : scormZips.keySet()) {
                ScormZipUtils.deleteScormZip(path);
            }
        }
        return storeSuccess;
    }

    public static boolean storeScormZip(String scormZipPath, byte[] scormZipData) {
        while (scormZipPath.startsWith("/")) {
            scormZipPath = scormZipPath.substring(1);
        }
        String path = basePath + scormZipPath;
        try {
            File file = new File(path);
            file.deleteOnExit();
            FileUtils.writeByteArrayToFile(file, scormZipData);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(scormZipData);
        } catch (IOException e) {
            log.error("store scormZip exception - {}", ServiceUtils.stringifyError(e));
            return false;
        }
        return true;
    }

    public static byte[] readScormZip(String scormZipPath) {
        while (scormZipPath.startsWith("/")) {
            scormZipPath = scormZipPath.substring(1);
        }
        String path = basePath + scormZipPath;
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            log.error("read scormZip exception - {}", ServiceUtils.stringifyError(e));
            return new byte[0];
        }
    }

    public static void deleteScormZip(String scormZipPath) {
        while (scormZipPath.startsWith("/")) {
            scormZipPath = scormZipPath.substring(1);
        }
        String path = basePath + scormZipPath;
        try {
            FileUtils.forceDelete(new File(path));
            if (scormZipPath.toLowerCase().endsWith(zipSuffix)) {
                FileUtils.forceDelete(new File(path.substring(0, scormZipPath.length() - zipSuffix.length())));
            }
        } catch (IOException e) {
            log.error("delete scormZip exception - {}", ServiceUtils.stringifyError(e));
        }
    }

    public static void deleteScormZips(Collection<String> scormZipPaths) {
        if (scormZipPaths != null) {
            scormZipPaths.forEach(ScormZipUtils::deleteScormZip);
        }
    }
    
}
