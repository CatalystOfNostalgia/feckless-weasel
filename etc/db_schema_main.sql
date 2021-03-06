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





/* WIPES the old database and creates a new one and USE the DB.
 *  It must be first.
 */
SOURCE etc/db_schema_create_db.sql;

/* Create User table */
SOURCE etc/db_schema_user_table.sql;

/* Create User roles table */
SOURCE etc/db_schema_user_role_table.sql;

/* Create User session table */
SOURCE etc/db_schema_user_session_table.sql;

/* Create University table */
SOURCE etc/db_schema_university_table.sql;

/* Create Department table */
SOURCE etc/db_schema_department_table.sql;

/* Create Class table */
SOURCE etc/db_schema_course_table.sql;

/* Create FileMetadata Table */
SOURCE etc/db_schema_filemetadata_table.sql;

/* Create FavoriteFiles Table */
SOURCE etc/db_schema_favorite_file_table.sql;

/* Create FavoriteCourses Table */
SOURCE etc/db_schema_favorite_course_table.sql;

/* Create Comments Table*/
SOURCE etc/db_schema_comment_table.sql;

/* Create Ratings Table*/
SOURCE etc/db_schema_rating_table.sql;