create table product
(
	id int auto_increment,
	name varchar(255) not null,
	image blob null,
	constraint product_id_uindex
		unique (id)
);

alter table product
	add primary key (id);

