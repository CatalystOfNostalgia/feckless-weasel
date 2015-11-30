/* Course table */
CREATE TABLE Course (
	id INT AUTO_INCREMENT,
	deptid INT,
	courseNumber INT,
    courseName varchar(50),
	PRIMARY KEY(id),
	INDEX USING HASH(deptid, courseNumber),
	FOREIGN KEY (deptid) REFERENCES Department(id)
);
