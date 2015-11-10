/* Course table */
CREATE TABLE Course (
	id INT AUTO_INCREMENT,
	deptid INT,
	courseNumber INT,
	PRIMARY KEY(id),
	INDEX USING HASH(deptid, courseNumber),
	FOREIGN KEY (deptid) REFERENCES Department(id)
);
