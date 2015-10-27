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
 * Servlet for creation and deletion of UserSessions (log in and logout)
 * @author Christian Gunderman
 */
@WebServlet("/servlet/user_session")
public final class UserSessionServlet extends HttpServlet {

    /**
     * Handles post requests to this end point. Performs creation and
     * deletion of UserSessions based upon current session cookie and
     * HTML form data. Hidden field "action" determines whether we
     * "create" or "delete" a session.
     * @param request The incoming HTTP request and associated form
     * data.
     * @param response The outgoing HTTP response containing the
     * redirect to the appropriate UI page.
     * @throws ServletException Thrown if service encounters an error
     * while handling request.
     * @throws IOException Thrown if server is unable to read request.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
        throws ServletException, IOException {

        // Determine action.
        String action = request.getParameter("action");
        final boolean isDelete = action.equals("delete");

        // Open a SQL connection and create the user and log them in.
        SQLSource.interact(new SQLInteractionInterface<Integer>() {

                /**
                 * Performs interactions with the database and encapsulates
                 * a SQL Connection that is freed when the function returns.
                 * @param connection A SQL connection.
                 * @throws ServiceException Thrown by service upon error.
                 * Passed to calling function.
                 * @throws SQLException Thrown by service upon uncaught
                 * SQL error. Passed to calling function.
                 * @return Required by generic interface. Does nothing.
                 */
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {

                    if (!isDelete) {
                        
                        // "create" is default action. Creates a new session.
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");
                        UserSession session = UserSessionUtil.startSession(connection,
                                                                           response,
                                                                           username,
                                                                           password);
                    } else {
                        UserSessionUtil.endAllSessions(request, response);
                    }
                    
                    // Have to return something.
                    return 0;
                }
            });

        if (isDelete) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect(UserServlet.PROFILE_PATH);
        }
    }
}
