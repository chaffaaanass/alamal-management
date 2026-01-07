package com.projects.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BackupScheduler {

    @Autowired
    private DatabaseBackup backupService;

    // Create backup on application startup
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void startupBackup() {
        try {
            System.out.println("Creating startup backup...");
            backupService.createScheduledBackup();
            System.out.println("Startup backup completed.");
        } catch (Exception e) {
            System.err.println("Failed to create startup backup: " + e.getMessage());
        }
    }
}