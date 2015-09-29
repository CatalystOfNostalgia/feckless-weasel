package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
/**
 * Class to represent the department table in the database
 * @author Elliot Essman
 */

public class DepartmentTable {
	
	private static String INSERT_ROW = "insert into Department (univid, deptname) values (?,?)";
	
	/**
	 * Insert a department into the table
	 */
	public static void insert(Connection conn, int univid, String deptname) throws SQLException, ServiceException{
		try{
		PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
		preparedStatement.setInt(1, univid);
		preparedStatement.setString(2, deptname);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		conn.close();
		} catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
	}
}

