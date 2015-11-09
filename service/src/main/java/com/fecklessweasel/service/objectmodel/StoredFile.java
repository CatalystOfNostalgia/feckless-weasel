package com.fecklessweasel.service.objectmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * StoredFile API that will be used for all operations pertaining to files uploaded
 * to the service.
 * @author Hayden Schmackpfeffer
 * @author Christian Gunderman
 */
public class StoredFile {
    /**
     * Directory name where files are stored.
     * TODO: before deployment, make sure that this path is absolute or files
     * will be written to arbitrary directory based upon where the service is
     * started from.
     */
    private static final String FILEPATH_PREFIX = "files";
    
    /** A file's unique identifier. */
    private int fid;
    /** The user id of the user who uploaded the file. */
    private int uid;
    /** The id of the course that the file pertains to */
    private int course;
    /** The date the file was created */
    private Date creationDate;

    /**
     * Private constructor to the StoredFile object. Is only called in create();
     * @param fid The Files Unique Identifier in the table
     * @param user The ID of the user who uploaded the file
     * @param course The course the file pertains to
     * @param university The University that the course belongs to
     * @param creationDate The date the file was created
     * @param rating The Rating of the file
     */
    private StoredFile(int fid, int uid, int course, Date creationDate) {
        this.fid = fid;
        this.uid = uid;
        this.course = course;
        this.creationDate = creationDate;
    }

    /**
     * Inserts the filemetadata as a new entry into the filemetadata table, returns the object
     * wrapper that represents the filemetadata.
     * @param user The uploading user.
     * @param course The course that the file is for.
     */
    public static StoredFile create(Connection sql,
                                    User user,
                                    Course course,
                                    InputStream fileData) throws ServiceException {
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(user);
        OMUtil.nullCheck(course);

        // Write metadata to the database.
        Date creationDate = new Date();
        int fid = FileMetadataTable.insertFileData(sql,
                                                   user.getID(),
                                                   course.getID(),
                                                   creationDate);

        // Attempt to write the uploaded data to a file mapped to the ID.
        // TODO: check file extensions are not necessary.
        // TODO: potential concurrency issue here, FileMetadata can be written
        // before file is fully uploaded, leading to metadata records for files
        // that are not yet uploaded.
        String fileName = createFilename(fid);
        if (!saveFile(fileData, fileName)) {

            // Upload failed, there is no file, delete the metadata.
            FileMetadataTable.deleteFile(sql, fid);

            throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
        }

        return new StoredFile(fid, user.getID(), course.getID(), creationDate);
    }

    /**
     * Look up a specific file and return it in object form.
     * @param sql The Sql connection to the FecklessWeaselDB.
     * @param fid The file's unique identifier.
     * @return The StoredFile object representation of the row found.
     */
    public static StoredFile lookup(Connection sql, int fid) throws ServiceException{
        OMUtil.sqlCheck(sql);
        ResultSet result = FileMetadataTable.lookUpFile(sql, fid);

        try {
            // If no tuples returned, throw error.
            if (!result.next()) {
                throw new ServiceException(ServiceStatus.APP_FILE_NOT_EXIST);
            }

            StoredFile fileData = new StoredFile(result.getInt("fid"),
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
     * Looks up the user who uploaded the file
     * @return The user who uploaded the file
     */
    public User lookupUser(Connection sql) throws ServiceException {
        OMUtil.sqlCheck(sql);

        return User.lookupById(sql, this.uid);
    }

    /**
     * Deletes this File metadata from the database
     * @param sql The SQL connection
     */
    public void delete(Connection sql) throws ServiceException {
        StoredFile.delete(sql, this.getID());
    }

    /**
     * Checks if two StoredFile objects refer to the same file
     * @param o The Object to compare to.
     * @return true if they share the same fid
     */
    @Override
    public boolean equals(Object o) {
        return ((StoredFile) o).getID() == this.getID();
    }

    /**
     * Get's the StoredFile's fid
     * @return The file identifier
     */
    public int getID() {
        return this.fid;
    }

    /**
     * Get's the course the file pertains to
     * @return The id of the course that the file is in
     */
    public int getCourse() {
        return this.course;
    }

    /**
     * Get's the date the file was created
     * @return The files creation date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Look up all files associated with a specific course ID.
     * @param sql The Sql connection to the FecklessWeaselDB
     * @param cid The ID of the course whose file info we want.
     * @return A List of StoredFile objects that represent each files info.
     */
    static Iterable<StoredFile> lookUpCourseFiles(Connection sql, int cid)
        throws ServiceException{
        OMUtil.sqlCheck(sql);

        ResultSet results = FileMetadataTable.lookUpCourseFiles(sql, cid);
        ArrayList<StoredFile> listOfFiles = new ArrayList<StoredFile>();

        try{
            while (results.next()) {
                User user = User.lookupById(sql, results.getInt("uid"));
                StoredFile fileData = new StoredFile(results.getInt("fid"),
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
     * @param uid The User who's file info we want to retrieve.
     * @return A List of StoredFile objects that represent each files info
     */
    static Iterable<StoredFile> lookUpUserFiles(Connection sql, int uid)
        throws ServiceException{
        OMUtil.sqlCheck(sql);

        ResultSet results = FileMetadataTable.lookUpUserFiles(sql, uid);
        ArrayList<StoredFile> listOfFiles = new ArrayList<StoredFile>();

        try {
            while(results.next()) {
                StoredFile fileData = new StoredFile(results.getInt("fid"),
                                                     results.getInt("uid"),
                                                     results.getInt("cid"),
                                                     results.getDate("creation_date"));
                listOfFiles.add(fileData);
            }

            results.close();
            return listOfFiles;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes a particular file's metadata from the Table
     * @param sql The SQL conenction to the database
     * @param fid The id of the fileinfo we want to delete
     */
    static void delete(Connection sql, int fid) throws ServiceException {
        OMUtil.sqlCheck(sql);

        // Delete file from filesystem.
        // TODO: concurrency concerns. Maybe try waiting a few seconds for file
        // to be released by current reader?
        if (!new File(createFilename(fid)).delete()) {
            throw new ServiceException(ServiceStatus.SERVER_DELETE_ERROR);
        }

        FileMetadataTable.deleteFile(sql, fid);
    }
    
    /**
     * Saves a file to the server.
     * @param inputStream The file input stream.
     * @return Returns true if file is successfully saved, false otherwise.
     */
    private static boolean saveFile(InputStream inputStream, String fileName) {
        try {
            int read;
            byte[] bytes = new byte[1024];

            File directory = new File(FILEPATH_PREFIX);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // TODO: ensure that file access is exclusive. Don't want to allow reads
            // while we are writing or downloaded file will be corrupt or read will fail.
            OutputStream outputStream = new FileOutputStream(new File(fileName));

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {

            // Print error to logs.
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Creates a filename for storage on the server from the file ID.
     * @param fid The file ID.
     * @return The file path.
     */
    private static String createFilename(int fid) {
        return FILEPATH_PREFIX + fid;
    }
}
