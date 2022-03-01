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
  validatepassword: boolean = false;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
      confirmpassword: this.fb.control('', [Validators.required, Validators.minLength(6)])
    })
  }

  onSubmit(form: any) {
    console.log("username: " + form.username)
    console.log("password: " + form.password)
    console.log("confirm password: " + form.confirmpassword)
  }

  validatePassword() {
    if (this.password == this.confirmpassword) {
      this.validatepassword = true;
    }
  }
}
