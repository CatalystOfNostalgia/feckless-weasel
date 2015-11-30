package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLIntegrityConstraintViolationException;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Course to represent the Course table in the database.
 * @author Elliot Essman
 */
public class CourseTable {

    public final static String INSERT_ROW = "insert into Course (deptid, courseNumber) values (?,?)";

    public final static String LOOKUP_ROW = "SELECT * FROM Course WHERE id=?";

    /**
     * Inserts a Course into the table.
     * @param conn A connection to the database.
     * @param deptid The ID f the department this Course is in.
     * @param Coursenum The number of this Course.
     * @return The id of the new Course.
     */
    public static int insertCourse(Connection conn, int deptid, int courseNumber)
        throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, deptid);
            preparedStatement.setInt(2, courseNumber);
            preparedStatement.executeUpdate();

            // Get new id
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            preparedStatement.close();
            return id;
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new ServiceException(ServiceStatus.APP_COURSE_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up the course using its unique identifier.
     * @param connection Connection to the mySQL database.
     * @param cid The course's unique identifier.
     * @return A ResultSet containing the course tuple.
     */
    public static ResultSet lookupCourse(Connection connection, int cid)
        throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");

        try {
            PreparedStatement lookupCourseQuery =
                connection.prepareStatement(LOOKUP_ROW);
            lookupCourseQuery.setInt(1, cid);

            return lookupCourseQuery.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
