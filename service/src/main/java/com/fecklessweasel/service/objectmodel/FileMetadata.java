package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.FileMetadataTable;

/**
 *FileMetadata API that will be used for all operations pertaining to file information stored in
 * the FileMetadata table of the database.
 */
 public class FileMetadata{

   /** A files unique identifier */
   private int fid;
   /** The user who uploaded the file*/
   private String user;
   /** The course that the file pertains to */
   private String course;
   /** The university that the course belongs to */
   private String university;
   /** The date the file was created */
   private Date creationDate;
   /** The rating of the file */
   private int rating;
   private static final int INITIAL_RATING = 0;
   /**
    * Private constructor to the FileMetadata object, is only called in create();
    *@param fid The Files Unique Identifier in the table
    *@param user The user who uploaded the file
    *@param course The course the file pertains to
    *@param university The University that the course belongs to
    *@param creationDate The date the file was created
    *@param rating The Rating of the file
    */
   private FileMetadata(int fid, String user, String course, String university, Date creationDate, int rating){
     this.fid = fid;
     this.user = user;
     this.course = course;
     this.university = university;
     this.creationDate = creationDate;
     this.rating = rating;
   }
    /**
     * inserts the filemetadata as a new entry into the filemetadata table, returns the object
     * wrapper that represents the filemetadata.
     */
   public static FileMetadata create(Connection sql,
                        String user,
                        String course,
                        String university,
                        Date creationDate) throws ServiceException{
    OMUtil.sqlCheck(sql);
    OMUtil.nullCheck(user);
    OMUtil.nullCheck(course);
    OMUtil.nullCheck(university);
    OMUtil.nullCheck(creationDate);

    //TODO Add validation for data members

    int fid = FileMetadataTable.insertFileData(sql, user, course, university, creationDate, INITIAL_RATING);
    return new FileMetadata(fid, user, course, university, creationDate, INITIAL_RATING);
  }
  /**
   * Look up a specific file, and return an object wrapper around its metadata row
   *@param sql The Sql connection to the FecklessWeaselDB
   *@param fid The Files unique identifier
   *@return The FileMetadata object representation of the row found
   */
  public static FileMetadata lookup(Connection sql, int fid) throws ServiceException{
    OMUtil.sqlCheck(sql);
    ResultSet result = FileMetadataTable.lookUpFile(sql, fid);

    try{
        //if no tuples returned, throw error.
        if (!result.next()) { throw new ServiceException(ServiceStatus.APP_FILE_NOT_EXIST); }

        FileMetadata fileData = new FileMetadata(result.getInt("fid"),
                                                 result.getString("user"),
                                                 result.getString("course"),
                                                 result.getString("university"),
                                                 result.getDate("creation_date"),
                                                 result.getInt("rating"));
        result.close();
        return fileData;
    } catch (SQLException ex) {
      throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
    }
  }

  /**
   * Look up all files in a specified course
   *@param sql The Sql connection to the FecklessWeaselDB
   *@param course The course whose file info we want
   *@param university The University that has the course
   *@return A List of FileMetadata objects that represent each files info
   */
  public static List<FileMetadata> lookUpCourseFiles(Connection sql, String course, String university)
    throws ServiceException{
    OMUtil.sqlCheck(sql);
    OMUtil.nullCheck(course);
    OMUtil.nullCheck(university);

    ResultSet results = FileMetadataTable.lookUpCourseFiles(sql, course, university);
    ArrayList<FileMetadata> listOfFiles = new ArrayList<FileMetadata>();

    try{
      while( results.next() ){
        FileMetadata fileData = new FileMetadata(results.getInt("fid"),
                                                 results.getString("user"),
                                                 results.getString("course"),
                                                 results.getString("university"),
                                                 results.getDate("creation_date"),
                                                 results.getInt("rating"));
        listOfFiles.add(fileData);
      }
      results.close();
      return listOfFiles;
    } catch (SQLException ex){
      throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
    }
  }
  /**
   * Look up all files belonging to a specific user
   *@param sql The Sql connection to the FecklessWeaselDB
   *@param user The User whos file info we are interested in
   *@return A List of FileMetadata objects that represent each files info
   */
  public static List<FileMetadata> lookUpUserFiles(Connection sql, String user)
    throws ServiceException{
    OMUtil.sqlCheck(sql);
    OMUtil.nullCheck(user);

    ResultSet results = FileMetadataTable.lookUpUsersFiles(sql, user);
    ArrayList<FileMetadata> listOfFiles = new ArrayList<FileMetadata>();

    try{
      while( results.next() ){
        FileMetadata fileData = new FileMetadata(results.getInt("fid"),
                                                 results.getString("user"),
                                                 results.getString("course"),
                                                 results.getString("university"),
                                                 results.getDate("creation_date"),
                                                 results.getInt("rating"));
        listOfFiles.add(fileData);
      }
      results.close();
      return listOfFiles;
    } catch (SQLException ex){
      throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
    }
  }

/**
 *Increments the specifed file's rating in the Table
 *@param sql The SQL connection
 *@param fid The id of the file we wish to increment the rating of
 */
 public static void incrementRating(Connection sql, int fid) throws ServiceException {
   OMUtil.sqlCheck(sql);

   FileMetadataTable.incrementFileRating(sql, fid);
 }

 /**
  *decrements the specifed file's rating in the Table
  *@param sql The SQL connection
  *@param fid The id of the file we wish to decrement the rating of
  */
  public static void decrementRating(Connection sql, int fid) throws ServiceException {
    OMUtil.sqlCheck(sql);

    FileMetadataTable.decrementFileRating(sql, fid);
  }

  /**
   * Deletes a particular file's metadata from the Table
   *@param sql The SQL conenction to the database
   *@param fid The id of the fileinfo we want to delete
   */
  public static void delete(Connection sql, int fid) throws ServiceException{
    OMUtil.sqlCheck(sql);

    FileMetadataTable.deleteFile(sql, fid);
  }

  /**
   * Deletes this File metadata from the database
   *@param sql The SQL connection
   */
  public void delete(Connection sql) throws ServiceException{
    OMUtil.sqlCheck(sql);
    FileMetadataTable.deleteFile(sql, this.getFid());
  }

  /**
   *Increments the files rating
   *@param sql The SQL connection
   */
  public void incrementRating(Connection sql) throws ServiceException{
    OMUtil.sqlCheck(sql);
    this.rating = this.rating + 1;
    FileMetadataTable.incrementFileRating(sql, this.getFid());
  }

  /**
   *Decrement the files rating
   *@param sql The SQL connection
   */
  public void decrementRating(Connection sql) throws ServiceException{
    OMUtil.sqlCheck(sql);
    this.rating = this.rating - 1;
    FileMetadataTable.decrementFileRating(sql, this.getFid());
  }

  /**
   * Checks if two FileMetadata objects refer to the same file
   *@param o The Object to compare to.
   *@return true if they share the same fid
   */
  @Override
  public boolean equals(Object o) {
    return ((FileMetadata) o).getFid() == this.getFid();
  }

  /**
   *Get's the FileMetadata's fid
   *@return The file identifier
   */
  public int getFid() {
    return this.fid;
  }

  /**
   *Get's the user who uploaded the file
   *@return The username of the user who uploaded the file
   */
  public String getUser() {
    return this.user;
  }

  /**
   *Get's the course the file pertains to
   *@return The course that the file is in
   */
  public String getCourse() {
    return this.course;
  }

  /**
   *Get's the university that the file is from
   *@return The university of the file
   */
  public String getUniversity() {
    return this.university;
  }

  /**
   *Get's the date the file was created
   *@return The files creation date
   */
  public Date getCreationDate() {
    return this.creationDate;
  }

  /**
   *Get's the rating of the file
   *@return The file's rating
   */
  public int getRating() {
    return this.rating;
  }

 }
