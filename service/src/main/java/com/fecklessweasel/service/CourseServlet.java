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
import com.fecklessweasel.service.DepartmentUtil;

@WebServlet("/servlet/course")
public final class CourseServlet extends HttpServlet {

    int courseID;

    /**
     * Handles post requests to create a new class
     * @param request contains parameters course and dept
     * @param response directs to course/index.jsp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        SQLSource.interact(new SQLInteractionInterface<Integer>() {
                               /**
                                * Connection to database
                                * @param connection
                                * @return
                                * @throws ServiceException
                                * @throws SQLException
                                */
                               @Override
                               public Integer run(Connection connection)
                                       throws ServiceException, SQLException {
                                   String deptIDStr = request.getParameter("department");
                                   String courseNumStr = request.getParameter("course");
                                   //checks to null values
                                   if (deptIDStr == null || courseNumStr == null) {
                                       throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
                                   }

                                   //check to makesure this is an int
                                   int deptID = Integer.parseInt(deptIDStr);
                                   int courseNum = Integer.parseInt(courseNumStr);
                                   Department department = Department.lookup(connection, deptID);

                                   // Create course
                                   Course course = Course.create(connection, department,courseNum);
                                   courseID = course.getID();

                                   // return int value
                                   return 0;
                               }
                           }

        );

        // Redirect to homepage.
        response.sendRedirect("/course/index.jsp?cid=" + courseID);
    }
}