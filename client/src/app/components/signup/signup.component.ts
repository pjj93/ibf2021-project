import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  form!: FormGroup;

  password: String = "";
  confirmpassword: String = "";

  isRegistered: boolean = false;
  hasServerResponse: boolean = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
      confirmpassword: this.fb.control('', [Validators.required, Validators.minLength(6)])
    }, { validator: this.checkPasswords })
  }

  onSubmit(form: any) {
    this.http.post("/api/client/signup", form).subscribe({
      next: (resp) => {
        console.log(resp)
        this.isRegistered = true;
        this.hasServerResponse = true;
        setTimeout(() => {
          this.isRegistered = false;
          this.hasServerResponse = false;
        }, 2000)
      },
      error: (e) => {
        console.log(e)
        this.hasServerResponse = true;
        setTimeout(() => {
          this.hasServerResponse = false;
        }, 2000)
      }
    })
  }

  checkPasswords(group: FormGroup) {
    const pass = group.controls["password"].value;
    const confirmPass = group.controls["confirmpassword"].value;

    return pass === confirmPass ? null : { notSame: true };
  }
}
