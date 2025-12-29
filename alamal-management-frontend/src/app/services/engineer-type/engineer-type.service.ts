import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EngineerType } from '../../models/engineer/engineer-type';
import { AuthService } from '../auth/auth.service';
import { EngineerTypeRequest } from '../../models/engineer/engineer-type-request';

@Injectable({
  providedIn: 'root'
})
export class EngineerTypeService {
private apiUrl: string = 'http://localhost:8080/api/engineer-types';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllEngineerTypes(): Observable<EngineerType[]> {
    return this.http.get<EngineerType[]>(this.apiUrl, {
      headers: this.getHeaders(),
    });
  }


  getEngineerByType(engineerType: string): Observable<EngineerType> {
    return this.http.get<EngineerType>(
      `${this.apiUrl}/${engineerType}`,
      { headers: this.getHeaders() }
    );
  }

  createEngineerType(request: EngineerTypeRequest): Observable<EngineerType> {
    return this.http.post<EngineerType>(this.apiUrl, request, {
      headers: this.getHeaders(),
    });
  }

  updateEngineerType(engineerTypeId: number, request: EngineerTypeRequest): Observable<EngineerType> {
    return this.http.put<EngineerType>(`${this.apiUrl}/${engineerTypeId}`, request, {
      headers: this.getHeaders(),
    });
  }

  deleteEngineerType(engineerTypeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${engineerTypeId}`, {
      headers: this.getHeaders(),
    });
  }
}
