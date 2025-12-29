import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EngineerRequest } from '../../models/engineer/engineer-request';
import { EngineerResponse } from '../../models/engineer/engineer-response';
import { AuthService } from '../auth/auth.service';
import { EngineerSummary } from '../../models/engineer/engineer-summary';

@Injectable({
  providedIn: 'root',
})
export class EngineerService {
  private apiUrl: string = 'http://localhost:8080/api/engineers';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllEngineers(): Observable<EngineerResponse[]> {
    return this.http.get<EngineerResponse[]>(this.apiUrl, {
      headers: this.getHeaders(),
    });
  }

  getEngineerSummary(name: string): Observable<EngineerSummary> {
    return this.http.get<EngineerSummary>(
      `${this.apiUrl}/summary/${name}`,
      { headers: this.getHeaders() }
    );
  }  

  getEngineersByName(engineerName: string): Observable<EngineerResponse[]> {
    return this.http.get<EngineerResponse[]>(
      `${this.apiUrl}/by-name/${engineerName}`,
      { headers: this.getHeaders() }
    );
  }

  getEngineersByType(engineerType: string): Observable<EngineerResponse[]> {
    return this.http.get<EngineerResponse[]>(
      `${this.apiUrl}/by-type/${engineerType}`,
      { headers: this.getHeaders() }
    );
  }

  getEngineersByChequeDate(chequeDate: string): Observable<EngineerResponse[]> {
    return this.http.get<EngineerResponse[]>(
      `${this.apiUrl}/all/${chequeDate}`,
      { headers: this.getHeaders() }
    );
  }

  getEngineersByNameAndChequeDate(engineerName: string, chequeDate: string): Observable<EngineerResponse[]> {
    return this.http.get<EngineerResponse[]>(
      `${this.apiUrl}/${engineerName}/${chequeDate}`,
      { headers: this.getHeaders() }
    );
  }

  getEngineerByChequeNumber(chequeNumber: string): Observable<EngineerResponse> {
    return this.http.get<EngineerResponse>(
      `${this.apiUrl}/${chequeNumber}`,
      { headers: this.getHeaders() }
    );
  }

  createEngineer(request: EngineerRequest): Observable<EngineerResponse> {
    return this.http.post<EngineerResponse>(this.apiUrl, request, {
      headers: this.getHeaders(),
    });
  }

  updateEngineer(engineerId: number, request: EngineerRequest): Observable<EngineerResponse> {
    return this.http.put<EngineerResponse>(`${this.apiUrl}/${engineerId}`, request, {
      headers: this.getHeaders(),
    });
  }

  deleteEngineer(engineerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${engineerId}`, {
      headers: this.getHeaders(),
    });
  }
}
