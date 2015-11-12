package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.CommentTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Stores all information about a Comment.
 * @author Elliot Essman
 */
public class Comment {
    
    /**
     * The user who made the comment.
     */
    private User user;
    
    /**
     * File The comment is on.
     */
    private FileMetadata file;
    
    /**
     * Time when the comment was made.
     */
    private Timestamp time;
    
    /**
     * The comment text.
     */
    private String text;
    
    /**
     * Max char length for comment text.
     */
    private static int MAX_TEXT_CHARS = 5000;
    
    private Comment(User user, FileMetadata file, Timestamp time, String text){
        this.user = user;
        this.file = file;
        this.time = time;
        this.text = text;
    }
    
    /**
     * Create a new comment in the database.
     * @param conn A connection to the database.
     * @param user The user who created the comment.
     * @param file The file the comment is on.
     * @param text The comment text.
     * @return The Comment object.
     */
    public static Comment Create(Connection conn, User user, FileMetadata file, String text) throws ServiceException{
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(user);
        OMUtil.nullCheck(file);
        
        if(text.length() > MAX_TEXT_CHARS || text.length() < 1){
            throw new ServiceException(ServiceStatus.APP_INVALID_COMMENT_TEXT);
        }
        
        Timestamp time = CommentTable.addComment(conn, user.getUid(), file.getFid(), text);
        return new Comment(user, file, time, text);
    }
    
    /**
     * Create a comment with data from the database.
     * @param file The file the comment is on.
     * @param result The ResultSet from the database.
     * @return A comment object.
     */
    protected static Comment fromResultSet(FileMetadata file, ResultSet result) throws ServiceException {
        try{
            return new Comment(User.fromResultSet(result), file, result.getTimestamp("datetime"), result.getString("text"));
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Returns the user who created the comment.
     * @return The user who created the comment.
     */
    public User getUser(){
        return this.user;
    }
    
    /**
     * Returns the file the comment is on.
     * @return The file the comment is on.
     */
    public FileMetadata getFile(){
        return this.file;
    }
    
    /**
     * Returns the time the comment was created.
     * @return The time the comment was created.
     */
    public Timestamp getTime(){
        return this.time;
    }
    
    /**
     * Returns the comment text.
     * @return The comment text.
     */
    public String getText(){
        return this.text;
    }
}