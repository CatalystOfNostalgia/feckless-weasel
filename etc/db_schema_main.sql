/*
 * Feckless Weasel SQL Installer Script Index
 * Orginally by: Christian Gunderman
 * NOTE: This script will WIPE everything and reinstall.
 *
 *
 * This file is the schema index and should only contain SOURCE
 * references to other scripts that perform creation of SQL
 * tables required by the service.
 */





/* #1: WIPES the old database and creates a new one and USE the DB.
 *  It must be first.
 */
SOURCE etc/db_schema_create_db.sql;

<<<<<<< HEAD
/* #2: Create User table */
SOURCE etc/db_schema_user_table.sql;

/* #3: Create User roles table */
SOURCE etc/db_schema_user_role_table.sql;

/* #4: Create User session table */
SOURCE etc/db_schema_user_session_table.sql;

/* #5: Create FileMetadata Table */
SOURCE etc/db_schema_filemetadata_table.sql;
