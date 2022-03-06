export interface Subscription {
  rule_id: string,
  username: string,
  tag: string,
  email_notification: boolean,
  auto_trade: boolean,
  description: string
}

export interface Ftx {
  api_key: string,
  api_secret: string
}

export interface User {
  username: string,
  password: string,
  role: string
}
