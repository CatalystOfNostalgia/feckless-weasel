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
 * FileMetadata API that will be used for all operations pertaining to file information stored in
 * the FileMetadata table of the database.
 * @author Hayden Schmackpfeffer
 */
public class FileMetadata{

    /** A files unique identifier */
    private int fid;
    /** The id of the user who uploaded the file*/
    private int user;
    /** The id of the course that the file pertains to */
    private int course;
    /** The date the file was created */
    private Date creationDate;

    /**
    * Private constructor to the FileMetadata object, is only called in create();
    * @param fid The Files Unique Identifier in the table
    * @param user The user who uploaded the file
    * @param course The course the file pertains to
    * @param university The University that the course belongs to
    * @param creationDate The date the file was created
    * @param rating The Rating of the file
    */
    private FileMetadata(int fid, int user, int course, Date creationDate){
     this.fid = fid;
     this.user = user;
     this.course = course;
     this.creationDate = creationDate;
    }
    /**
    * inserts the filemetadata as a new entry into the filemetadata table, returns the object
    * wrapper that represents the filemetadata.
    * @param user The Id of the uploading user
    * @param course The id of the course
    * @param creationDate The Date the file was created
    */
    public static FileMetadata create(Connection sql,
                                        int user,
                                        int course,
                                        Date creationDate) throws ServiceException {
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(creationDate);

        //TODO Add validation for data members

        int fid = FileMetadataTable.insertFileData(sql, user, course, creationDate);
        return new FileMetadata(fid, user, course, creationDate);
    }
    /**
    * Look up a specific file, and return an object wrapper around its metadata row
    * @param sql The Sql connection to the FecklessWeaselDB
    * @param fid The Files unique identifier
    * @return The FileMetadata object representation of the row found
    */
    public static FileMetadata lookup(Connection sql, int fid) throws ServiceException{
       OMUtil.sqlCheck(sql);
       ResultSet result = FileMetadataTable.lookUpFile(sql, fid);

       try{
           //if no tuples returned, throw error.
           if (!result.next()) {
               throw new ServiceException(ServiceStatus.APP_FILE_NOT_EXIST);
           }

           FileMetadata fileData = new FileMetadata(result.getInt("fid"),
                                            result.getInt("uid"),
                                            result.getInt("cid"),
                                            result.getDate("creation_date"));
            result.close();
            return fileData;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
    * Look up all files in a specified course
    * @param sql The Sql connection to the FecklessWeaselDB
    * @param course The course whose file info we want
    * @return A List of FileMetadata objects that represent each files info
    */
    public static List<FileMetadata> lookUpCourseFiles(Connection sql, int course)
    throws ServiceException{
       OMUtil.sqlCheck(sql);

       ResultSet results = FileMetadataTable.lookUpCourseFiles(sql, course);
       ArrayList<FileMetadata> listOfFiles = new ArrayList<FileMetadata>();

       try{
           while (results.next()) {
               FileMetadata fileData = new FileMetadata(results.getInt("fid"),
                                                results.getInt("uid"),
                                                results.getInt("cid"),
                                                results.getDate("creation_date"));
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
    * @param sql The Sql connection to the FecklessWeaselDB
    * @param user The User whos file info we are interested in
    * @return A List of FileMetadata objects that represent each files info
    */
    public static List<FileMetadata> lookUpUserFiles(Connection sql, int user)
    throws ServiceException{
        OMUtil.sqlCheck(sql);

        ResultSet results = FileMetadataTable.lookUpUserFiles(sql, user);
        ArrayList<FileMetadata> listOfFiles = new ArrayList<FileMetadata>();

        try{
            while( results.next() ){
                FileMetadata fileData = new FileMetadata(results.getInt("fid"),
                                                     results.getInt("uid"),
                                                     results.getInt("cid"),
                                                     results.getDate("creation_date"));
                listOfFiles.add(fileData);
            }
            results.close();
            return listOfFiles;
        } catch (SQLException ex){
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
    * Deletes a particular file's metadata from the Table
    * @param sql The SQL conenction to the database
    * @param fid The id of the fileinfo we want to delete
    */
    public static void delete(Connection sql, int fid) throws ServiceException{
       OMUtil.sqlCheck(sql);

       FileMetadataTable.deleteFile(sql, fid);
    }

    /**
    * Deletes this File metadata from the database
    * @param sql The SQL connection
    */
    public void delete(Connection sql) throws ServiceException{
        OMUtil.sqlCheck(sql);
        FileMetadataTable.deleteFile(sql, this.getFid());
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
    *@return The uid of the user who uploaded the file
    */
    public int getUser() {
        return this.user;
    }

    /**
    *Get's the course the file pertains to
    *@return The id of the course that the file is in
    */
    public int getCourse() {
        return this.course;
    }


    /**
    *Get's the date the file was created
    *@return The files creation date
    */
    public Date getCreationDate() {
        return this.creationDate;
    }

 }
