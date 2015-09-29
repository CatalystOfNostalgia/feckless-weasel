package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
/**
 * Class to represent the class table in the database
 * @author Elliot Essman
 */

public class ClassTable {
	
	private static String INSERT_ROW = "insert into Class (univid, deptid, classnumber) values (?,?,?)";
	
	/**
	 * Insert a class into the table
	 */
	public static void insert(Connection conn, int univid, int deptid, int classnumber) throws SQLException, ServiceException{
		try{
		PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
		preparedStatement.setInt(1, univid);
		preparedStatement.setInt(2, deptid);
		preparedStatement.setInt(3, classnumber);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		conn.close();
		} catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
	}
}

