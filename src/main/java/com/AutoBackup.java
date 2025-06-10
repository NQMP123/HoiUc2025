/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.*;

/**
 *
 * @author Administrator
 */
public class AutoBackup {

    public static void start() {
        String sourceFolderPath = "./src/main/java/";
        // Đường dẫn lưu file backup
        String backupFolderPath = "./backup";

        // Tạo tên file backup với ngày tháng năm
        String dateStr = new SimpleDateFormat("dd_MM_yyyy___HH'h'_mm'm'_ss's'").format(new Date());
        String backupFileName = "backup_" + dateStr + ".zip";
        String backupFilePath = backupFolderPath + File.separator + backupFileName;

        // Tạo folder backup nếu chưa tồn tại
        File backupDir = new File(backupFolderPath);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        // Thực hiện backup
        try {
            zipFolder(Paths.get(sourceFolderPath), Paths.get(backupFilePath));
            System.out.println("Backup thành công tại: " + backupFilePath);
        } catch (IOException e) {
            com.ngocrong.NQMP.UtilsNQMP.logError(e);
            System.err.println("Lỗi trong quá trình backup: " + e.getMessage());
        }
    }

    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(sourceFolderPath)
                    .filter(path -> !Files.isDirectory(path)) // Lọc các file (bỏ folder)
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceFolderPath.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            com.ngocrong.NQMP.UtilsNQMP.logError(e);
                            System.err.println("Lỗi khi nén file: " + path + " - " + e.getMessage());
                        }
                    });
        }
    }
}
