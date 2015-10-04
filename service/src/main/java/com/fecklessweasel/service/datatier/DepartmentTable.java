package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Class to represent the department table in the database
 * 
 * @author Elliot Essman
 */

public class DepartmentTable {

	private static String INSERT_ROW = "insert into Department (univid, deptname, acronym) values (?,?,?)";

	/**
	 * Insert a department into the table
	 * @param conn A connection to the database
	 * @param univid Id of the university this department is in
	 * @param deptname The official name of the department
	 * @param acronym The acronym of the department
	 * @return The id of the new department
	 */
	public static long insertDepartment(Connection conn, int univid, String deptname, String acronym)
			throws ServiceException {
		CodeContract.assertNotNull(conn, "conn");
		CodeContract.assertNotNullOrEmptyOrWhitespace(deptname, "deptname");
		CodeContract.assertNotNullOrEmptyOrWhitespace(acronym, "acronym");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
			preparedStatement.setInt(1, univid);
			preparedStatement.setString(2, deptname);
			preparedStatement.setString(3, acronym);
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
