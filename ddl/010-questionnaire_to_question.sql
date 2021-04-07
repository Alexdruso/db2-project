create table questionnaire_to_question
(
	questionnaire_id int not null,
	question_id int not null,
	primary key (questionnaire_id, question_id),
	constraint questionnaire_to_question_question_id_fk
		foreign key (question_id) references question (id)
			on update cascade on delete cascade,
	constraint questionnaire_to_question_questionnaire_id_fk
		foreign key (questionnaire_id) references questionnaire (id)
			on update cascade on delete cascade
);

