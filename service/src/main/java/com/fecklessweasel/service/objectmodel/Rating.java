package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import com.fecklessweasel.service.datatier.RatingTable;
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

/**
 * Stores all information about a Rating.
 * @author Elliot Essman
 */
public class Rating {
    
    /** The user who made the rating.*/
    private User user;
    
    /** The file this rating is on.*/
    private FileMetadata file;
    
    /** The rating the user has given the file.*/
    private int rating;
    
    private Rating(User user, FileMetadata file, int rating){
        this.user = user;
        this.file = file;
        this.rating = rating;
    }
    
    /**
     * Create a new rating in the database.
     * @param conn A connection to the database.
     * @param user The user giving the rating.
     * @param file The file the ratingis for.
     * @param rating The rating the file was given.
     */
    public static Rating Create(Connection conn, User user, FileMetadata file, int rating) throws ServiceException{
        RatingTable.addRating(conn, user.getUid(), file.getFid(), rating);
        return new Rating(user, file, rating);
    }
    
    /**
     * Get the user who made the rating.
     * @return The user who made the rating.
     */
    public User getUser(){
        return this.user;
    }
    
    /**
     * Get the file the rating is for.
     * @return The file the rating is for.
     */
    public FileMetadata getFile(){
        return this.file;
    }
    
    /**
     * Get the rating the file was given.
     * @return The rating the file was given.
     */
    public int getRating(){
        return this.rating;
    }
}