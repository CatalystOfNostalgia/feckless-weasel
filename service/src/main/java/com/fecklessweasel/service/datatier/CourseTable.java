package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Course to represent the Course table in the database.
 * @author Elliot Essman
 */
public class CourseTable {

    private static String INSERT_ROW = "insert into Course (univid, deptid, courseNumber) values (?,?,?)";
    private static String LOOKUP_WITH_DEPT = "SELECT * FROM Course WHERE Course.deptid=?";

    /**
     * Inserts a Course into the table.
     * @param conn A connection to the database.
     * @param univid ID of the university this Course is at.
     * @param deptid The ID f the department this Course is in.
     * @param Coursenum The number of this Course.
     * @return The id of the new Course.
     */
    public static int insertCourse(Connection conn, int univid, int deptid, int courseNumber) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, univid);
            preparedStatement.setInt(2, deptid);
            preparedStatement.setInt(3, courseNumber);
            preparedStatement.executeUpdate();

            // Get new id
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();
            int id = result.getInt(1);
            preparedStatement.close();
            return id;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
    /**
     *@author Hayden Schmackpfeffer
     *@param conn SQL Connection
     *@param deptid Department ID that all returned courses will have
     *@return all tuples in Course table where deptid= deptid
     */
    public static ResultSet getAllWithDept(Connection conn, int deptid) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");

        try {
            PreparedStatement lookupWithDept = conn.prepareStatement(LOOKUP_WITH_DEPT);
            lookupWithDept.setInt(1, deptid);

            return lookupWithDept.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
