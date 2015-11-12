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
import com.fecklessweasel.service.*;

/**
 * Servlet that handles HTTP requests to create a department
 *
 * @author Anjana Rao
 */
@WebServlet("/servlet/department")
public final class DepartmentServlet extends HttpServlet {

    /**
     * Handles post requests
     *
     * @param request  the HTTP request that contains data for department
     * @param response the HTTP response that redirects to the department/indez page
     * @throws ServletException throws if service encounters an error
     * @throws IOException      throws if unable to read request
     */
    int ID;

    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        SQLSource.interact(new SQLInteractionInterface<Integer>() {
                               @Override
                               public Integer run(Connection connection)
                                       throws ServiceException, SQLException {

                                   int uniID = Integer.parseInt(request.getParameter("university"));
                                   University university = UniversityUtil.getUniversityID(uniID);
                                   String deptName = request.getParameter("deptName");
                                   String acronym = request.getParameter("acronym");
                                   if (university == null || deptName == null || acronym == null) {
                                       throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
                                   }
                                   // Create university
                                   Department department = Department.create(connection, university, deptName, acronym);

                                   //get dept ID from database
                                   ID = department.getID();

                                   // return int value
                                   return 0;
                               }
                           }
        );
        // Redirect to homepage.
        response.sendRedirect("/department/index.jsp?did=" + ID);
    }
}
