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
 * @author Elliot Essman, Hayden Schmackpfeffer
 */
public class CourseTable {

    public final static String INSERT_ROW = "insert into Course (deptid, courseNumber, courseName) values (?,?,?)";

    public final static String LOOKUP_ROW = "SELECT * FROM Course WHERE id=?";

    public final static String SELECT_PAGINATED
        = "SELECT * FROM Course WHERE deptid=? ORDER BY courseNumber LIMIT ?,?";

    /**
     * Inserts a Course into the table.
     * @param conn A connection to the database.
     * @param deptid The ID f the department this Course is in.
     * @param Coursenum The number of this Course.
     * @return The id of the new Course.
     */
    public static int insertCourse(Connection conn, int deptid, int courseNumber, String courseName)
        throws ServiceException {

        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNullOrEmptyOrWhitespace(courseName, "courseName");

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, deptid);
            preparedStatement.setInt(2, courseNumber);
            preparedStatement.setString(3, courseName);
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

    /**
     * Lookup all Courses in a Department from offset to offset + amt
     * @param connection MySQL database connection.\
     * @param deptid Unique ID of Department the courses belong to
     * @param offset skip the first x amount of rows, where x = offset
     * @param amt the amount of rows to return
     * @throws ServiceException Thrown upon error
     * @return A result set containing amt number of rows starting after the first offset rows
     */
    public static ResultSet lookUpPaginated(Connection connection, int deptid, int offset, int amt)
            throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");

        try {
            PreparedStatement lookupStatement = connection.prepareStatement(SELECT_PAGINATED);
            lookupStatement.setInt(1, deptid);
            lookupStatement.setInt(2, offset);
            lookupStatement.setInt(3, amt);

            return lookupStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
