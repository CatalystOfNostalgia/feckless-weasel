/*
 * Feckless Weasel SQL Installer User Session Table Script
 * Orginally by: Christian Gunderman
 */

CREATE TABLE UserSession(
       uid INT NOT NULL,
       session_id VARCHAR(36) NOT NULL,
       PRIMARY KEY(uid),
       FOREIGN KEY (uid) REFERENCES User(uid) ON DELETE CASCADE);
