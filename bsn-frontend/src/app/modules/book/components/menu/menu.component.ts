import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {INotification} from "./notification.model";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  socketClient: any = null;
  private notifSubscription: any;
  unreadNotifCount: number = 0;
  notifications: Array<INotification> = [];

  constructor(private kcService: KeycloakService, private toastrService: ToastrService) {
  }

  ngOnInit(): void {
    this.addActiveLink();

    // Connect the Websocket to the backend
    if (this.kcService.keycloak.tokenParsed?.sub) {
      let ws = new SockJS('http://localhost:8080/api/v1/ws')
      this.socketClient = Stomp.over(ws);
      this.socketClient.connect({'Authorization:': 'Bearer ' + this.kcService.keycloak.token}, () => {
          this.notifSubscription = this.socketClient.subscribe(
            // Same url of the backend
            `/user/${this.kcService.keycloak.tokenParsed?.sub}/notifications`,
            (message: any) => {
              const notification: INotification = JSON.parse(message.body);
              if (notification) {
                this.notifications.unshift(notification);
                if (notification.status === 'BORROWED') {
                  this.toastrService.info(notification.message, notification.bookTitle);
                } else if (notification.status === 'RETURNED') {
                  this.toastrService.info(notification.message, notification.bookTitle);
                } else if (notification.status === 'RETURN_APPROVED') {
                  this.toastrService.info(notification.message, notification.bookTitle);
                }
                this.unreadNotifCount++;
              }
            }
          )
        }
      )
    }
  }

  addActiveLink() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach((link) => {
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

  get username() {
    // @ts-ignore
    return this.kcService.keycloak?.tokenParsed?.given_name;
  }
}
