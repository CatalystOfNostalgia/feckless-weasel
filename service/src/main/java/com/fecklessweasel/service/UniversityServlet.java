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
import com.fecklessweasel.service.objectmodel.*;

/**
 * Servlet that handles updates to universities.
 * @author Anjana Rao
 */
@WebServlet("/servlet/university")
public final class UniversityServlet extends HttpServlet {

    /**
     * Handles HTTP POST requests
     *
     * @param request  to create a university
     * @param response redirects to university/index.jsp
     * @throws ServletException When any type of error occurs.
     * @throws IOException If the servlet is unable to read the request.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        int univID = SQLSource.interact(new SQLInteractionInterface<Integer>() {
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {
                    String longName = request.getParameter("longName");
                    String acronym = request.getParameter("acronym");
                    String city = request.getParameter("city");
                    String state = request.getParameter("state");
                    String country = request.getParameter("country");

                    // Checks for missing values
                    if (longName == null ||
                        acronym == null ||
                        city == null ||
                        state == null ||
                        country == null) {
                        throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
                    }

                    // Create university
                    University university = University.create(connection, longName,
                                                              acronym, city, state, country);

                    // Get the ID of the new university.
                    return university.getID();
                }
            });

        // Redirect to homepage.
        response.sendRedirect("/university/index.jsp?uid=" + univID);
    }
}
