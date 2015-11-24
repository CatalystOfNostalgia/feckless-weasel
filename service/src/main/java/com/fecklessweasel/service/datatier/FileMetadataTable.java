package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Date;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Wrapper class for MySQL FileMetadata tabke.
 * @author Hayden Schmackpfeffer
 */
public abstract class FileMetadataTable{

    public static final String INSERT_FILE_QUERY =
        "INSERT INTO FileMetadata (uid, cid, creation_date, title, description, tag)" +
        " VALUES (?,?,?,?,?,?)";

    public static final String LOOKUP_FILE_QUERY =
        "SELECT * FROM Filemetadata F WHERE F.fid=?";

    public static final String LOOKUP_FILES_FROM_USER_QUERY =
        "SELECT * FROM Filemetadata F WHERE F.uid=? ORDER BY F.title";

    public static final String LOOKUP_FILES_FROM_COURSE_QUERY =
        "SELECT * FROM Filemetadata F WHERE F.cid=? ORDER BY F.title";

    public static final String DELETE_FILE_QUERY =
        "DELETE FROM FileMetadata WHERE fid=?";

    /**
     * Inserts a file into the corresponding MySQL table. returns the generated fid
     * @param connection Connection to the database from SQLSource.
     * @param user An ID number that corresponds the user that uploaded the file
     * @param course The ID of the course
     * @param creationDate The user-specified creation date for the file.
     * @param title The file title.
     * @param description The file description.
     * @return fid Returns the fid of the inserted file. This is a files unique identifier
     */
    public static int insertFileData(Connection connection,
                                     int user,
                                     int course,
                                     String title,
                                     String description,
                                     Date creationDate,
                                     String tag)
        throws ServiceException {

        // Ensure parameters are clean.
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNull(creationDate, "creationDate");
        CodeContract.assertNotNullOrEmptyOrWhitespace(title, "title");
        CodeContract.assertNotNullOrEmptyOrWhitespace(description, "description");

        try {
            PreparedStatement insertStatement = connection.prepareStatement(
                INSERT_FILE_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, user);
            insertStatement.setInt(2, course);
            insertStatement.setDate(3, new java.sql.Date(creationDate.getTime()));
            insertStatement.setString(4, title);
            insertStatement.setString(5, description);
            insertStatement.setString(6, tag);

            insertStatement.execute();

            // Get autoincrement row id.
            ResultSet result = insertStatement.getGeneratedKeys();
            result.next();

            int fid = result.getInt(1);
            insertStatement.close();
            return fid;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up the file using its unique identifier
     * @param connection Connection to the mySQL database
     * @param fileId The files unique identifier
     * @return file A ResultSet containing the file tuple - only a single row.
     */
    public static ResultSet lookUpFile(Connection connection, int fileId)
        throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");

        try {

            PreparedStatement lookUpFileStatement = connection.prepareStatement(LOOKUP_FILE_QUERY);
            lookUpFileStatement.setInt(1, fileId);

            return lookUpFileStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up all files belonging to a certain user
     * @param connection Connection to the mySQL database
     * @param user: The username whose files we want to return
     * @return files A ResultSet containing all file tuples associated with the username
     */
    public static ResultSet lookUpUserFiles(Connection connection, int user)
        throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");

        try{
            PreparedStatement lookUpFileStatement
                = connection.prepareStatement(LOOKUP_FILES_FROM_USER_QUERY);
            lookUpFileStatement.setInt(1, user);

            return lookUpFileStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }

    }

    /**
     * Looks up all files belonging to a certain user
     * @param connection Connection to the mySQL database
     * @param course The course we want files from
     * @result files A ResultSet containing all file tuples associated with the username
     */
    public static ResultSet lookUpCourseFiles(Connection connection, int course)
        throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");

        try {
            PreparedStatement lookUpFileStatement = connection.prepareStatement(LOOKUP_FILES_FROM_COURSE_QUERY);
            lookUpFileStatement.setInt(1, course);

            return lookUpFileStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes the specified file's metadata from the table
     * @param connection Connection to the mySQL database
     * @param fid The file's unique identifier
     */
    public static void deleteFile(Connection connection, int fid)
        throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");

        try {
            PreparedStatement deleteStatement
                = connection.prepareStatement(DELETE_FILE_QUERY);
            deleteStatement.setInt(1, fid);

            if (deleteStatement.executeUpdate() != 1) {
                deleteStatement.close();
                throw new ServiceException(ServiceStatus.APP_FILE_NOT_EXIST);
            }
            deleteStatement.close();
        } catch (SQLException ex){
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
