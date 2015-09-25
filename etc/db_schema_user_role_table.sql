/*
 * Feckless Weasel SQL Installer Roles Table Script
 * Orginally by: Christian Gunderman
 */

CREATE TABLE UserRole(
       rid SMALLINT AUTO_INCREMENT,
       role VARCHAR(25) NOT NULL,
       description VARCHAR(255) NOT NULL,
       PRIMARY KEY(rid),
       UNIQUE (role),
       INDEX USING HASH(role));

/* Create default roles */
INSERT INTO UserRole(role, description)
VALUES ('ROLE_ADMIN', 'Administrator with unrestricted access.');

INSERT INTO UserRole(role, description)
VALUES ('ROLE_USER', 'Standard user account.');

/* User Has Role table */
CREATE TABLE UserHasRole(
       uid INT NOT NULL,
       rid SMALLINT NOT NULL,
       PRIMARY KEY(uid, rid),
       FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE,
       FOREIGN KEY (rid) REFERENCES UserRole(rid) ON DELETE CASCADE);
