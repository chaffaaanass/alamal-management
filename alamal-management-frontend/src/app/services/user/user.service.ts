import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../models/auth/user';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl: string = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, {
      headers: this.getHeaders(),
    });
  }

  getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${username}`, {
      headers: this.getHeaders(),
    });
  }

  createUser(request: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, request, {
      headers: this.getHeaders(),
    });
  }

  updateUser(
    username: string,
    request: User
  ): Observable<User> {
    return this.http.put<User>(
      `${this.apiUrl}/${username}`,
      request,
      {
        headers: this.getHeaders(),
      }
    );
  }

  deleteUser(username: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${username}`, {
      headers: this.getHeaders(),
    });
  }
}
