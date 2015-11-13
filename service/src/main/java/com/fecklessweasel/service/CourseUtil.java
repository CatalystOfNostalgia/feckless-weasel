package com.fecklessweasel.service;

import java.io.IOException;
import java.lang.NumberFormatException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.Course;
import com.fecklessweasel.service.objectmodel.Department;

/**
 * Utility class for looking up courses from servlet request
 *
 * @author Anjana Rao
 */
public final class CourseUtil {

    private CourseUtil() {
    }

    /**
     * gets request and returns course
     *
     * @param request contains course id
     * @return course
     * @throws ServiceException
     */
    public static Course findCourse(HttpServletRequest request)
            throws ServiceException {

        String courseID = request.getParameter("cid");
        if (courseID == null) {
            throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
        }
        final int cid = Integer.parseInt(courseID);
        //try {
        //cid = Integer.parseInt(request.getParameter("did"));
        //} catch (NumberFormatException e) {
        //}

        // Open a SQL connection, find course in tables
        return SQLSource.interact(new SQLInteractionInterface<Course>() {
            /**
             * connects to databsae and looks up department in table
             *
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public Course run(Connection connection)
                    throws ServiceException, SQLException {
                return Course.lookupById(connection, cid);
            }
        });
    }
}
