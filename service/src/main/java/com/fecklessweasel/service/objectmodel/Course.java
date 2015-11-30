package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.CourseTable;

/**
 * Stores all information about a Course at a university.
 * @author Elliot Essman
 * @author Christian Gunderman
 */
public final class Course {

    /** ID in the database table. */
    private int id;
    /** Department this Course is in. */
    private int did;
    /** Course number. */
    private int courseNum;

    /** Max Course number. */
    private static int NUM_MAX = 999;
    /** Min Course number. */
    private static int NUM_MIN = 1;

    /**
     * Private constructor. Should be created by the database or create method.
     */
    private Course(int id, int did, int courseNum) {
        this.id = id;
        this.did = did;
        this.courseNum = courseNum;
    }

    /**
     * Creates a Course in the database.
     * @param conn A connection to the database.
     * @param department The department this Course is in.
     * @param Coursenum The number of this Course.
     * @return A Course object.
     */
    public static Course create(Connection conn, Department department,
                                int courseNum) throws ServiceException {
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(department);
        OMUtil.nullCheck(courseNum);

        if (courseNum > NUM_MAX || courseNum < NUM_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COURSE_NUMBER);
        }

        int id = CourseTable.insertCourse(conn, department.getID(), courseNum);
        return new Course(id, department.getID(), courseNum);
    }

    /**
     * Looks up a course by it's unique ID.
     * @param conn The MySQL database connection.
     * @param cid The unique course ID.
     * @throws ServiceException Thrown if error occurs or course does not exist.
     * @return The Course object.
     */
    public static Course lookupById(Connection conn, int cid)
        throws ServiceException {

        ResultSet result = CourseTable.lookupCourse(conn, cid);

        try {
            if (!result.next()) {
                throw new ServiceException(ServiceStatus.APP_COURSE_NOT_EXIST);
            }

            Course course = new Course(result.getInt("id"),
                                       result.getInt("deptid"),
                                       result.getInt("courseNumber"));

            result.close();
            return course;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }                              
    }

    /**
     * Gets the department of the Course.
     * @return The deparmtent of the Course.
     */
    public Department lookupDepartment(Connection conn)
        throws ServiceException {
        OMUtil.sqlCheck(conn);

        return Department.lookup(conn, this.did);
    }

    /**
     * Gets the number of the Course.
     * @return The number of the Course.
     */
    public int getCourseNum() {
        return this.courseNum;
    }

    /**
     * Gets the database ID of the Course.
     * @return The database ID of the Course.
     */
    int getID() {
        return this.id;
    }
}
