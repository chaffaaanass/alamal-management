import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SocietyRequest } from '../../models/society/society-request';
import { SocietyResponse } from '../../models/society/society-response';
import { AuthService } from '../auth/auth.service';
import { SocietySummary } from '../../models/society/society-summary';

@Injectable({
  providedIn: 'root',
})
export class SocietyService {
  private apiUrl: string = 'http://105.157.46.152:8080/api/societies';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllSocieties(): Observable<SocietyResponse[]> {
    return this.http.get<SocietyResponse[]>(this.apiUrl, {
      headers: this.getHeaders(),
    });
  }

  getSocietySummary(societyName: string): Observable<SocietySummary> {
    return this.http.get<SocietySummary>(
      `${this.apiUrl}/summary/${societyName}`,
      { headers: this.getHeaders() }
    );
  }

  getSocietiesBySocietyName(
    societyName: string
  ): Observable<SocietyResponse[]> {
    return this.http.get<SocietyResponse[]>(`${this.apiUrl}/${societyName}`, {
      headers: this.getHeaders(),
    });
  }

  getSocietiesByBatchType(batchType: string): Observable<SocietyResponse[]> {
    return this.http.get<SocietyResponse[]>(`${this.apiUrl}/${batchType}`, {
      headers: this.getHeaders(),
    });
  }

  getSocietyByChequeNumber(chequeNumber: number): Observable<SocietyResponse> {
    return this.http.get<SocietyResponse>(`${this.apiUrl}/${chequeNumber}`, {
      headers: this.getHeaders(),
    });
  }

  getSocietiesBySocietyNameAndDate(
    societyName: string,
    date: string
  ): Observable<SocietyResponse[]> {
    return this.http.get<SocietyResponse[]>(
      `${this.apiUrl}/${societyName}/${date}`,
      { headers: this.getHeaders() }
    );
  }

  getSocietyByDate(date: string): Observable<SocietyResponse> {
    return this.http.get<SocietyResponse>(`${this.apiUrl}/${date}`, {
      headers: this.getHeaders(),
    });
  }

  createSociety(request: SocietyRequest): Observable<SocietyResponse> {
    return this.http.post<SocietyResponse>(this.apiUrl, request, {
      headers: this.getHeaders(),
    });
  }

  updateSociety(
    societyId: number,
    request: SocietyRequest
  ): Observable<SocietyResponse> {
    return this.http.put<SocietyResponse>(
      `${this.apiUrl}/${societyId}`,
      request,
      {
        headers: this.getHeaders(),
      }
    );
  }

  deleteSociety(societyId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${societyId}`, {
      headers: this.getHeaders(),
    });
  }
}
