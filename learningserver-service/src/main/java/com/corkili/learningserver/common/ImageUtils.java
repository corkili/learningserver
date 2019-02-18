package com.corkili.learningserver.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {

    private static final String basePath = "../images/";

    public static String getImagePath(String entityName, String imageName, long teacherId) {
        while (imageName.startsWith("/")) {
            imageName = imageName.substring(1);
        }
        return ServiceUtils.format("{}-{}-{}-{}", entityName, teacherId,
                UUID.randomUUID().toString().replaceAll("-", ""), imageName);
    }

    public static boolean storeImages(Map<String, byte[]> images) {
        if (images == null) {
            return true;
        }
        boolean storeSuccess = true;
        for (Entry<String, byte[]> entry : images.entrySet()) {
            if (!ImageUtils.storeImage(entry.getKey(), entry.getValue())) {
                storeSuccess = false;
            }
        }
        if (!storeSuccess) {
            for (String path : images.keySet()) {
                ImageUtils.deleteImage(path);
            }
        }
        return storeSuccess;
    }

    public static boolean storeImage(String imagePath, byte[] imageData) {
        while (imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }
        String path = basePath + imagePath;
        try {
            File file = new File(path);
            file.deleteOnExit();
            FileUtils.writeByteArrayToFile(file, imageData);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(imageData);
        } catch (IOException e) {
            log.error("store image exception - {}", ServiceUtils.stringifyError(e));
            return false;
        }
        return true;
    }

    public static byte[] readImage(String imagePath) {
        while (imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }
        String path = basePath + imagePath;
        try {
            return FileUtils.readFileToByteArray(new File(path));
        } catch (IOException e) {
            log.error("read image exception - {}", ServiceUtils.stringifyError(e));
            return new byte[0];
        }
    }

    public static void deleteImage(String imagePath) {
        while (imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }
        String path = basePath + imagePath;
        try {
            FileUtils.forceDelete(new File(path));
        } catch (IOException e) {
            log.error("delete image exception - {}", ServiceUtils.stringifyError(e));
        }
    }

    public static void deleteImages(Collection<String> imagePaths) {
        if (imagePaths != null) {
            imagePaths.forEach(ImageUtils::deleteImage);
        }
    }
}
