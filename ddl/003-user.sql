create table user
(
	id int auto_increment,
	username varchar(255) not null,
	password varchar(255) not null,
	email varchar(255) not null,
	lastlogin datetime null,
	ban tinyint(1) default 0 not null,
	admin tinyint(1) null,
	constraint user_email_uindex
		unique (email),
	constraint user_id_uindex
		unique (id),
	constraint user_username_uindex
		unique (username)
);

alter table user
	add primary key (id);

