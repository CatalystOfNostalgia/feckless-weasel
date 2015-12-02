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

/**
 * Wrapper class for MySQL rating table.
 * @author Elliot Essman
 */
public abstract class RatingTable{
    
    private static String ADD_RATING = "insert into Rating (uid, fid, rating) values (?,?,?)";
            
    private static String UPDATE_RATING = "update Rating set rating=? where uid=? and fid=?";
    
    private static String GET_FILE_RATING = "SELECT AVG(rating) as avg FROM Rating WHERE fid=?";
    
    private static String GET_USER_FILE_RATING = "SELECT rating FROM Rating WHERE fid=? and uid=?";
    
    /**
     * Add a rating from a user to a given file.
     * @param conn A connection to the database.
     * @param uid The user ID who is giving the rating.
     * @param fid The file ID getting the rating.
     * @param rating The rating given.
     */
    public static void addRating(Connection conn, int uid, int fid, int rating) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_RATING);
            preparedStatement.setInt(1, rating);
            preparedStatement.setInt(2, uid);
            preparedStatement.setInt(3, fid);
            preparedStatement.setInt(4, uid);
            preparedStatement.setInt(5, fid);
            preparedStatement.setInt(6, rating);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLIntegrityConstraintViolationException ex){
            throw new ServiceException(ServiceStatus.APP_RATING_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Gets the average rating of a file accross all ratings.
     * @param conn A connection to the database.
     * @param fid The file id of the file to check.
     * @return A resultSet containing the average.
     */
    public static ResultSet getFileRating(Connection conn, int fid) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNull(fid, "fid");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_FILE_RATING);
            preparedStatement.setInt(1, fid);
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Returns the rating of the user as 1 or -1, 0 if not rated yet
     * @param conn A connection to the database.
     * @param fid The file id of the file to check.
     * @param uid The user id of the user to check.
     * @return The rating in resultset
     */
    public static int getUserFileRating(Connection conn, int uid, int fid) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_USER_FILE_RATING);
            preparedStatement.setInt(1, fid);
            preparedStatement.setInt(2, uid);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.isBeforeFirst()) {    
                return 0; 
            } 
            result.next();
            return result.getInt("rating");
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
