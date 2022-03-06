drop database if exists app;

create database app;

use app;

create table user(
    username varchar(64),
    `password` varchar(20) not null,
    `role` varchar(16) not null default "user",
    primary key(username)
);

insert into user (username, `password`, `role`) values ("shadowysupercoder69420@gmail.com", "123456", "admin");

create table twitter(
    rule_id varchar(20),
    `value` varchar(256),
    tag varchar(20),
    `description` varchar(20),
    primary key(rule_id)
);

insert into 
twitter (rule_id, `value`, tag, `description`)
values ("1500344672710840323", "test tweet -is:retweet -is:reply (from:peajayjay OR from:testuser69420)", "test", "test tweet"),
		("1500360168625692676", "transfers available -is:retweet -is:reply (from:peajayjay OR from:testuser69420)", "test coinbase", "test coinbase listing"),
        ("1500351541449953280", "transfers available -is:retweet -is:reply from:CoinbaseAssets", "coinbase listing", "coinbase listing"),
        ("1500352439492300804", "doge -is:retweet -is:reply from:elonmusk", "elon doge", "elon tweets doge");

create table subscription(
    username varchar(64) not null,
    rule_id varchar(20) not null,
    email_notification BOOLEAN default true,
    auto_trade BOOLEAN default false,
    constraint fk_subscription_username
        foreign key(username)
        references user(username)
        on delete cascade
        on update restrict,
    constraint fk_rule_id
        foreign key(rule_id)
        references twitter(rule_id)
        on delete cascade
        on update restrict
);

create table ftx(
    api_key char(40) not null,
    api_secret char(40) not null,
    username varchar(64) not null,
    constraint fk_ftx_username
        foreign key(username)
        references user(username)
        on delete cascade
        on update restrict
);