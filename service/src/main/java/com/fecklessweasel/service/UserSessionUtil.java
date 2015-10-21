package com.fecklessweasel.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.UserSession;

/**
 * Utility class for creating and deleting UserSessions. Encapsulates cookie
 * operations required for log in and logout.
 * @author Christian Gunderman
 */
public final class UserSessionUtil {
    /** The client side cookie for storing user authentication token. */
    private static final String AUTH_COOKIE = "FecklessWeaselAuthToken";

    /**
     * Private constructor to prevent instantiation.
     */
    private UserSessionUtil() { }

    /**
     * Starts a new session and logs in the user.
     * @param response The HTTP response for the HTTP request. Method
     * automatically sets the authentication cookie. Caller must sendRedirect
     * to desired end UI page.
     * @param username The username of the user to log in.
     * @param password The password of the user to log in.
     * @throws ServiceException Thrown if the service encounters an error.
     */
    public static UserSession startSession(Connection connection,
                                           HttpServletResponse response,
                                           String username,
                                           String password) throws ServiceException {

        // Start the session.
        UserSession session = UserSession.start(connection,
                                                username,
                                                password);

        // Store session in a cookie.
        Cookie authCookie = new Cookie(AUTH_COOKIE,
                                       session.getSessionString());

        // Make this cookie available anywhere on site hostname.
        authCookie.setPath("/");

        // TODO: uncomment when SSL is enabled.
        // Prevent cookie from being sent over non secure protocol (no HTTP, only HTTPS).
        // authCookie.setSecure(true);        
        
        response.addCookie(authCookie);

        return session;
    }

    /**
     * Resumes a session from the authentication cookie stored in a request.
     * @param connection The connection to the database. Provided by SQLSource.
     * @param request The HTTP request containing the authentication cookie.
     * @throws ServiceException Thrown if the service encounters an exception.
     * INVALID_SESSION_HEADER, ACCESS_DENIED, or NOT_AUTHENTICATED ServerStatus
     * if unable to authenticate.
     * @return The resumed UserSession.
     */
    public static UserSession resumeSession(Connection connection,
                                            HttpServletRequest request)
        throws ServiceException {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }
        
        // Run through all cookies for our domain. This is ok as long as the session
        // cookie is our only cookie. If we start saving more than five or ten
        // we should use a different strategy for indexing and searching.
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AUTH_COOKIE)) {
                return UserSession.resumeFromSessionString(connection, cookie.getValue());
            }
        }

        // No auth cookie present.
        throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
    }

    /**
     * Resumes a session from the authentication cookie stored in a request.
     * Connection to SQL is obtained automatically.
     * @param request The HTTP request containing the authentication cookie.
     * @throws ServiceException Thrown if the service encounters an exception.
     * INVALID_SESSION_HEADER, ACCESS_DENIED, or NOT_AUTHENTICATED ServerStatus
     * if unable to authenticate.
     * @return The resumed UserSession.
     */
    public static UserSession resumeSession(final HttpServletRequest request)
        throws ServiceException {

        try {
            // Open a SQL connection and create the user and log them in.
            return SQLSource.interact(new SQLInteractionInterface<UserSession>() {
                    @Override
                    public UserSession run(Connection connection)
                        throws ServiceException, SQLException {

                        return resumeSession(connection,
                                             request);
                    }
                });
        } catch (ServiceException ex) {
            if (ex.status == ServiceStatus.NOT_AUTHENTICATED) {
                return null;
            }

            // Bubble up exception to user interface.
            throw ex;
        }
    }

    /**
     * Ends all currently active sessions for the currently logged in user.
     * @param request The HTTP request containing the auth cookie of the
     * current user.
     * @param response The HTTP response that will receive the auth cookie
     * deletion.
     * @throws ServiceException Thrown if the service encounters an error.
     */
    public static void endAllSessions(final HttpServletRequest request,
                                      final HttpServletResponse response)
        throws ServiceException {

        // Open a SQL connection and create the user and log them in.
        SQLSource.interact(new SQLInteractionInterface<Integer>() {
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {

                    UserSession session = resumeSession(connection,
                                                        request);

                    // Delete sessions in DB.
                    UserSession.endAll(connection, session.getUser().getUsername());

                    // Delete the session cookie by setting it's age to zero.
                    Cookie authCookie = new Cookie(AUTH_COOKIE,
                                                   session.getSessionString());
                    authCookie.setPath("/");
                    authCookie.setMaxAge(0);

                    // TODO: uncomment when SSL is enabled.
                    // Prevent cookie from being sent over non secure protocol (no HTTP, only HTTPS).
                    // authCookie.setSecure(true);

                    response.addCookie(authCookie);

                    // Have to return something.
                    return 0;
                }
            });
    }
}
