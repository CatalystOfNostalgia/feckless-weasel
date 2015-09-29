package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Class to represent the school table in the database
 * 
 * @author Elliot Essman
 */

public class UniversityTable {

	private static String INSERT_ROW = "insert into University (longname, acronym, city, state, country) values (?,?,?,?,?)";

	/**
	 * Insert a university into the table
	 * 
	 * @return The id of the new university
	 */
	public static long insertUniversity(Connection conn, String longname, String acronym, String city, String state,
			String country) throws ServiceException {
		CodeContract.assertNotNull(conn, "conn");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
			preparedStatement.setString(1, longname);
			preparedStatement.setString(2, acronym);
			preparedStatement.setString(3, city);
			preparedStatement.setString(4, state);
			preparedStatement.setString(5, country);
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
