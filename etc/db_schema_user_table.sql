/*
 * Feckless Weasel SQL Installer User Table Script
 * Orginally by: Christian Gunderman
 */

CREATE TABLE User(
       uid INT AUTO_INCREMENT,
       user VARCHAR(25) NOT NULL,
       pass VARCHAR(64) NOT NULL,
       join_date DATETIME NOT NULL,
       email VARCHAR(320) NOT NULL,
       PRIMARY KEY(uid),
       UNIQUE (user),
       INDEX USING HASH(user));
