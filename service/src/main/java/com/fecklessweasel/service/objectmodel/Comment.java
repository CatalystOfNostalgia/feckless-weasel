package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.CommentTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * Stores all information about a Comment.
 * @author Elliot Essman
 */
public class Comment {
    
    /**
     * The user who made the comment.
     */
    private int uid;
    
    /**
     * File The comment is on.
     */
    private int fid;
    
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
    
    private Comment(int uid, int fid, Timestamp time, String text){
        this.uid = uid;
        this.fid = fid;
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
    public static Comment Create(Connection conn, User user, StoredFile file, String text) throws ServiceException{
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(user);
        OMUtil.nullCheck(file);
        
        if(text.length() > MAX_TEXT_CHARS || text.length() < 1){
            throw new ServiceException(ServiceStatus.APP_INVALID_COMMENT_TEXT);
        }
        
        Timestamp time = CommentTable.addComment(conn, user.getID(), file.getID(), text);
        return new Comment(user.getID(), file.getID(), time, text);
    }
    
    /**
     * Returns comments on the given file.
     * @param start The first comment to get.
     * @param count The amount of comments to get.
     * @return A list of Comment objects.
     */
    protected static List<Comment> lookupFileComments(Connection conn, int fid, int start, int count) throws ServiceException{
        OMUtil.sqlCheck(conn);
        
        ResultSet results = CommentTable.getFileComments(conn, fid, start, count);
        ArrayList<Comment> Comments = new ArrayList<Comment>();
        
        try{
            while (results.next()) {
                User user = User.lookupById(conn, results.getInt("uid"));
                Comment c = new Comment(results.getInt("uid"),
                                                     results.getInt("fid"),
                                                     results.getTimestamp("time"),
                                                     results.getString("text"));
                Comments.add(c);
            }
            results.close();
            return Comments;
        } catch (SQLException ex){
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Returns the user who created the comment.
     * @return The user who created the comment.
     */
    public User lookupUser(Connection sql) throws ServiceException {
        OMUtil.sqlCheck(sql);
        return User.lookupById(sql, this.uid);
    }
    
    /**
     * Returns the file the comment is on.
     * @return The file the comment is on.
     */
    public StoredFile lookupFile(Connection sql) throws ServiceException {
        OMUtil.sqlCheck(sql);
        return StoredFile.lookup(sql, this.fid);
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