create table expense (
	id bigint not null auto_increment,
	`date` date not null,
	amount numeric(13, 2) not null,
	reason varchar(500),
	constraint pk_expense primary key(id)
);