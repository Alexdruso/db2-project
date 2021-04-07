-- Cyclic dependencies found

create table answer
(
	id int auto_increment,
	text text not null,
	questionnaire_submission_id int null,
	question_id int null,
	constraint answer_id_uindex
		unique (id),
	constraint answer_question_id_fk
		foreign key (question_id) references question (id)
			on update cascade on delete cascade,
	constraint answer_questionnaire_submission_id_fk
		foreign key (questionnaire_submission_id) references questionnaire_submission (id)
			on update cascade on delete cascade
);

alter table answer
	add primary key (id);

