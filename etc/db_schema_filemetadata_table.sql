/*
 * Feckless Weasel SQL Installer FileMetadata Table Script
 */

CREATE TABLE FileMetadata(
	fid INT AUTO_INCREMENT,
	user varchar(25) NOT NULL,
	course varchar(50) NOT NULL,
	university varchar(50) NOT NULL,
	creation_date DATETIME,
	PRIMARY KEY (fid),
	INDEX USING HASH(user));
