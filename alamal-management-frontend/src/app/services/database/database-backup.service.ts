import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class DatabaseBackupService {
  private apiUrl: string = 'http://105.157.46.152:8080/api/database';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  listBackups(): Observable<any> {
    return this.http.get(`${this.apiUrl}/backups`, {
      headers: this.getHeaders(),
    });
  }

  getBackupStatistics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/backup/statistics`, {
      headers: this.getHeaders(),
    });
  }

  getDatabaseInfo(): Observable<any> {
    return this.http.get(`${this.apiUrl}/info`, {
      headers: this.getHeaders(),
    });
  }

  createBackup(compress: boolean = false): Observable<any> {
    return this.http.post(`${this.apiUrl}/backup?compress=${compress}`, {}, {
      responseType: 'blob',
      observe: 'response', 
      headers: this.getHeaders(),
    });
  }

  restoreSpecificBackup(fileName: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/restore/${fileName}`, {}, {
      headers: this.getHeaders(),
    });
  } 
}
