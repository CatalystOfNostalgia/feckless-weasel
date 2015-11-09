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
 * Wrapper class for MySQL comment table.
 * @author Elliot Essman
 */
public abstract class CommentTable{
    
    private static String ADD_COMMENT = "DECLARE @newnum INT"+
                                        "SELECT @newnum = (SELECT COUNT(*) FROM Comment WHERE fid=?)" +
                                        "INSERT INTO Comment (uid, fid, num) VALUES (?,?, @newnum )"+
                                        "SELECT @newnum";
    
    public static int addComment(Connection conn, int uid, int fid) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNull(uid, "uid");
        CodeContract.assertNotNull(fid, "fid");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_COMMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, fid);
            preparedStatement.setInt(2, uid);
            preparedStatement.setInt(3, fid);
            preparedStatement.executeUpdate();

            // Get new id
            ResultSet result = preparedStatement.getGeneratedKeys();
            int num = result.getInt(1);
            preparedStatement.close();
            return num;
        } catch (SQLIntegrityConstraintViolationException ex){
            throw new ServiceException(ServiceStatus.APP_COMMENT_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
