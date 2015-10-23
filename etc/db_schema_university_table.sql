/* University table */
CREATE TABLE University (
	id int AUTO_INCREMENT,
	longName varchar(30),
	acronym varchar(5),
	city varchar(30),
	state varchar(2),
	country varchar(30),
	UNIQUE(longName),
	INDEX USING HASH(acronym),
	PRIMARY KEY(id)
);