import {Injectable} from '@angular/core';
import Keycloak from "keycloak-js";
import {IKcUserProfile} from "./kc-user-profile";

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private _userProfile: IKcUserProfile | undefined;

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090', // Keycloak url
        realm: 'book-social-network',
        clientId: 'bsn'
      });
    }
    return this._keycloak;
  }

  get userProfile() {
    return this._userProfile;
  }

  async init() {
    console.log("Authenticating user...");
    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required',
    });

    if (authenticated) {
      this._userProfile = (await this.keycloak?.loadUserProfile()) as IKcUserProfile;
      this._userProfile.token = this.keycloak?.token;
    }
  }

  onLogin() {
    return this.keycloak?.login();
  }

  onLogout() {
    return this.keycloak?.logout({
      redirectUri: "http://localhost:4200"
    });
  }
}
