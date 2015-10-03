package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.util.UUID;

import com.fecklessweasel.service.datatier.UserSessionTable;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Public API for UserSessions. Sessions track login and logout for users
 * of the system.
 * @author Christian Gunderman
 */
public class UserSession {

    /** Pinata session HTTP header. Contains auth info. [user];[id] */
    public static final String HEADER = "Feckless-Weasel-Session";
    /** The unique session ID. */
    private final UUID sessionId;
    /** The User object of the session owner. */
    private final User sessionUser;

    /**
     * Starts a new session and makes a record of it in the database.
     * @throws ServiceException If database error occurs, password don't match,
     * user doesn't exist, etc.
     * @param connection The SQL connection.
     * @param username The username of the user starting the session.
     * @param password The password of the user startign the session.
     * @return A session object to encapsulate the open session info.
     */
    public static UserSession start(Connection connection,
                                    String username,
                                    String password) throws ServiceException {
        // Clean up inputs.
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(username);
        OMUtil.nullCheck(password);

        // Lookup user.
        // Throws if invalid username.
        User user = User.lookup(connection, username);

        // Check if passwords match.
        if (!OMUtil.sha256(password).equals(user.getPasswordHash())) {
            throw new ServiceException(ServiceStatus.APP_INVALID_PASSWORD);
        }

        // Generate session UUID.
        UUID sessionId = UUID.randomUUID();

        // Create new session.
        UserSessionTable.insertSession(connection, user.getUid(), sessionId);

        return new UserSession(sessionId, user);
    }

    /**
     * Resumes a currently open session. This should be done each call that
     * requires authentication. This is the preferred method for resume.
     * @param connection The SQL connection.
     * @param httpSessionHeader The HTTP auth header.
     * @return A session object to encapsulate the open session info.
     */
    public static UserSession resume(Connection connection,
                                     String httpSessionHeader)
        throws ServiceException {
        OMUtil.sqlCheck(connection);

        if (httpSessionHeader == null) {
            throw new ServiceException(ServiceStatus.ACCESS_DENIED);
        }

        String[] sessionParams = httpSessionHeader.split(";");

        if (sessionParams.length != 2) {
            throw new ServiceException(ServiceStatus.INVALID_SESSION_HEADER);
        }

        try {
            return resume(connection, sessionParams[0], sessionParams[1]);
        } catch (ServiceException ex) {

            // Rethrow with more relevant exceptions.
            if (ex.status == ServiceStatus.APP_USER_NOT_EXIST) {
                throw new ServiceException(ServiceStatus.INVALID_SESSION_HEADER);
            }

            throw ex;
        }

    }

    /**
     * Resumes a currently open session.
     * @param connection The SQLConnection.
     * @param username The username of the user.
     * @param sessionId A String serialized UUID.
     * @return A session object to encapsulate the open session info.
     */
    public static UserSession resume(Connection connection,
                                     String username,
                                     String sessionId) throws ServiceException {
        OMUtil.nullCheck(sessionId);

        try {
            return resume(connection, username, UUID.fromString(sessionId));
        } catch (IllegalArgumentException ex) {
            // Session ID is not a valid UUID.
            throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
        }
    }

    /**
     * Resumes a currently open session.
     * @param connection The SQLConnection.
     * @param username The username of the user.
     * @param sessionId The sessionId UUID.
     * @return A session object to encapsulate the open session info.
     */
    public static UserSession resume(Connection connection,
                                     String username,
                                     UUID sessionId) throws ServiceException {
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(username);
        OMUtil.nullCheck(sessionId);

        // Lookup user.
        // Throws if invalid username.
        User user = User.lookup(connection, username);

        // Check if a session exists for this user.
        if (!UserSessionTable.sessionExists(connection, user.getUid(), sessionId)) {
            throw new ServiceException(ServiceStatus.INVALID_SESSION);
        }

        return new UserSession(sessionId, user);
    }

    /**
     * Ends all currently open sessions for the given user.
     * @param connection The SQLConnection.
     * @param username The username of the user.
     */
    public static void endAll(Connection connection, String username) throws ServiceException {
        UserSessionTable.deleteAllSessions(connection, username);
    }

    /**
     * Ends all currently open sessions (including this one)
     * for this session's user.
     * @param connection The SQL Connection.
     * @param username The username of the user.
     */
    public void endAll(Connection connection) throws ServiceException {
        UserSessionTable.deleteAllSessions(connection, this.sessionUser.getUid());
    }

    /**
     * Ends this session.
     * @param connection The SQLConnection.
     */
    public void end(Connection connection) throws ServiceException {
        UserSessionTable.deleteSession(connection,
                                       this.sessionUser.getUid(),
                                       this.sessionId);
    }

    /**
     * Gets this session's unique ID.
     */
    public UUID getSessionId() {
        return this.sessionId;
    }

    /**
     * Get session owner.
     * @return The user that started this session.
     */
    public User getUser() {
        return this.sessionUser;
    }

    /**
     * Creates UserSession object. This constructor is private because
     * UserSession objects should only be created by start() and resume().
     * @param sessionId the unique session id.
     * @param sessionUser The User object of the session owner.
     */
    private UserSession(UUID sessionId, User sessionUser) {
        this.sessionId = sessionId;
        this.sessionUser = sessionUser;
    }
}
