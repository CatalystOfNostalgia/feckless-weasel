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

public abstract class FileMetadataTable{

  public static final String INSERT_FILE_QUERY =
    "INSERT INTO FileMetadata (user, course, university, creation_date, rating)" +
    " VALUES (?,?,?,?,?)";

  public static final String LOOKUP_FILE_QUERY =
    "SELECT * FROM Filemetadata F WHERE F.fid=?";

  public static final String LOOKUP_FILES_FROM_USER_QUERY =
    "SELECT * FROM Filemetadata F WHERE F.user=?";

    public static final String LOOKUP_FILES_FROM_COURSE_QUERY =
      "SELECT * FROM Filemetadata F WHERE F.course=? AND F.university=?";

  public static final String DELETE_FILE_QUERY =
    "DELETE FROM FileMetadata WHERE fid=";

  /**
   * Inserts a file into the corresponding MySQL table. returns the generated fid
   *@param connection Connection to the database from SQLSource.
   *@param user The username
   *@param course The coursename
   *@param university The name of the university
   *@param creationDate The user-specified creation date for the file
   *@param rating The files rated score.
   *@return fid Returns the fid of the inserted file. This is a files unique identifier
   */
  public static long insertFileData(Connection connection,
                                     String user,
                                     String course
                                     String university
                                     Date creationDate,
                                     int rating)
    throws ServiceException {
        //ensure parameters are clean
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");
        CodeContract.assertNotNullOrEmptyOrWhitespace(course, "course");
        CodeContract.assertNotNullOrEmptyOrWhitespace(university, "university");
        CodeContract.assertNotNull(creationDate, "creationDate");

        try {
          PreparedStatement insertStatement = connection.prepareStatement(
            INSERT_FILE_QUERY, Statement.RETURN_GENERATED_KEYS
          );
          insertStatement.setString(1, user);
          insertStatement.setString(2, course);
          insertStatement.setString(3, university);
          insertStatement.setDate(4, creationDate);
          insertStatement.setInt(5, rating);

          insertStatement.execute();

          // Get autoincrement row id.
          ResultSet result = insertStatement.getGeneratedKeys();
          result.next();

          long uid = result.getLong(1);

          insertStatement.close();

          return uid;
        } catch (SQLException ex) {
          throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    /**
     * Looks up the file using its unique identifier
     *@param connection Connection to the mySQL database
     *@param fileId The files unique identifier
     *@return file A ResultSet containing the file tuple - only a single row.
     */
    public static ResultSet lookUpFile(Connection connection, int fileId)
      throws ServiceException{

        CodeContract.assertNotNull(connection);

        try{
          PreparedStatement lookUpFileStatement = connection.prepareStatement(LOOKUP_FILE_QUERY);
          lookUpFileStatement.setString(1, fileId);

          return lookUpFileStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    /**
     * Looks up all files belonging to a certain user
     *@param connection Connection to the mySQL database
     *@param user: The username whose files we want to return
     *@return files A ResultSet containing all file tuples associated with the username
     */
    public static ResultSet lookUpUsersFiles(Connection connection, String user)
      throws ServiceException{

        CodeContract.assertNotNull(connection);
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");

        try{
          PreparedStatement lookUpFileStatement = connection.prepareStatement(LOOKUP_FILES_FROM_USER_QUERY);
          lookUpFileStatement.setString(1, user);

          return lookUpFileStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }

    }

    /**
     * Looks up all files belonging to a certain user
     *@param connection Connection to the mySQL database
     *@param course The course we want files from
     *@param university The university that the course belongs to
     *@result files A ResultSet containing all file tuples associated with the username
     */
    public static ResultSet lookUpCourseFiles(Connection connection, String course, String university)
      throws ServiceException{

              CodeContract.assertNotNull(connection);
              CodeContract.assertNotNullOrEmptyOrWhitespace(course, "course");
              CodeContract.assertNotNullOrEmptyOrWhitespace(university, "university");

              try{
                PreparedStatement lookUpFileStatement = connection.prepareStatement(LOOKUP_FILES_FROM_USER_QUERY);
                lookUpFileStatement.setString(1, course);
                lookUpFileStatement.setString(1, university);

                return lookUpFileStatement.executeQuery();
              } catch (SQLException ex) {
                  throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
              }
    }

    public static void deleteFile(Connection connection, int fid) throws ServiceException{

      try {
        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_FILE_QUERY);
        deleteStatement.setInt(1, fid);

        if (deleteStatement.executeUpdate() != -1) {
          deleteStatement.close();
          throw new ServiceException(ServiceStatus.APP_FILE_NOT_EXIST);
        }

      } catch (ServiceException ex){

      }
    }
}
