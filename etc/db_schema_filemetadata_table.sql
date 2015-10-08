/*
 * Feckless Weasel SQL Installer FileMetadata Table Script
<<<<<<< HEAD
 * @author: Hayden Schmackpfeffer
=======
>>>>>>> c3363d796706e0db1277a041ba624120361b3596
 */

CREATE TABLE FileMetadata(
	fid INT AUTO_INCREMENT,
	uid INT NOT NULL,
	cid INT NOT NULL,
	creation_date DATETIME,
    INDEX USING HASH(fid, uid),
	PRIMARY KEY (fid),
    FOREIGN KEY (uid) REFERENCES User(uid),
    FOREIGN KEY (cid) REFERENCES Department(id));