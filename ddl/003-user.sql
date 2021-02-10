create table user
(
	id int not null,
	username varchar(255) not null,
	password varchar(255) not null,
	email varchar(255) not null,
	last_login datetime null,
	ban tinyint(1) default 0 not null,
	admin tinyint(1) null,
	constraint user_email_uindex
		unique (email),
	constraint user_id_uindex
		unique (id)
);

alter table user
	add primary key (id);

