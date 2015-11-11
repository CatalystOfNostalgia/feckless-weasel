package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.CommentTable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
    
    private Comment(User user, FileMetadata file, Timestamp time){
        this.user = user;
        this.file = file;
        this.time = time;
    }
    
    /**
     * Create a new comment in the database.
     * @param conn A connection to the database.
     * @param user The user who created the comment.
     * @param file The file the comment is on.
     * @return The Comment object.
     */
    public static Comment Create(Connection conn, User user, FileMetadata file) throws ServiceException{
        Timestamp t = CommentTable.addComment(conn, user.getUid(), file.getFid());
        return new Comment(user, file, t);
    }
    
    /**
     * Create a comment with data from the database.
     * @param file The file the comment is on.
     * @param result The ResultSet from the database.
     * @return A comment object.
     */
    protected static Comment fromResultSet(FileMetaData file, ResultSet result){
        return new Comment(User.fromResultSet(result), file, result.get("datetime"));
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
}