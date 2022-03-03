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
  editMode: boolean = false;
  username: string = "jian_jun3@hotmail.com";
  getSubscription!: Observable<any>;
  selectedSubscription!: Subscription;
  subscriptions: Subscription[] = [];
  constructor(private http: HttpClient, private appSvc: AppService) { }

  ngOnInit(): void {
    this.getSubscription = this.http.get("http://localhost:8080/api/client/dashboard",
    { headers: new HttpHeaders({'username': this.username })})

    this.getSubscription.subscribe(resp => {
      this.subscriptions =  resp.subscriptions;
      console.log(this.subscriptions)
      console.log(this.subscriptions.length)
    })

  }

  onItemButtonClick(subscription: Subscription) {
    this.editMode = true;
    this.selectedSubscription = subscription;
  }

  onItemButtonSave(email_notfication: string, auto_trade: string) {
    this.editMode = false;
    console.log(email_notfication)
    console.log(auto_trade)
  }

  onEmailNotificationChange(rule_id: string, username: string, email_notification: boolean) {
    console.log(rule_id)
    console.log(username)
    console.log(email_notification)
  }

  onAutoTradeChange(rule_id: string, username: string, email_notification: boolean) {
    console.log(rule_id)
    console.log(username)
    console.log(email_notification)
  }

}
