package com.fecklessweasel.service.objectmodel;

import java.io.*;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.FileMetadataTable;
import com.fecklessweasel.service.datatier.RatingTable;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;

/**
 * StoredFile API that will be used for all operations pertaining to files uploaded
 * to the service.
 * @author Hayden Schmackpfeffer
 * @author Christian Gunderman
 * @author James Flinn
 * @author Eric Luan
 */
public class StoredFile implements Comparable<StoredFile> {
    /** Minimum StoredFile title length. */
    public static final int MIN_TITLE = 1;
    /** Maximum StoredFile title length. */
    public static final int MAX_TITLE = 255;
    /** Minimum StoredFile description length. */
    public static final int MIN_DESCRIPTION = 1;
    /** Maximum StoredFile description length. */
    public static final int MAX_DESCRIPTION = 255;
    /** Minimum StoredFile extensions length. */
    public static final int MIN_EXTENSION = 1;
    /** Maximum StoredFile extensions length. */
    public static final int MAX_EXTENSION = 4;

    /**
     * Directory name where files are stored.
     * TODO: before deployment, make sure that this path is absolute or files
     * will be written to arbitrary directory based upon where the service is
     * started from.
     */
    private static final String FILEPATH_PREFIX = "files/";

    /** A file's unique identifier. */
    private int fid;
    /** The user id of the user who uploaded the file. */
    private int uid;
    /** The id of the course that the file pertains to */
    private int cid;
    /** The date the file was created */
    private Date creationDate;
    /** The file title. */
    private String title;
    /** The file description. */
    private String description;

    /** The file tag. */
    private String tag;
    /** The file's extension. */
    private String extension;
    /** A file's relevance score */
    private long relevanceScore; 
    
    /**
     * Private constructor to the StoredFile object. Is only called in create();
     * @param fid The Files Unique Identifier in the table
     * @param uid The ID of the user who uploaded the file
     * @param cid The course the file pertains to
     * @param creationDate The date the file was created
     * @param title The title for the file.
     * @param description The description for the file.
     * @param tag The tag associated with the file.
     * @param extension The file extension.
     */
    protected StoredFile(int fid, int uid, int cid, Date creationDate,
                       String title, String description, String tag, String extension) {
        this.fid = fid;
        this.uid = uid;
        this.cid = cid;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.extension = extension;
    }

    /**
     * Files are compared by their relevance score
     * @param otherFile The other file to compare to
     * @return 1 if this file is greater, 0 otherwise.
     */
    public int compareTo(StoredFile otherFile) {
        if (this.getRelevanceScore() > otherFile.getRelevanceScore()){
            return 1;
        } else if(this.getRelevanceScore() < otherFile.getRelevanceScore()){
            return -1;
        } else {
            return 0; 
        }
    }

    /**
     * Inserts the filemetadata as a new entry into the filemetadata table, returns the object
     * wrapper that represents the filemetadata.
     * @param user The uploading user.
     * @param course The course that the file is for.
     * @param title The title for the file.
     * @param description The description for the file.
     * @param fileData The input stream for the file data to write to the file.
     * @param tag The tag associated with the file.
     * @param extension The file's extension.
     */
    public static StoredFile create(Connection sql,
                                    User user,
                                    Course course,
                                    String title,
                                    String description,
                                    String tag,
                                    String extension,
                                    InputStream fileData) throws ServiceException {
        OMUtil.nullCheck(fileData);

        // Write metadata to the database.
        Date creationDate = new Date();
        int fid = addToDatabase(sql, user, course, title, description, creationDate, tag, extension);

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

        return new StoredFile(fid,
                              user.getID(),
                              course.getID(),
                              creationDate,
                              title,
                              description,
                              tag,
                              extension);
    }

    /**
     * Inserts the filemetadata as a new entry into the filemetadata table, returns the object
     * wrapper that represents the filemetadata.
     * @param user The uploading user.
     * @param course The course that the file is for.
     * @param title The title for the file.
     * @param description The description for the file.
     * @param markdownText The text for the markdown file.
     */
    public static StoredFile create(Connection sql,
                                    User user,
                                    Course course,
                                    String title,
                                    String description,
                                    String markdownText) throws ServiceException {
        OMUtil.nullCheck(markdownText);

        String extension = "md";

        // Write metadata to the database.
        Date creationDate = new Date();
        int fid = addToDatabase(sql, user, course, title, description, creationDate, "notes", extension);

        // Attempt to write the uploaded data to a file mapped to the ID.
        // TODO: check file extensions are not necessary.
        // TODO: potential concurrency issue here, FileMetadata can be written
        // before file is fully uploaded, leading to metadata records for files
        // that are not yet uploaded.
        String fileName = createFilename(fid);
        if (!saveFile(markdownText, fileName)) {
            // Upload failed, there is no file, delete the metadata.
            FileMetadataTable.deleteFile(sql, fid);
            throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
        }

        return new StoredFile(fid,
                user.getID(),
                course.getID(),
                creationDate,
                title,
                description,
                "notes",
                extension);
    }

    /**
     * Inserts the filemetadata as a new entry into the filemetadata table, returns the fid
     * @param user The uploading user.
     * @param course The course that the file is for.
     * @param title The title for the file.
     * @param description The description for the file.
     * @param creationDate The date this file was created.
     * @param tag The tag associated with the file.
     * @param extension The file's extension.
     */
    private static int addToDatabase(Connection sql,
                                      User user,
                                      Course course,
                                      String title,
                                      String description,
                                      Date creationDate,
                                      String tag,
                                      String extension) throws ServiceException {
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(user);
        OMUtil.nullCheck(course);
        OMUtil.nullCheck(title);
        OMUtil.nullCheck(description);
        OMUtil.nullCheck(extension);

        // Check title length.
        if (title.length() < MIN_TITLE || title.length() > MAX_TITLE) {
            throw new ServiceException(ServiceStatus.APP_INVALID_TITLE_LENGTH);
        }

        // Check description length.
        if (description.length() < MIN_DESCRIPTION || description.length() > MAX_DESCRIPTION) {
            throw new ServiceException(ServiceStatus.APP_INVALID_DESCRIPTION_LENGTH);
        }

        // Check extension length.
        if (extension.length() < MIN_EXTENSION || extension.length() > MAX_EXTENSION) {
            throw new ServiceException(ServiceStatus.APP_INVALID_EXTENSION_LENGTH);
        }

        // Write metadata to the database.
        return FileMetadataTable.insertFileData(sql,
                user.getID(),
                course.getID(),
                title,
                description,
                creationDate,
                tag,
                extension);
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
                                                 result.getDate("creation_date"),
                                                 result.getString("title"),
                                                 result.getString("description"),
                                                 result.getString("tag"),
                                                 result.getString("extension"));
            result.close();
            return fileData;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Updates the markdown file's text instead of creating a new file in the database.
     * @param fid The file id of the file being saved to.
     * @param markdownText The text of the file.
     */
    public static void updateMarkdownFile(int fid, String markdownText) throws ServiceException {
        if (!saveFile(markdownText, createFilename(fid))) {
            throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
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
     * Checks if the given user can edit the markdown file.
     * @param user The user attempting to edit the file.
     * @return true if the user can edit the file.
     */
    public boolean userCanEdit(User user) throws ServiceException {
        if (!this.getExtension().equals("md")) {
            throw new ServiceException(ServiceStatus.INVALID_FILE_TYPE);
        }

        return user.getID() == this.uid;
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
     * Looks up the course the file is associated with.
     * @param conn The connection to the MySQL database.
     * @return The course that this file belongs to.
     */
    public Course lookupCourse(Connection conn) throws ServiceException {
        OMUtil.sqlCheck(conn);

        return Course.lookupById(conn, this.cid);
    }

    /**
     * Get's the date the file was created
     * @return The files creation date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    /**
     * Get's the relevance score of the file
     * @return The file's relevance score
     */
    public long getRelevanceScore(){
        return this.relevanceScore;
    }

    /**
     * Set's the relevance scor of the file
     * @param score The score to set it to
     */
    public void setRelevanceScore(long score){
        this.relevanceScore = score;
    }

    /**
     * Gets the title for this StoredFile.
     * @return The file's title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the description for this StoredFile.
     * @return The file's description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the tag of this file
     * @return the files tag
     */
    public String getTag() {
        return this.tag;
    }
    
    /**
     * Gets the file extensions for this StoredFile.
     * @return The file's extension.
     */
    public String getExtension() {
        return this.extension;
    }

    /**
     * Returns this file's path.
     * @return The file's path.
     */
    public String getFilePath() {
        return FILEPATH_PREFIX + this.fid;
    }

    /**
     * Look up all files associated with a specific course ID.
     * @param sql The Sql connection to the FecklessWeaselDB
     * @param course The the course whose file info we want.
     * @return A List of StoredFile objects that represent each files info.
     */
    public static Iterable<StoredFile> lookupCourseFiles(Connection sql, Course course)
        throws ServiceException{
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(course);

        int cid = course.getID();

        ResultSet results = FileMetadataTable.lookUpCourseFiles(sql, cid);
        ArrayList<StoredFile> listOfFiles = new ArrayList<StoredFile>();

        try {
            while (results.next()) {
                StoredFile fileData = new StoredFile(results.getInt("fid"),
                                                     results.getInt("uid"),
                                                     results.getInt("cid"),
                                                     results.getDate("creation_date"),
                                                     results.getString("title"),
                                                     results.getString("description"),
                                                     results.getString("tag"),
                                                     results.getString("extension"));
                listOfFiles.add(fileData);
            }
            results.close();
            Date today = new Date();
            long todayInMillis = today.getTime();
            for(StoredFile file: listOfFiles){
                Date fileDate = file.getCreationDate();
                long fileDateInMillis = fileDate.getTime();
                long timeDifference = (todayInMillis - fileDateInMillis)/(1000*60*60*24); 
                long score = file.lookupRating(sql) - timeDifference;
                file.setRelevanceScore(score); 
            }
            
            Collections.sort(listOfFiles);
            Collections.reverse(listOfFiles);
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
    static Iterable<StoredFile> lookupUserFiles(Connection sql, int uid)
        throws ServiceException{
        OMUtil.sqlCheck(sql);

        ResultSet results = FileMetadataTable.lookUpUserFiles(sql, uid);
        ArrayList<StoredFile> listOfFiles = new ArrayList<StoredFile>();

        try {
            while(results.next()) {
                StoredFile fileData = new StoredFile(results.getInt("fid"),
                                                     results.getInt("uid"),
                                                     results.getInt("cid"),
                                                     results.getDate("creation_date"),
                                                     results.getString("title"),
                                                     results.getString("description"),
                                                     results.getString("tag"),
                                                     results.getString("extension"));
                listOfFiles.add(fileData);
            }

            results.close();
            return listOfFiles;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Returns comments on this file.
     *@param start The first comment to get.
     * @param count The amount of comments to get.
     * @return A list of Comment objects.
     */
    public List<Comment> lookupComments(Connection conn, int start, int count) throws ServiceException{
        return Comment.lookupFileComments(conn, this.fid, start, count);
    }
    
    /**
     * Returns the rating of this file.
     * @param conn A connection to the database.
     * @return The rating of this file.
     */
    public int lookupRating(Connection conn) throws ServiceException{
        return Rating.lookupFileRating(conn, this.fid);
    }
    
    public int getRatingByUser(Connection conn, int uid) throws ServiceException {
        return RatingTable.getUserFileRating(conn, uid, this.fid);
    }

    /**
     * Get all notes belonging to a certain user
     * @param sql Database connection
     * @param uid User ID
     * @return List of all StoredFile notes belonging to user uid
     */
    static Iterable<StoredFile> lookupUserNotes(Connection sql, int uid) 
        throws ServiceException {

        OMUtil.sqlCheck(sql);

        ResultSet results = FileMetadataTable.lookUpUserNotes(sql, uid);
        ArrayList<StoredFile> listOfNotes = new ArrayList<StoredFile>();

        try {
            while (results.next()) {
                StoredFile note = new StoredFile(results.getInt("fid"),
                                                 results.getInt("uid"),
                                                 results.getInt("cid"),
                                                 results.getDate("creation_date"),
                                                 results.getString("title"),
                                                 results.getString("description"),
                                                 results.getString("tag"),
                                                 results.getString("extension"));
                listOfNotes.add(note);
            }

            results.close();
            return listOfNotes;
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
     * Saves the markdown file to the server.
     * @param text The text of the markdown file.
     * @param fileName The file path the file will be saved to.
     * @return Returns true if file is successfully saved, false otherwise.
     */
    private static boolean saveFile(String text, String fileName) {
        try {
            File directory = new File(FILEPATH_PREFIX);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File markdownFile = new File(fileName);
            markdownFile.createNewFile();

            // TODO: ensure that file access is exclusive. Don't want to allow reads
            // Write the markdown text to the file
            PrintWriter writer = new PrintWriter(markdownFile);
            writer.print(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Gets the text of a markdown file stored on the server.
     * @param fid The markdown file's id.
     * @return The text contained in the file.
     */
    public static String getMarkdownText(final int fid) throws ServiceException {
        StoredFile file = SQLSource.interact(new SQLInteractionInterface<StoredFile>() {
            @Override
            public StoredFile run(Connection connection)
                    throws ServiceException, SQLException {

                return StoredFile.lookup(connection, fid);
            }
        });

        if (!file.getExtension().equals("md")) {
            throw new ServiceException(ServiceStatus.INVALID_FILE_TYPE);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(createFilename(fid)));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = reader.readLine();
            }
            reader.close();
            return sb.toString();
        } catch(Exception e) {
            // TODO: Change this to better ServiceStatus
            throw new ServiceException(ServiceStatus.UNKNOWN_ERROR);
        }
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
