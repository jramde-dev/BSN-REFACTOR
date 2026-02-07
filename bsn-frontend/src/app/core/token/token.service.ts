import {Injectable} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token(): string {
    return localStorage.getItem('token') as string;
  }

  isTokenNotValid() {
    return !this.isTokenValid();
  }

  /**
   * Check if the token is valid.
   */
  private isTokenValid() {
    const token: string = this.token;

    if (!token) {
      return false;
    }

    // Decode the token
    const jwtHelperService = new JwtHelperService();
    const isTokenExpired = jwtHelperService.isTokenExpired(token);
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }
}
