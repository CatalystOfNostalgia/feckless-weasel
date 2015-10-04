/* University table */
CREATE TABLE University (
id int AUTO_INCREMENT,
longname varchar(30),
acronym varchar(5),
city varchar(30),
state varchar(2),
country varchar(30),
INDEX USING HASH(acronym),
PRIMARY KEY(id)
);