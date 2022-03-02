import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  form!: FormGroup;

  password: String = "";
  confirmpassword: String = "";

  constructor(private fb: FormBuilder, private http: HttpClient) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
      confirmpassword: this.fb.control('', [Validators.required, Validators.minLength(6)])
    }, { validator: this.checkPasswords })
  }

  onSubmit(form: any) {
    console.log(form)
    this.http.post("http://localhost:8080/signup", form).subscribe(response => {
      console.log("response >>> " + JSON.stringify(response))
    })
  }

  checkPasswords(group: FormGroup) {
    const pass = group.controls["password"].value;
    const confirmPass = group.controls["confirmpassword"].value;

    return pass === confirmPass ? null : { notSame: true };
  }



}
