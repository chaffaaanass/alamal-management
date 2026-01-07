package com.projects.demo.controllers;

import com.projects.demo.security.DatabaseBackup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
@CrossOrigin(origins = "*")
public class DatabaseBackupController {

    @Autowired
    private DatabaseBackup backupService;

    /*
     * Create a backup and return it as a downloadable file
     */
    @PostMapping("/backup")
    public ResponseEntity<Resource> createBackup(@RequestParam(required = false, defaultValue = "false") boolean compress)
            throws IOException {

        String backupPath;

        if (compress) {
            backupPath = backupService.createCompressedBackup();
        } else {
            backupPath = backupService.createBackup();
        }

        File backupFile = new File(backupPath);

        if (!backupFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(backupFile);

        String contentType = compress ? "application/zip" : "application/x-sqlite3";
        String filename = backupFile.getName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /*
     * Get backup statistics
     */
    @GetMapping("/backup/statistics")
    public ResponseEntity<Map<String, Object>> getBackupStatistics() {
        Map<String, Object> stats = backupService.getBackupStatistics();
        return ResponseEntity.ok(stats);
    }

    /*
     * Create backup and return info
     */
    @PostMapping("/backup/info")
    public ResponseEntity<Map<String, Object>> createBackupInfo() throws IOException {
        String backupPath = backupService.createBackup();
        File backupFile = new File(backupPath);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Backup created successfully");
        response.put("backupPath", backupPath);
        response.put("fileName", backupFile.getName());
        response.put("fileSize", backupFile.length());
        response.put("timestamp", backupFile.lastModified());

        // Add statistics
        response.putAll(backupService.getBackupStatistics());

        return ResponseEntity.ok(response);
    }

    /*
     * Get list of available backups (newest first)
     */
    @GetMapping("/backups")
    public ResponseEntity<Map<String, Object>> listBackups() {
        List<String> backups = backupService.listBackupsNewestFirst()
                .stream()
                .map(File::getName)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("backups", backups);
        response.put("count", backups.size());
        response.putAll(backupService.getBackupStatistics());

        return ResponseEntity.ok(response);
    }

    /*
     * Manually trigger cleanup of old backups
     */
    @DeleteMapping("/backups/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupBackups() {
        int deletedCount = backupService.cleanupOldBackups();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cleanup completed");
        response.put("deletedCount", deletedCount);
        response.putAll(backupService.getBackupStatistics());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/restore/{fileName}")
    public ResponseEntity<Map<String, Object>> restoreSpecificBackup(@PathVariable String fileName) {
        try {
            String backupPath = "../backups/" + fileName;
            boolean success = backupService.restoreDatabase(backupPath);

            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "Database restored from " + fileName);
                response.put("status", "SUCCESS");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Restore failed");
                response.put("status", "FAILED");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Restore failed: " + e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("databaseSize", backupService.getDatabaseSize());
        info.putAll(backupService.getBackupStatistics());

        return ResponseEntity.ok(info);
    }
}