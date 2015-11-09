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
 * Class to represent the department table in the database.
 * @author Elliot Essman
 */
public class DepartmentTable {

    private static String INSERT_ROW = "insert into Department (univid, deptName, acronym) values (?,?,?)";

    private static String LOOKUP_ALL_IN_UNIV = "SELECT * FROM Department WHERE Department.univid=?";

    /**
     * Insert a department into the table.
     * @param conn A connection to the database.
     * @param univid Id of the university this department is in.
     * @param deptName The official name of the department.
     * @param acronym The acronym of the department.
     * @return The id of the new department.
     */
    public static int insertDepartment(Connection conn, int univid, String deptName, String acronym)
            throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");
        CodeContract.assertNotNullOrEmptyOrWhitespace(deptName, "deptName");
        CodeContract.assertNotNullOrEmptyOrWhitespace(acronym, "acronym");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, univid);
            preparedStatement.setString(2, deptName);
            preparedStatement.setString(3, acronym);
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
     *@param conn The SQL Connection
     *@param universityID The ID of the university that owns all returned Departmetns
     *@return All tuples in the department table where univid = universityID
     */
    public static ResultSet lookupAllInUniversity(Connection conn, int universityID) throws ServiceException {
        CodeContract.assertNotNull(conn, "conn");

        try {
            PreparedStatement lookupStatement = conn.prepareStatement(LOOKUP_ALL_IN_UNIV);
            lookupStatement.setInt(1, universityID);

            return lookupStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
