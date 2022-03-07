import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models';
import { SessionService } from 'src/app/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form!: FormGroup;
  fieldTextType: boolean = false;
  errLogin: boolean = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router, private sessionSvc: SessionService) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)])
    })
  }

  onSubmit(form: User) {
    this.http.post("/api/client/login", form).subscribe({
      next: (resp) => {
        console.log(resp);
        this.sessionSvc.setUsername(form.username)
        this.router.navigate(['/dashboard']);
      },
      error: (e) => {
        console.log(e)
        this.errLogin = true;
        setTimeout(() => {
          this.errLogin = false;
        }, 2000)
      }
    })
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }

}
