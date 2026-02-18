import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./auth-pages/login/login.component";
import {RegisterComponent} from "./auth-pages/register/register.component";
import {ActivateAccountComponent} from "./auth-pages/activate-account/activate-account.component";
import {authGuard} from "./core/guard/auth.guard";

const routes: Routes = [
  {path: 'login', title: 'Login', component: LoginComponent},
  {path: 'register', title: 'Register', component: RegisterComponent},
  {path: 'activate-account', title: 'Activate Account', component: ActivateAccountComponent},

  // Mise en place du lazy loading avec les modules.
  {
    path: "books",
    loadChildren: () => import("./modules/book/book.module").then(m => m.BookModule),
    canActivate: [authGuard]
  },
  {path: "", redirectTo: "login", pathMatch: "full"},
  {path: "**", redirectTo: "books", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
