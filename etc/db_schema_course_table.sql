/* Class table */
CREATE TABLE Course (
	id int AUTO_INCREMENT,
	univid int,
	deptid int,
	courseNumber int,
	PRIMARY KEY(id),
	INDEX USING HASH(univid, deptid, classnumber),
	FOREIGN KEY (univid) REFERENCES University(id),
	FOREIGN KEY (deptid) REFERENCES Department(id)
);