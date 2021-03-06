/*
 * Feckless Weasel SQL Installer FileMetadata Table Script
 * @author: Hayden Schmackpfeffer
 */

CREATE TABLE FileMetadata(
       fid INT AUTO_INCREMENT,
       uid INT NOT NULL,
       cid INT NOT NULL,
       title VARCHAR(255) NOT NULL,
       description VARCHAR(255) NOT NULL,
       tag VARCHAR(255) NOT NULL,
       extension VARCHAR(4) NOT NULL,
       creation_date DATETIME,
       INDEX USING HASH(fid, uid),
       PRIMARY KEY (fid),
       FOREIGN KEY (uid) REFERENCES User(uid),
       FOREIGN KEY (cid) REFERENCES Course(id));
