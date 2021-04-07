create table questionnaire_submission
(
	id int auto_increment,
	points int default 0 null,
	user_id int null,
	questionnaire_id int null,
	constraint questionnaire_submission_id_uindex
		unique (id),
	constraint questionnaire_submission_questionnaire_id_fk
		foreign key (questionnaire_id) references questionnaire (id)
			on update cascade on delete cascade,
	constraint questionnaire_submission_user_id_fk
		foreign key (user_id) references user (id)
			on update cascade on delete cascade
);

alter table questionnaire_submission
	add primary key (id);

