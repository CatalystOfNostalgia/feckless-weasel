package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLIntegrityConstraintViolationException;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Class for interacting with the two favorites tables
 * @author Hayden Schmackpfeffer
 */
public class FavoritesTable {

	public static String INSERT_FAV_FILE = 
		"INSERT INTO favoritefile (uid, fid) VALUES (?,?)";

	public static String INSERT_FAV_COURSE =
		"INSERT INTO favoritecourse (uid, cid) VALUE (?,?)";

	public static String SELECT_FILES = 
		"SELECT * FROM favoritefile WHERE uid=?";

	public static String SELECT_COURSES =
		"SELECT * FROM favoritecourse WHERE uid=?";

	public static String FILE_FAV_EXISTS = 
		"SELECT EXISTS(SELECT 1 FROM favoritefile WHERE uid=? AND fid=?)";

	public static String COURSE_FAV_EXISTS =
		"SELECT EXISTS(SELECT 1 FROM favoritecourse WHERE uid=? AND cid=?)";

	/**
	 * Insert a favorite course into the FavoriteCourse table
	 * @param conn MySQL connection
	 * @param uid The id of the user who favorited
	 * @param cid the id of the Course that was favorited
	 */
	public static void insertFavoriteCourse (Connection conn, int uid, int cid) 
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_FAV_COURSE);
            preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, cid);
            preparedStatement.executeUpdate();

            preparedStatement.close();
		} catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
	}

	/**
	 * Insert a favorite file into the FavoriteFile table
	 * @param conn MySQL connection
	 * @param uid The id of the user who favorited
	 * @param fid the id of the File that was favorited
	 */
	public static void insertFavoriteFile(Connection conn, int uid, int fid)
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_FAV_FILE);
			preparedStatement.setInt(1, uid);
            preparedStatement.setInt(2, fid);
            preparedStatement.executeUpdate();

			preparedStatement.close();
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}	
	}

	/**
	 * Get all of a users Favorite Courses
	 * @param conn MySQL connection
	 * @param uid The ID of the user who favorited the courses
	 * @return a ResultSet of all (uid, cid) with the passed in uid
	 */
	public static ResultSet getFavoriteCourses(Connection conn, int uid)
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SELECT_COURSES);
			preparedStatement.setInt(1, uid);

			return preparedStatement.executeQuery();
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}
	}

	/**
	 * Get all of a users Favorite Files
	 * @param conn MySQL connection
	 * @param uid The ID of the user who favorited the files
	 * @return a ResultSet of all (uid, fid) with the passed in uid
	 */
	public static ResultSet getFavoriteFiles(Connection conn, int uid)
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(SELECT_FILES);
			preparedStatement.setInt(1, uid);

			return preparedStatement.executeQuery();
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}
	}

	/**
	 * Returns True if the user has already favorited the course
	 * @param conn MySQL Connection
	 * @param uid User ID favoriting
	 * @param cid Course ID being favorited
	 * @return if row where (uid, cid) exists in table
	 */
	public static boolean favoriteCourseExists(Connection conn, int uid, int cid) 
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(COURSE_FAV_EXISTS);
			preparedStatement.setInt(1, uid);
			preparedStatement.setInt(2, cid);

			ResultSet result = preparedStatement.executeQuery();
			result.next();

			return (result.getInt(1) == 1 ? true : false);
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}
	}

		/**
	 * Returns True if the user has already favorited the file
	 * @param conn MySQL Connection
	 * @param uid User ID favoriting
	 * @param fid File ID being favorited
	 * @return if row where (uid, fid) exists in table
	 */
	public static boolean favoriteFileExists(Connection conn, int uid, int fid) 
		throws ServiceException {

		CodeContract.assertNotNull(conn, "conn");

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(FILE_FAV_EXISTS);
			preparedStatement.setInt(1, uid);
			preparedStatement.setInt(2, fid);

			ResultSet result = preparedStatement.executeQuery();
			result.next();

			return (result.getInt(1) == 1 ? true : false);
		} catch (SQLException ex) {
			throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
		}
	}
}