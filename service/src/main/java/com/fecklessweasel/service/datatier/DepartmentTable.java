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

    public static String LOOKUP_DEPARTMENT_QUERY =
            "SELECT * FROM Department WHERE Department.id=?";
    private static String INSERT_ROW = "insert into Department (univid, deptName, acronym) values (?,?,?)";

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
    /**Looks up the Department in the database based off of university ID and acronym**/
    public static ResultSet lookupDepartment(Connection connection, int id)
            throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNull(id, "");

        try {
            PreparedStatement lookupStatement =
                    connection.prepareStatement(LOOKUP_DEPARTMENT_QUERY);
            lookupStatement.setInt(1, id);

            return lookupStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
