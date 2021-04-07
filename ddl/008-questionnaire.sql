-- Cyclic dependencies found

create table questionnaire
(
	id int auto_increment,
	date date not null,
	user_id int null,
	product_id int not null,
	constraint questionnaire_date_uindex
		unique (date),
	constraint questionnaire_id_uindex
		unique (id),
	constraint questionnaire_product_id_fk
		foreign key (product_id) references product (id)
			on update cascade on delete cascade,
	constraint questionnaire_user_id_fk
		foreign key (user_id) references user (id)
			on update cascade on delete cascade
);

alter table questionnaire
	add primary key (id);

