package com.fecklessweasel.service;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.User;
import com.fecklessweasel.service.objectmodel.UserSession;

/**
 * A servlet that handles post requests from an HTML form and produces
 * new users and modifies their values.
 * @author Christian Gunderman
 */
@WebServlet("/servlet/user")
public final class UserServlet extends HttpServlet {

    /** Profile page URL. */
    public static String PROFILE_PATH = "/account";

    /**
     * Handles post requests to this servlet and address.
     * @param request The HTTP request containing the form data for
     * creating a user.
     * @param response The HTTP response containing the redirect to
     * a UI page.
     * @throws ServletException Thrown if the service encounters an
     * error.
     * @throws IOException Thrown if the service is unable to read
     * the request for some reason.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
        throws ServletException, IOException {

        // Get action form parameter or send bad request if not given.
        final String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Open a SQL connection and create the user and log them in.
        SQLSource.interact(new SQLInteractionInterface<Integer>() {

                /**
                 * Performs SQL Interactions.
                 * @param connection The SQL connection. Connection is released
                 * upon return.
                 * @throws ServiceException Exception is thrown up to calling
                 * function.
                 * @throws SQLException Exception is thrown if there is an
                 * unhandled SQLException.
                 * @return Returns 0 because generic interfaces have to return
                 * something.
                 */
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {

                    String username = request.getParameter("username");
                    String password = request.getParameter("password");

                    if (action.equals("create")) {
                        // Create a new user.
                        User user = User.create(connection,
                                                username,
                                                password,
                                                request.getParameter("email"));

                        // Log in new user.
                        UserSession session = UserSessionUtil.startSession(connection,
                                                                           response,
                                                                           username,
                                                                           password);
                    } else if (action.equals("update_password")) {
                        String newPassword = request.getParameter("new-password");

                        // Lookup user (we'll need to compare their passwords).
                        User user = User.lookup(connection,
                                                username);

                        // Update the user's password.
                        user.updatePassword(connection,
                                            password,
                                            newPassword);
                    } else if (action.equals("update_email")) {
                        String newEmail = request.getParameter("email");

                        // Lookup user (we'll need to compare their passwords).
                        User user = User.lookup(connection,
                                                username);

                        // Update the user's email.
                        user.updateEmail(connection,
                                         newEmail);
                    }

                    // Have to return something.
                    return 0;
                }
            });

        // Redirect to profile page.
        response.sendRedirect(PROFILE_PATH);
    }
}
