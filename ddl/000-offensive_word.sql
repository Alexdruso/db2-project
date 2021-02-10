create table offensive_word
(
	word varchar(255) not null,
	constraint offensive_word_word_uindex
		unique (word)
);

alter table offensive_word
	add primary key (word);

