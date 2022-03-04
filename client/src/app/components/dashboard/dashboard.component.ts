import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { AppService } from 'src/app/app.service';
import { Subscription } from 'src/app/models';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  itemButtonText: string = "Edit";
  username: string = "jian_jun3@hotmail.com";
  getSubscription!: Observable<any>;
  selectedSubscription!: Subscription;
  subscriptions: Subscription[] = [];
  isUpdated: boolean = false;
  emailSwitchClicked: boolean = false;
  tradeSwitchClicked: boolean = false;
  hasServerResponse: boolean = false;

  constructor(private http: HttpClient, private appSvc: AppService) { }

  ngOnInit(): void {
    this.getSubscription = this.http.get("http://localhost:8080/api/client/dashboard",
    { headers: new HttpHeaders({'username': this.username })})

    this.getSubscription.subscribe(resp => {
      this.subscriptions =  resp.subscriptions;
    })

    console.log("isEmailUpdated: " + this.isUpdated)
    console.log("emailSwitchedClicked: " + this.emailSwitchClicked)
    console.log("hasServerRespnose: " + this.hasServerResponse)
  }

  onEmailNotificationChange(value_rule_id: string, value_username: string, value_email_notification: boolean) {
    this.emailSwitchClicked = true;
    let payload = {
      rule_id: value_rule_id,
      username: value_username,
      email_notification: value_email_notification
    };

    this.http.post(
              "http://localhost:8080/api/client/subscription/email",
              payload)
              .subscribe({
                next: (resp) => {
                                  console.log(resp)
                                  this.isUpdated = true;
                                  this.hasServerResponse = true;
                                  setTimeout(() => {
                                    this.isUpdated = false;
                                    this.hasServerResponse = false;
                                    this.emailSwitchClicked = false;
                                  }, 1000)
                },
                error: (e) => {
                                  console.log(e)
                                  this.hasServerResponse = true;
                                  setTimeout(() => {
                                    this.hasServerResponse = false;
                                    this.emailSwitchClicked = false;
                                  }, 1000)
                }

              })
  }

  onAutoTradeChange(value_rule_id: string, value_username: string, value_auto_trade: boolean) {
    this.tradeSwitchClicked = true;
    let payload = {
      rule_id: value_rule_id,
      username: value_username,
      auto_trade: value_auto_trade
    };

    this.http.post(
              "http://localhost:8080/api/client/subscription/trade",
              payload)
              .subscribe({
                next: (resp) => {
                                  console.log(resp)
                                  this.isUpdated = true;
                                  this.hasServerResponse = true;
                                  setTimeout(() => {
                                    this.isUpdated = false;
                                    this.hasServerResponse = false;
                                    this.tradeSwitchClicked = false;
                                  }, 1000)
                },
                error: (e) => {
                                  console.log(e)
                                  this.hasServerResponse = true;
                                  setTimeout(() => {
                                    this.hasServerResponse = false;
                                    this.tradeSwitchClicked = false;
                                  }, 1000)
                }

              })
  }

}
