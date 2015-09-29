package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
/**
 * Class to represent the school table in the database
 * @author Elliot Essman
 */

public class UniversityTable {
	
	private static String INSERT_ROW = "insert into University (longname, acronym, city, state, country) values (?,?,?,?,?)";
	
	/**
	 * Insert a university into the table
	 */
	public static void insert(Connection conn, String longname, String acronym, String city, String state, String country) throws SQLException, ServiceException{
		try{
		PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROW);
		preparedStatement.setString(1, longname);
		preparedStatement.setString(2, acronym);
		preparedStatement.setString(3, city);
		preparedStatement.setString(4, state);
		preparedStatement.setString(5, country);
		preparedStatement.executeUpdate();
		preparedStatement.close();
		conn.close();
		} catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
	}
}

