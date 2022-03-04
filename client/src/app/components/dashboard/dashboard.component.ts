import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { AppService } from 'src/app/app.service';
import { Ftx, Subscription } from 'src/app/models';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  form!: FormGroup
  itemButtonText: string = "Edit";
  username: string = "jian_jun3@hotmail.com";
  getSubscription!: Observable<any>;
  getFtx!: Observable<any>;
  selectedSubscription!: Subscription;
  subscriptions: Subscription[] = [];
  ftx!: Ftx;
  hasFtx: boolean = false;
  isUpdated: boolean = false;
  emailSwitchClicked: boolean = false;
  tradeSwitchClicked: boolean = false;
  hasServerResponse: boolean = false;

  constructor(private http: HttpClient, private appSvc: AppService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.getSubscription = this.http.get("http://localhost:8080/api/client/dashboard",
    { headers: new HttpHeaders({'username': this.username })})

    this.getSubscription.subscribe(resp => {
      this.subscriptions =  resp.subscriptions;
    })

    this.getFtx = this.http.get("http://localhost:8080/api/client/ftx",
    { headers: new HttpHeaders({'username': this.username })})

    this.getFtx.subscribe({
        next: (resp) => {
                          console.log(resp);
                          this.hasFtx = true;
        },
        error: (e) => {
                        console.log(e)
        }
      })

    this.form = this.fb.group({
        api_key: this.fb.control({value: this.ftx?.api_key, disabled: true}, [Validators.minLength(40), Validators.maxLength(40)]),
        api_secret: this.fb.control({value: this.ftx?.api_secret, disabled: true}, [Validators.minLength(40), Validators.maxLength(40)])
      })

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

  onUpdate(formdata: Ftx) {

    if(this.form.controls['api_key'].disabled) {
      this.form.controls['api_key'].enable()
    } else {
      this.form.controls['api_key'].disable()
    }
    if(this.form.controls['api_secret'].disabled) {
      this.form.controls['api_secret'].enable()
    } else {
      this.form.controls['api_secret'].disable()
    }
    if((this.ftx?.api_key != formdata.api_key) || (this.ftx?.api_secret != formdata.api_secret)) {
      this.http
        .post("http://localhost:8080/api/client/ftx",
               formdata,
               { headers: new HttpHeaders({'username': this.username }) }
              )
        .subscribe({
              next: (resp) => {
                              console.log(resp)
                              this.ftx.api_key = formdata.api_key
                              this.ftx.api_secret = formdata.api_secret
                }
        })
    }
  }
}
