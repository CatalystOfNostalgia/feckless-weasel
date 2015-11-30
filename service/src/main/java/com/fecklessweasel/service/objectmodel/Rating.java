package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.RatingTable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Stores all information about a Rating.
 * @author Elliot Essman
 */
public class Rating {
    
    /** ID of the user who made the rating.*/
    private int uid;
    
    /** ID of the file this rating is on.*/
    private int fid;
    
    /** The rating the user has given the file.*/
    private int rating;
    
    /**
     * Max rating value for a file.
     */
    private static int MAX_RATING = 5; 
    
    private Rating(int uid, int fid, int rating){
        this.uid = uid;
        this.fid = fid;
        this.rating = rating;
    }
    
    /**
     * Create a new rating in the database.
     * @param conn A connection to the database.
     * @param user The user giving the rating.
     * @param file The file the ratingis for.
     * @param rating The rating the file was given.
     */
    public static Rating Create(Connection conn, User user, StoredFile file, int rating) throws ServiceException{
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(user);
        OMUtil.nullCheck(file);
        if(rating > MAX_RATING || rating < 0){
            throw new ServiceException(ServiceStatus.APP_INVALID_RATING);
        }
        RatingTable.addRating(conn, user.getID(), file.getID(), rating);
        return new Rating(user.getID(), file.getID(), rating);
    }
    
    /**
     * Returns the rating of the given file.
     * @param conn A connection to the database.
     * @param fid The id of the file to use.
     * @return The rating of the file.
     */
    protected static double lookupFileRating(Connection conn, int fid) throws ServiceException{
        OMUtil.sqlCheck(conn);
        
        ResultSet results = RatingTable.getFileRating(conn, fid);
        try{
            results.next();
            double rating = results.getDouble("avg");
            results.close();
            return rating;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Get the user who made the rating.
     * @return The user who made the rating.
     */
    public User lookupUser(Connection sql) throws ServiceException {
        OMUtil.sqlCheck(sql);
        return User.lookupById(sql, this.uid);
    }
    
    /**
     * Get the file the rating is for.
     * @return The file the rating is for.
     */
    public StoredFile lookupFile(Connection sql) throws ServiceException {
        OMUtil.sqlCheck(sql);
        return StoredFile.lookup(sql, this.fid);
    }
    
    /**
     * Get the rating the file was given.
     * @return The rating the file was given.
     */
    public int getRating(){
        return this.rating;
    }
}