import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {TokenService} from "../token/token.service";
import {KeycloakService} from "../../services/keycloak/keycloak.service";

export const authGuard: CanActivateFn = () => {
  const kcService = inject(KeycloakService);
  const router = inject(Router);

  if (kcService.keycloak?.isTokenExpired()) {
    router.navigate(['login']);
    return false;
  }
  return true;
};
