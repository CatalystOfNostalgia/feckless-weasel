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
 * The course servlet for creating courses.
 * @author Anjana Rao
 * @author Hayden Schmackpfeffer
 */
@WebServlet("/servlet/course")
public final class CourseServlet extends HttpServlet {

    /**
     * Handles post requests from forms that point to this servlet
     * @param request contains parameters course and dept
     * @param response directs to course/index.jsp
     * @throws ServletException When any handled error happens.
     * @throws IOException Server was unable to process the request.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        String methodType = request.getParameter("method");
        switch (methodType) {
            case "handleCourseCreate":
                handleCourseCreate(request, response);
                break;
            default:
                // do nothing
        }
    }

    /**
     * Handles post requests to create a new class.
     * @param request contains parameters course and dept
     * @param response directs to course/index.jsp
     * @throws ServletException When any handled error happens.
     * @throws IOException Server was unable to process the request.
     */
    protected void handleCourseCreate(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        int courseID = SQLSource.interact(new SQLInteractionInterface<Integer>() {
                @Override
                public Integer run(Connection connection)
                    throws ServiceException, SQLException {
                    String deptIDStr = request.getParameter("department");
                    String courseNumStr = request.getParameter("course");
                    
                    OMUtil.nullCheck(courseNumStr);
                    OMUtil.nullCheck(deptIDStr);

                    int deptID = OMUtil.parseInt(deptIDStr);
                    int courseNum = OMUtil.parseInt(courseNumStr);

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

    @Override
    protected void doGet(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        final String username = request.getParameter("username");
        final int courseID = OMUtil.parseInt(request.getParameter("cid"));

        Boolean b =
        SQLSource.interact(new SQLInteractionInterface<Boolean>() {
            @Override 
            public Boolean run(Connection connection)
                throws ServiceException {
  
                final User user = User.lookup(connection, username);
                return (Boolean) user.toggleFavoriteCourse(connection, courseID);
            }
        });

        response.sendRedirect("/course/index.jsp?cid=" + courseID);
    }
}
