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
import com.fecklessweasel.service.objectmodel.Course;
import com.fecklessweasel.service.objectmodel.Department;
import com.fecklessweasel.service.objectmodel.University;
import com.fecklessweasel.service.objectmodel.FileMetadata;

/**
 * A servlet that will fill the Database with testing data when posted to
 * new users.
 * @author Hayden Schmackpfeffer
 */
@WebServlet("/servlet/populate")
public final class UserServlet extends HttpServlet {


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

                    String[] usernames = {"hschmack", "person1",
                                          "SteveJobs27", "Drake"};
                    String[] passwords = {"password1", "pass_word2", 
                                          "PassWord3", "P455W0RD4"};
                    String[] emails    = {"hayden@case.edu", "generic@gmail.com",
                                          "jobs@apple.com", "hotlinebling@yahoo.com"};

                    // Create the test users
                    for (int i = 0; i < usernames.length; i++){
                        User user = User.create(connection,
                                            usernames[i],
                                            passwords[i],
                                            emails[i]);

                    }


                    
                    // Have to return something.
                    return 0;
                }
            });

        // Redirect to profile page.
        response.sendRedirect("/");
    }
}
