import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private http: HttpClient) { }

  getSubscriptions(): Observable<any> {
    return this.http.get("http://localhost:8080/api/client/dashboard",
    { headers: new HttpHeaders({'username': 'jian_jun3@hotmail.com'})})
  }

}
