create table question
(
	id int auto_increment,
	text text not null,
	optional tinyint(1) default 0 not null,
	constraint question_id_uindex
		unique (id)
);

alter table question
	add primary key (id);

