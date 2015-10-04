package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Class to represent the class table in the database
 * 
 * @author Elliot Essman
 */

public class ClassTable {

	private static String INSERT_ROW = "insert into Class (univid, deptid, classnumber) values (?,?,?)";

	/**
	 * Inserts a class into the table.
	 * @param conn A connection to the database
	 * @param univid Id of the university this class is at
	 * @param deptid The official name of the department
	 * @param classnum The number of this class
	 * @return The id of the new class
	 */
	public static long insertClass(Connection conn, int univid, int deptid, int classnumber) throws ServiceException,
			ServiceException {
		CodeContract.assertNotNull(conn, "conn");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
			preparedStatement.setInt(1, univid);
			preparedStatement.setInt(2, deptid);
			preparedStatement.setInt(3, classnumber);
			preparedStatement.executeUpdate();
			// get new id
			ResultSet result = preparedStatement.getGeneratedKeys();
			result.next();
			long id = result.getLong(1);
			preparedStatement.close();
			return id;
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}
	}
}
