package com.corkili.learningserver.scorm.cam.load;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 未进行测试
     */
    public static boolean compressZip(String srcPath, String zipFilePath) {
        if (!isEndWithZip(zipFilePath)) {
            zipFilePath += ".zip";
        }
        File srcFile = new File(srcPath);
        List<File> fileList = FileUtils.getAllFiles(srcFile);
        byte[] buffer = new byte[BUFFER_SIZE];
        ZipEntry zipEntry;
        int readLen;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))){
            for (File file : fileList) {
                if (file.isFile()) {
                    zipEntry = new ZipEntry(FileUtils.getRelativePath(srcPath, file));
                    zipEntry.setSize(file.length());
                    zipEntry.setTime(file.lastModified());
                    zipOutputStream.putNextEntry(zipEntry);

                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                    while ((readLen = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        zipOutputStream.write(buffer, 0, readLen);
                    }

                    inputStream.close();
                } else {
                    zipEntry = new ZipEntry(FileUtils.getRelativePath(srcPath, file) + "/");
                    zipOutputStream.putNextEntry(zipEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean decompressZip(String zipFilePath, String saveFileDir) {
        if (!isEndWithZip(zipFilePath)) {
            return false;
        }
        if (!new File(zipFilePath).exists()) {
            return false;
        }
        if (!saveFileDir.endsWith("/")) {
            saveFileDir += "/";
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            byte[] buffer = new byte[BUFFER_SIZE];
            int readLen;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    File dir = new File(saveFileDir + zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    File file = FileUtils.createFile(saveFileDir, zipEntry.getName());
                    OutputStream outputStream = new FileOutputStream(file);

                    while ((readLen = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        outputStream.write(buffer, 0, readLen);
                    }

                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isEndWithZip(String zipFilePath) {
        return zipFilePath != null && !"".equals(zipFilePath.trim())
                && (zipFilePath.endsWith(".ZIP") || zipFilePath.endsWith(".zip"));
    }

}
