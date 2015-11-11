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
    
    private static String ADD_RATING = "insert into Comment (uid, fid, rating) values (?,?,?)";
    
    private static String UPDATE_RATING = "update Comment set rating=? where uid=? and fid=?";
    
    /**
     * Add a rating from a user to a given file.
     * @param conn A connection to the database.
     * @param uid The user ID who is giving the rating.
     * @param fid The file ID getting the rating.
     * @param rating The rating given.
     */
    public static void addRating(Connection conn, int uid, int fid, int rating) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNull(uid, "uid");
        CodeContract.assertNotNull(fid, "fid");
        CodeContract.assertNotNull(rating, "rating");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_RATING);
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, fid);
            preparedStatement.setInt(3, rating);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLIntegrityConstraintViolationException ex){
            throw new ServiceException(ServiceStatus.APP_RATING_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
}
