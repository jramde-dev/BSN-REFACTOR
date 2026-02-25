import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from "../token/token.service";
import {KeycloakService} from "../../services/keycloak/keycloak.service";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(private kcService: KeycloakService) {
  }

  /**
   * Intercepter la reqête et injecter le token.
   * @param request
   * @param next
   */
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token: string | undefined = this.kcService.keycloak?.token;

    if (token) {
      const authReq = request.clone({
        headers: new HttpHeaders({Authorization: 'Bearer ' + token})
      })
      return next.handle(authReq);
    }

    return next.handle(request);
  }
}
