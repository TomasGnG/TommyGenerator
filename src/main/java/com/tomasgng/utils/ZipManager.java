package com.tomasgng.utils;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class ZipManager {
    public static void zip (String targetPath, String destinationFilePath) throws IOException {
        var zipFile = new ZipFile(destinationFilePath);
        var targetFile = new File(targetPath);
        BasicFileAttributes basicFileAttributes = Files.readAttributes(targetFile.toPath(), BasicFileAttributes.class);
        if (basicFileAttributes.isRegularFile()) {
            zipFile.addFile(targetFile);
        } else if (basicFileAttributes.isDirectory()) {
            zipFile.addFolder(targetFile);
        } else {
            //neither file nor directory
        }
    }

    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.extractAll(destinationFolderPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
