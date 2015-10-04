/* Department table */
CREATE TABLE Department (
id int AUTO_INCREMENT,
univid int,
deptname varchar(20),
INDEX USING HASH(univid, deptname),
PRIMARY KEY(id),
FOREIGN KEY (univid) REFERENCES University(id)
);