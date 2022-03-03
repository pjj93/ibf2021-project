drop database if exists app;

create database app;

use app;

create table user(
    username varchar(64),
    `password` varchar(20),
    `role` varchar(8) default "user",
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
    twitter(rule_id, `value`, `tag`, `description`) 
	values  ("1497546470748135424", "testdoge -is:retweet -is:reply (from:peajayjay OR from:testuser69420)", "testdoge", "Test tweet for doge"),
			("1497577128975470592", "world -is:retweet -is:reply (from:peajayjay OR from:testuser69420)", "world", "Hello World"),
            ("1497848388800290822", "posttry -is:retweet -is:reply from:testuser69420", "post from server", "Post from server");

create table subscription(
    username varchar(64) not null,
    rule_id varchar(20) not null,
    email_notification enum("yes", "no") default "yes" not null,
    auto_trade enum("yes", "no") default "no" not null,
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