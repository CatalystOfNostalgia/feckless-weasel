/*
 * Feckless Weasel SQL Installer Database creation script
 * Orginally by: Christian Gunderman
 * NOTE: This script will WIPE everything and create new database.
 */


DROP DATABASE IF EXISTS FecklessWeaselDB;
CREATE DATABASE FecklessWeaselDB;
USE FecklessWeaselDB;

/* University table */
CREATE TABLE University (
id int AUTO_INCREMENT,
longname varchar(30),
acronym varchar(5),
city varchar(30),
state varchar(2),
country varchar(30),
PRIMARY KEY(id)
);

/* Department table */
CREATE TABLE Department (
id int AUTO_INCREMENT,
univid int,
deptname varchar(20),
PRIMARY KEY(id),
FOREIGN KEY (univid) REFERENCES University(id)
);

/* Class table */
CREATE TABLE Class (
id int AUTO_INCREMENT,
univid int,
deptid int,
classnumber int,
PRIMARY KEY(id),
FOREIGN KEY (univid) REFERENCES University(id),
FOREIGN KEY (deptid) REFERENCES Department(id)
);