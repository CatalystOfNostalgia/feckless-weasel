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

/**
 * The course servlet for creating courses.
 * @author Anjana Rao
 */
@WebServlet("/servlet/course")
public final class CourseServlet extends HttpServlet {

    /**
     * Handles post requests to create a new class.
     * @param request contains parameters course and dept
     * @param response directs to course/index.jsp
     * @throws ServletException When any handled error happens.
     * @throws IOException Server was unable to process the request.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        int courseID = SQLSource.interact(new SQLInteractionInterface<Integer>() {
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {
                    String deptIDStr = request.getParameter("department");
                    String courseNumStr = request.getParameter("course");
                    
                    // Check for missing form values.
                    if (deptIDStr == null || courseNumStr == null) {
                        throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
                    }

                    int deptID;
                    int courseNum;

                    // Check to make sure form values are integers so we don't crash
                    // the server.
                    // "WHY OH WHY IS NUMBERFORMATEXCEPTION AN UNCHECKED EXCEPTION ORACLE??"
                    try {
                        deptID = Integer.parseInt(deptIDStr);
                        courseNum = Integer.parseInt(courseNumStr);
                    } catch (NumberFormatException ex) {
                        throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
                    }

                    // Lookup Dept. that will receive the new course.
                    Department department = Department.lookup(connection, deptID);

                    // Create course.
                    Course course = Course.create(connection, department, courseNum);

                    // Return course ID to caller.
                    return course.getID();
                }
            });

        // Redirect to homepage.
        response.sendRedirect("/course/index.jsp?cid=" + courseID);
    }
}
