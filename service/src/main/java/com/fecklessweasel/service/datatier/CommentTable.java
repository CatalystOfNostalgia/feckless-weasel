package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Wrapper class for MySQL comment table.
 * @author Elliot Essman
 */
public abstract class CommentTable{
    
    private static String ADD_COMMENT = "INSERT INTO Comment (uid, fid, datetime) VALUES (?,?,?)";
    
    private static String GET_FILE_COMMENTS = "SELECT * FROM Comment c, User u WHERE c.uid=u.uid AND c.fid=? ORDER BY datetime LIMIT ? OFFSET ?";
    
    /**
     * Add a comment to a file.
     * @param conn A connection to the database.
     * @param uid The user ID who wrote the comment.
     * @param fid The file ID the comment is for.
     * @return The timestamp given to the comment.
     */
    public static Timestamp addComment(Connection conn, int uid, int fid) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNull(uid, "uid");
        CodeContract.assertNotNull(fid, "fid");
        Timestamp time = Timestamp.from(java.time.Instant.now());
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_COMMENT);
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, fid);
            preparedStatement.setTimestamp(3, time);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return time;
        } catch (SQLIntegrityConstraintViolationException ex){
            throw new ServiceException(ServiceStatus.APP_COMMENT_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Get comments for a given file.
     * @param conn A connection to the database.
     * @param fid The file ID to get comments from.
     * @param first The first comment to get. Starts at 0.
     * @param amount The number of comments to get stating at first.
     * @return The resultSet of comments.
     */
    public static ResultSet getFileComments(Connection conn, int fid, first, amount){
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNull(fid, "fid");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(GET_FILE_COMMENTS);
            preparedStatement.setInt(1, fid);
            preparedStatement.setInt(2, amount);
            preparedStatement.setInt(3, first);
            return preparedStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
