package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import java.util.Date;
import java.util.UUID;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

import javax.mail.internet.InternetAddress;

/**
 * Wrapper class for SQL UserSessionTable.
 * Manages authenticated User sessions, log in, and log out.
 *
 * Table is currently configured to allow only one log in per user,
 * but can be easily reconfigured to change that by removing
 * ON DUPLICATE UPDATE from insert query and by adding session_id
 * to the primary key.
 * @author Christian Gunderman
 */
public abstract class UserSessionTable {

    private static final String INSERT_SESSION_QUERY
        = "INSERT INTO UserSession (uid, session_id) VALUES (?, ?) " +
        "ON DUPLICATE KEY UPDATE session_id=VALUES(session_id)";

    private static final String DELETE_SESSION_QUERY
        = "DELETE FROM UserSession WHERE uid=? AND session_id=?";

    private static final String DELETE_ALL_SESSIONS_QUERY
        = "DELETE FROM UserSession WHERE uid=?";

    private static final String QUERY_SESSION_QUERY
        = "SELECT * FROM UserSession WHERE uid=? AND session_id=?";

    private static final String DELETE_ALL_SESSIONS_NAME_QUERY
        = "DELETE FROM UserSession WHERE " +
        "uid=(SELECT uid FROM User WHERE user=?)";

    /**
     * Inserts a Session into the table.
     * @param connection The SQLConnection.
     * @param uid The table AUTO_INCREMENT index of the user.
     * @param uuid The session id.
     */
    public static void insertSession(Connection connection,
                                     long uid,
                                     UUID uuid) throws ServiceException {
        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_SESSION_QUERY);
            insertStatement.setLong(1, uid);
            insertStatement.setString(2, uuid.toString());

            insertStatement.execute();
            insertStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes a session.
     * @param connection The SQLConnection
     * @param uid The user AUTO_INCREMENT index integer.
     * @param sessionId The unique session id.
     */
    public static void deleteSession(Connection connection,
                                     long uid,
                                     UUID sessionId) throws ServiceException {
        try {
            PreparedStatement deleteStatement
                = connection.prepareStatement(DELETE_SESSION_QUERY);
            deleteStatement.setLong(1, uid);
            deleteStatement.setString(2, sessionId.toString());

            deleteStatement.execute();
            deleteStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes all of a user's sessions.
     * @param connection The SQLConnection
     * @param uid The user AUTO_INCREMENT index integer.
     */
    public static void deleteAllSessions(Connection connection,
                                         long uid) throws ServiceException {
        try {
            PreparedStatement deleteStatement
                = connection.prepareStatement(DELETE_ALL_SESSIONS_QUERY);
            deleteStatement.setLong(1, uid);

            deleteStatement.execute();
            deleteStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes all of user's sessions.
     * @param connection The SQLConnection
     * @param uid The user AUTO_INCREMENT index integer.
     */
    public static void deleteAllSessions(Connection connection,
                                         String username) throws ServiceException {
        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(DELETE_ALL_SESSIONS_NAME_QUERY);
            insertStatement.setString(1, username);

            insertStatement.execute();
            insertStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Checks if a session exists.
     * @param connection The SQLConnection
     * @param uid The user AUTO_INCREMENT index integer.
     * @param sessionId The unique session id.
     * @return True if the session exists.
     */
    public static boolean sessionExists(Connection connection,
                                        long uid,
                                        UUID sessionId) throws ServiceException {
        try {
            PreparedStatement queryStatement
                = connection.prepareStatement(QUERY_SESSION_QUERY);
            queryStatement.setLong(1, uid);
            queryStatement.setString(2, sessionId.toString());

            ResultSet result = queryStatement.executeQuery();
            boolean sessionExists = result.next();

            queryStatement.close();

            return sessionExists;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
