package com.corkili.learningserver.scorm.cam.load;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    private static final int BUFFER_SIZE = 1024;

    public static boolean compressZip(File[] files, String zipFilePath) {
        if (files == null || files.length <= 0) {
            return false;
        }
        if (!isEndWithZip(zipFilePath)) {
            zipFilePath += ".zip";
        }
        File zipFile = new File(zipFilePath);
        // TODO 压缩方法
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
            int readLen = 0;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    File dir = new File(saveFileDir + zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    File file = createFile(saveFileDir, zipEntry.getName());
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

    private static List<File> getAllFiles(File srcFile) {
        List<File> fileList = new ArrayList<>();
        File[] files = srcFile.listFiles();
        for (File f : Objects.requireNonNull(files)) {
            if (f.isFile()) {
                fileList.add(f);
            }
            if (f.isDirectory()) {
                if (Objects.requireNonNull(f.listFiles()).length != 0) {
                    fileList.addAll(getAllFiles(f));
                } else {
                    fileList.add(f);
                }
            }
        }
        return fileList;
    }

    private static String getRelativePath(String dirPath, File file) {
        File dir = new File(dirPath);
        String relativePath = file.getName();
        while (true) {
            file = file.getParentFile();
            if (file == null) {
                break;
            }
            if (file.equals(dir)) {
                break;
            } else {
                relativePath = file.getName() + "/" + relativePath;
            }
        }
        return relativePath;
    }

    private static File createFile(String dstPath, String fileName) throws IOException {
        String[] dirs = fileName.split("/");
        File file = new File(dstPath);

        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, dirs[dirs.length - 1]);
        } else {
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, dirs[0]);
        }
        return file;
    }

}
