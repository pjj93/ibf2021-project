import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  constructor() { }

  setUsername(username: string) {
    sessionStorage.setItem("username", username)
  }

  getUsername(): string | null {
    return sessionStorage.getItem("username")
  }

}
