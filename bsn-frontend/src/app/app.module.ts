import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import { LoginComponent } from './auth-pages/login/login.component';
import {FormsModule} from "@angular/forms";
import { RegisterComponent } from './auth-pages/register/register.component';
import { ActivateAccountComponent } from './auth-pages/activate-account/activate-account.component';
import {CodeInputModule} from "angular-code-input";
import {HttpTokenInterceptor} from "./core/interceptor/http-token.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ActivateAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CodeInputModule
  ],
  providers: [
    HttpClient,
    // Make interceptor global
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
