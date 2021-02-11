create table questionnaire
(
	id int not null,
	date date not null,
	user_id int null,
	constraint questionnaire_date_uindex
		unique (date),
	constraint questionnaire_id_uindex
		unique (id),
	constraint questionnaire_user_id_fk
		foreign key (user_id) references user (id)
			on update cascade on delete set null
);

alter table questionnaire
	add primary key (id);
