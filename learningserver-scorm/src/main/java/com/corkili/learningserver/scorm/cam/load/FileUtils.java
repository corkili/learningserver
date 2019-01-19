package com.corkili.learningserver.scorm.cam.load;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    public static List<File> getAllFiles(File srcFile) {
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

    public static String getRelativePath(String dirPath, File file) {
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

    public static File createFile(String dstPath, String fileName) throws IOException {
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
