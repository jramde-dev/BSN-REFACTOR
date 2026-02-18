import {Component} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";
import {TokenService} from "../../core/token/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {
  }

  onLogin() {
    this.errorMessage = [];
    this.authService.authenticate({body: this.authRequest}).subscribe({
      next: (response) => {
        this.tokenService.token = response.token as string;
        this.router.navigate(['/books']);
      },
      error: (err) => {
        console.log(err)

        // businessValidationErrors and businessErrorMessage are same name
        // of validationErrors declared in the backend
        if (err.error.businessValidationErrors) {
          // If no field is filled, collect all the message errors.
          this.errorMessage = err.error.businessValidationErrors;
        } else {
          // If not a validation error, then catch the error message
          // error is Angular default error
          // businessErrorMessage is our backend error msg name
          this.errorMessage.push(err.error.businessErrorMessage);
        }
      }
    })
  }

  onRegister() {
    this.router.navigate(['/register']);
  }
}
