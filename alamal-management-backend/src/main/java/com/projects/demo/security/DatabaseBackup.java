package com.projects.demo.security;

import com.projects.demo.AlamalManagementBackendApplication;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DatabaseBackup {
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    private static final String BACKUP_DIR = "../backups/";
    private static final int MAX_BACKUPS = 8;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConfigurableApplicationContext context;

    // Remove initialization from constructor
    public DatabaseBackup() {
        // Just create backup directory structure
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
    }

    // Initialize after properties are set
    @PostConstruct
    public void init() {
        try {
            System.out.println("Initializing DatabaseBackupService with database URL: " + databaseUrl);
            initializeBackupDirectory();
        } catch (Exception e) {
            System.err.println("Error in @PostConstruct init: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Initialize backup directory and ensure it has at least one backup
    private void initializeBackupDirectory() {
        try {
            System.out.println("Initializing backup directory...");
            File backupDir = new File(BACKUP_DIR);
            File[] existingBackups = listBackupFilesSorted();

            System.out.println("Found " + existingBackups.length + " existing backups");

            // If no backups exist, create one on startup
            if (existingBackups.length == 0) {
                createBackupOnStartup();
            }
        } catch (Exception e) {
            System.err.println("Error initializing backup directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create initial backup on system startup
    private void createBackupOnStartup() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFileName = BACKUP_DIR + "initial_backup_" + timestamp + ".sqlite";

        String dbPath = databaseUrl.replace("jdbc:sqlite:", "");
        File sourceFile = new File(dbPath);

        if (sourceFile.exists()) {
            File backupFile = new File(backupFileName);
            FileUtils.copyFile(sourceFile, backupFile);
            System.out.println("Initial backup created on startup: " + backupFileName);
        }
    }

    /*
     * Create a backup of the database with rolling strategy
     * @return Path to the backup file
    */
    public String createBackup() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFileName = BACKUP_DIR + "backup_" + timestamp + ".sqlite";

        // Extract database path from the URL
        String dbPath = databaseUrl.replace("jdbc:sqlite:", "");
        File sourceFile = new File(dbPath);

        if (!sourceFile.exists()) {
            throw new IOException("Database file not found: " + dbPath);
        }

        File backupFile = new File(backupFileName);

        // Copy the database file
        FileUtils.copyFile(sourceFile, backupFile);

        // Apply rolling backup strategy
        applyRollingBackupStrategy();

        return backupFileName;
    }

    /*
     * Create a compressed backup with rolling strategy
     * @return Path to the compressed backup
     */
    public String createCompressedBackup() throws IOException {
        String backupPath = createBackup();
        String zipPath = backupPath + ".zip";

        // Create zip file
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipPath)))) {
            File fileToZip = new File(backupPath);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = Files.readAllBytes(fileToZip.toPath());
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        }

        // Delete the uncompressed backup file
        Files.delete(Paths.get(backupPath));

        // Rename zip file to remove .sqlite from name
        String newZipPath = zipPath.replace(".sqlite.zip", ".zip");
        Files.move(Paths.get(zipPath), Paths.get(newZipPath));

        // Apply rolling strategy again for zip files
        applyRollingBackupStrategy();

        return newZipPath;
    }

    /*
     * Apply FIFO rolling backup strategy
     * Keeps only the latest MAX_BACKUPS files
     */
    private void applyRollingBackupStrategy() {
        try {
            File[] backupFiles = listBackupFilesSorted();

            // Delete oldest files if we exceed MAX_BACKUPS
            if (backupFiles.length > MAX_BACKUPS) {
                int filesToDelete = backupFiles.length - MAX_BACKUPS;
                System.out.println("Files to delete count : " + filesToDelete);

                for (int i = 0; i < filesToDelete; i++) {
                    File oldestFile = backupFiles[i];
                    boolean deleted = oldestFile.delete();

                    if (deleted) {
                        System.out.println("Deleted old backup (rolling strategy): " + oldestFile.getName());
                    } else {
                        System.err.println("Failed to delete old backup: " + oldestFile.getName());
                    }
                }
            }

            // Log current backup status
            logBackupStatus();

        } catch (Exception e) {
            System.err.println("Error applying rolling backup strategy: " + e.getMessage());
        }
    }

    /*
     * List backup files sorted by modification date (oldest first)
     * @return Sorted array of backup files
     */
    private File[] listBackupFilesSorted() {
        File backupDir = new File(BACKUP_DIR);
        File[] backupFiles = backupDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".sqlite") ||
                        name.toLowerCase().endsWith(".zip") ||
                        name.toLowerCase().endsWith(".db"));

        if (backupFiles == null) {
            return new File[0];
        }

        // Sort by last modified date (oldest first)
        Arrays.sort(backupFiles, Comparator.comparingLong(File::lastModified));

        return backupFiles;
    }

    /*
     * List backup files sorted by modification date (newest first for display)
     * @return Sorted list of backup files
     */
    public List<File> listBackupsNewestFirst() {
        File[] backupFiles = listBackupFilesSorted();
        List<File> backups = Arrays.asList(backupFiles);

        // Reverse to show newest first
        Collections.reverse(backups);

        return backups;
    }

    /*
     * Get backup statistics
     * @return Map with backup statistics
     */
    public Map<String, Object> getBackupStatistics() {
        File[] backupFiles = listBackupFilesSorted();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBackups", backupFiles.length);
        stats.put("maxAllowed", MAX_BACKUPS);
        stats.put("availableSlots", Math.max(0, MAX_BACKUPS - backupFiles.length));
        stats.put("oldestBackup", backupFiles.length > 0 ? backupFiles[0].getName() : "None");
        stats.put("latestBackup", backupFiles.length > 0 ? backupFiles[backupFiles.length - 1].getName() : "None");
        stats.put("totalSize", Arrays.stream(backupFiles).mapToLong(File::length).sum());

        return stats;
    }

    /*
     * Log current backup status
     */
    private void logBackupStatus() {
        File[] backupFiles = listBackupFilesSorted();

        System.out.println("=== Backup Status ===");
        System.out.println("Total backups: " + backupFiles.length + "/" + MAX_BACKUPS);

        if (backupFiles.length > 0) {
            System.out.println("Oldest: " + backupFiles[0].getName() +
                    " (" + new Date(backupFiles[0].lastModified()) + ")");
            System.out.println("Latest: " + backupFiles[backupFiles.length - 1].getName() +
                    " (" + new Date(backupFiles[backupFiles.length - 1].lastModified()) + ")");
        }

        System.out.println("====================");
    }

    /*
     * Manually clean up old backups (if needed)
     * @return Number of deleted files
     */
    public int cleanupOldBackups() {
        File[] backupFiles = listBackupFilesSorted();
        int initialCount = backupFiles.length;

        applyRollingBackupStrategy();

        File[] remainingFiles = listBackupFilesSorted();
        return initialCount - remainingFiles.length;
    }

    /*
     * Create a scheduled backup (call this from a scheduled task)
     * @return Backup file path
     */
    public String createScheduledBackup() throws IOException {
        System.out.println("Creating scheduled backup...");
        String backupPath = createBackup();
        System.out.println("Scheduled backup created: " + backupPath);
        return backupPath;
    }

    /*
     * Restore database from a backup file
     * @param backupFile MultipartFile containing the backup
     * @return true if successful
     */

    public boolean restoreDatabase(String backupFilePath) throws IOException {
        File backupFile = new File(backupFilePath);
        if (!backupFile.exists()) {
            throw new IOException("Backup file not found: " + backupFilePath);
        }

        String dbPath = databaseUrl.replace("jdbc:sqlite:", "");
        File originalFile = new File(dbPath);

        // Create a backup of current database before restore
        String preRestoreBackup = createBackup();
        System.out.println("Pre-restore backup created: " + preRestoreBackup);

        try {
            // 2. Shut down the connection pool to release the file lock
            if (dataSource instanceof HikariDataSource) {
                ((HikariDataSource) dataSource).close();
                System.out.println("Database connection pool closed.");
            }

            // 3. Perform the file copy
            FileUtils.copyFile(backupFile, originalFile);

            // 4. Handle SQLite WAL files if they exist (crucial for SQLite)
            new File(dbPath + "-wal").delete();
            new File(dbPath + "-shm").delete();

            // 5. Trigger a restart in a separate thread so this method can return
            restartApplication();

            return true;
        } catch (Exception e) {
            System.err.println("Restore failed! Attempting to roll back to: " + preRestoreBackup);
            File backup = new File(preRestoreBackup);
            FileUtils.copyFile(backup, originalFile);
            throw e;
        }
    }

    private void restartApplication() {
        Thread restartThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Give time for the response to reach the user
                context.close();
                // This assumes you are using the standard SpringBoot layout
                SpringApplication.run(AlamalManagementBackendApplication.class);
            } catch (Exception e) {
                System.exit(0); // Exit and let Docker/Systemd restart the app
            }
        });
        restartThread.setDaemon(false);
        restartThread.start();
    }
    /*
     * Get the current database file size
     * @return File size in bytes
     */
    public long getDatabaseSize() {
        String dbPath = databaseUrl.replace("jdbc:sqlite:", "");
        File dbFile = new File(dbPath);
        return dbFile.exists() ? dbFile.length() : 0;
    }
}
