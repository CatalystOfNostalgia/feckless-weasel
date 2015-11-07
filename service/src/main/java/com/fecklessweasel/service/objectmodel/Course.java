package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fecklessweasel.service.datatier.CourseTable;

/**
 * Stores all information about a Course at a university.
 * @author Elliot Essman
 */
public class Course {

    /** ID in the database table. */
    private int id;
    /** Department this Course is in. */
    private Department department;
    /** University this Course is in. */
    private University university;
    /** Course number. */
    private int courseNum;

    /** Max Course number. */
    private static int NUM_MAX = 999;
    /** Min Course number. */
    private static int NUM_MIN = 1;

    /**
     * Private constructor. Should be created by the database or create method.
     */
    private Course(int id, Department department, int courseNum) {
        this.id = id;
        this.university = department.getUniversity();
        this.department = department;
        this.courseNum = courseNum;
    }

    /**
     * Creates a Course in the database.
     * @param conn A connection to the database.
     * @param department The department this Course is in.
     * @param Coursenum The number of this Course.
     * @return A Course object.
     */
    public static Course create(Connection conn, Department department, int courseNum) throws ServiceException {
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(department);
        OMUtil.nullCheck(courseNum);
        University university = department.getUniversity();

        if (courseNum > NUM_MAX || courseNum < NUM_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_COURSE_NUMBER);
        }
        int id = CourseTable.insertCourse(conn, university.getID(), department.getID(), courseNum);
        return new Course(id, department, courseNum);
    }

    /**
     *@author Hayden Schmackpfeffer
     *@param conn SQL Connection
     *@param dept Department which all returned courses belong to
     *@return list of all courses in dept
     */
    public static ArrayList<Course> getAllWithDept(Connection conn, Department dept) throws ServiceException {
        OMUtil.sqlCheck(conn);
        OMUtil.nullCheck(dept);

        ResultSet results = CourseTable.getAllWithDept(conn, dept.getID());
        ArrayList<Course> courses = new ArrayList<Course>();

        try{
            while (results.next()) {
                Course course = new Course(results.getInt("id"),
                                    dept,
                                    results.getInt("courseNumber"));
                courses.add(course);
            }
            results.close();
            return courses;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    
    /**
     * Gets the database ID of the Course.
     * @return The database ID of the Course.
     */
    protected int getID() {
        return this.id;
    }

    /**
     * Gets the department of the Course.
     * @return The deparmtent of the Course.
     */
    public Department getDepartment() {
        return this.department;
    }

    /**
     * Gets the university of the Course.
     * @return The university of the Course.
     */
    public University getUniversity() {
        return this.university;
    }

    /**
     * Gets the number of the Course.
     * @return The number of the Course.
     */
    public int getCourseNum() {
        return this.courseNum;
    }
}
