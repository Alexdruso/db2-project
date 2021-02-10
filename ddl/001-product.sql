create table product
(
	id int not null,
	name varchar(255) not null,
	image blob null,
	constraint product_id_uindex
		unique (id)
);

alter table product
	add primary key (id);

