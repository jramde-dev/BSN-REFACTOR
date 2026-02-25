import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  constructor(private kcService: KeycloakService) {
  }

  ngOnInit(): void {
    this.addActiveLink();
  }

  addActiveLink() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach((link) => {
      const value = link.getAttribute("href");
      if (window.location.href.endsWith(link.getAttribute("href") || "")) {
        link.classList.add("active");
      }

      link.addEventListener("click", () => {
        navLinks.forEach((l) => l.classList.remove("active"));
        link.classList.add("active");
      })
    })
  }

  async onLogout() {
    this.kcService.logout();
  }
}
