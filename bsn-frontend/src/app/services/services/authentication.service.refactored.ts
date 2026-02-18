import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { RegistrationRequest } from '../models/registration-request';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private readonly baseUrl = '/api/auth';

  constructor(private http: HttpClient) {}

  // Authentication operations
  login(credentials: AuthenticationRequest): Observable<ApiResponse<AuthenticationResponse>> {
    return this.http.post<ApiResponse<AuthenticationResponse>>(`${this.baseUrl}/authenticate`, credentials);
  }

  register(userData: RegistrationRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.baseUrl}/register`, userData);
  }

  activateAccount(token: string): Observable<ApiResponse<void>> {
    return this.http.get<ApiResponse<void>>(`${this.baseUrl}/activate-account`, { 
      params: { token } 
    });
  }
}
