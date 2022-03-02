import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form!: FormGroup;
  fieldTextType: boolean = false;

  constructor(private fb: FormBuilder, private http: HttpClient) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)])
    })
  }

  onSubmit(form: any) {
    console.log(form)
    this.http.post("http://localhost:8080/login", form).subscribe(response => {
      console.log("response >>> " + JSON.stringify(response))
    })
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }

}
