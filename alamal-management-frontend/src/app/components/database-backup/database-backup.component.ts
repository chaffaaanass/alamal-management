import { Component, OnInit } from '@angular/core';
import { DatabaseBackupService } from '../../services/database/database-backup.service';
import { saveAs } from 'file-saver';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-database-backup',
  imports: [CommonModule, FormsModule],
  templateUrl: './database-backup.component.html',
  styleUrl: './database-backup.component.css',
})
export class DatabaseBackupComponent implements OnInit {
  backups: any[] = [];
  selectedFile: File | null = null;
  isCreatingBackup = false;
  isRestoring = false;
  message = '';
  messageType = '';
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private backupService: DatabaseBackupService) {}

  ngOnInit(): void {
    this.loadBackups();
  }

  loadBackups(): void {
    this.loading = true;
    this.backupService.listBackups().subscribe({
      next: (response: any) => {
        this.backups = response.backups || [];
        console.log(this.backups);
        this.loading = false;
      },
      error: (error) => {
        this.showMessage('Error loading backups', 'error');
      },
    });
  }

  formatBackupDate(filename: string): string {
    const match = filename.match(/backup_(\d{8})_(\d{6})/);

    if (!match) {
      return filename; // fallback
    }

    const datePart = match[1]; // YYYYMMDD
    const timePart = match[2]; // HHMMSS

    const year = datePart.substring(0, 4);
    const month = datePart.substring(4, 6);
    const day = datePart.substring(6, 8);

    const hour = timePart.substring(0, 2);
    const minute = timePart.substring(2, 4);
    const second = timePart.substring(4, 6);

    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
  }

  createBackup(compress: boolean = false): void {
    this.isCreatingBackup = true;
    this.backupService.createBackup(compress).subscribe({
      next: (response: any) => {
        const blob = new Blob([response.body], {
          type: compress ? 'application/zip' : 'application/x-sqlite3',
        });
        const filename =
          response.headers
            .get('content-disposition')
            ?.split('filename=')[1]
            ?.replace(/"/g, '') ||
          `backup_${new Date().toISOString()}.${compress ? 'zip' : 'sqlite'}`;

        saveAs(blob, filename);
        this.showMessage(
          'Backup created and downloaded successfully!',
          'success'
        );
        this.loadBackups();
      },
      error: (error) => {
        this.showMessage('Error creating backup', 'error');
      },
      complete: () => {
        this.isCreatingBackup = false;
      },
    });
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      // Check if it's a SQLite or ZIP file
      if (
        file.name.endsWith('.sqlite') ||
        file.name.endsWith('.db') ||
        file.name.endsWith('.zip')
      ) {
        this.selectedFile = file;
        this.showMessage(`Selected file: ${file.name}`, 'info');
      } else {
        this.showMessage('Please select a .sqlite, .db, or .zip file', 'error');
        event.target.value = '';
      }
    }
  }

  restoreFromBackup(backup: any): void {
    if (
      !confirm(
        `Are you sure you want to restore from ${backup}? This will overwrite current data.`
      )
    ) {
      return;
    }

    this.backupService.restoreSpecificBackup(backup).subscribe({
      next: (response: any) => {
        this.successMessage = 'Database restored successfully!';
        this.loading = false;
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (error) => {
        this.errorMessage =
          error.error?.message || 'Failed to restore database';
        this.loading = false;
        setTimeout(() => (this.successMessage = ''), 3000);
      },
    });
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  formatDate(timestamp: number): string {
    return new Date(timestamp).toLocaleString();
  }

  private showMessage(text: string, type: 'success' | 'error' | 'info'): void {
    this.message = text;
    this.messageType = type;
    setTimeout(() => {
      this.message = '';
      this.messageType = '';
    }, 5000);
  }
}
