import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BatchType } from '../../models/society/batch-type';
import { AuthService } from '../auth/auth.service';
import { BatchTypeRequest } from '../../models/society/batch-type-request';

@Injectable({
  providedIn: 'root'
})
export class BatchTypeService {
private apiUrl: string = 'http://alamal-management.duckdns.org/api/batch-types';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllBatchTypes(): Observable<BatchType[]> {
    return this.http.get<BatchType[]>(this.apiUrl, {
      headers: this.getHeaders(),
    });
  }


  getBatchTypeByType(batchType: string): Observable<BatchType> {
    return this.http.get<BatchType>(
      `${this.apiUrl}/${batchType}`,
      { headers: this.getHeaders() }
    );
  }

  createBatchType(request: BatchTypeRequest): Observable<BatchType> {
    return this.http.post<BatchType>(this.apiUrl, request, {
      headers: this.getHeaders(),
    });
  }

  updateBatchType(batchTypeId: number, request: BatchTypeRequest): Observable<BatchType> {
    return this.http.put<BatchType>(`${this.apiUrl}/${batchTypeId}`, request, {
      headers: this.getHeaders(),
    });
  }

  deleteBatchType(batchTypeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${batchTypeId}`, {
      headers: this.getHeaders(),
    });
  }
}

