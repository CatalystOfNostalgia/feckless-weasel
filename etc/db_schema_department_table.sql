/* Department table */
CREATE TABLE Department (
	id int AUTO_INCREMENT,
	univid int,
	deptName varchar(50),
	acronym varchar(4),
	INDEX USING HASH(univid, deptname),
	PRIMARY KEY(id),
	FOREIGN KEY (univid) REFERENCES University(id)
);