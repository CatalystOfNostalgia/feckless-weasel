package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Date;
import javax.mail.internet.InternetAddress;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Wrapper class for MySQL User table. Performs queries and basic checks.
 * Exhaustive validation is performed by the object model.
 * @author Christian Gunderman
 */
public abstract class UserTable {
    /** Create user query. */
    public static final String INSERT_USER_QUERY =
        "INSERT INTO User (user, pass, join_date, email)" +
        " VALUES (?,?,?,?)";

    /** Lookup user query. */
    public static final String LOOKUP_USER_QUERY =
        "SELECT * FROM User U, UserRole R, UserHasRole H WHERE U.user=? " +
        " AND U.uid=H.uid AND H.rid=R.rid";

    /** Delete user query. */
    public static final String DELETE_USER_QUERY =
        "DELETE FROM User WHERE user=?";

    /**
     * Inserts a user into the MySQL table with some minimal validation.
     * This method should NOT be called directly since most validation
     * occurs in the object model.
     * @param connection Connection to the database from SQLSource.
     * @param user The username.
     * @param pass The password.
     * @param joinDate The date that the user joined.
     * @param email The user's email.
     */
    public static long insertUser(Connection connection,
                                  String user,
                                  String pass,
                                  Date joinDate,
                                  InternetAddress email)
        throws ServiceException {

        // Check basic checks for clean arguments.
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");
        CodeContract.assertNotNullOrEmptyOrWhitespace(pass, "pass");
        CodeContract.assertNotNull(joinDate, "joinDate");
        CodeContract.assertNotNull(email, "email");

        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_USER_QUERY,
                                              Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, user);
            insertStatement.setString(2, pass);
            insertStatement.setDate(3, new java.sql.Date(joinDate.getTime()));
            insertStatement.setString(4, email.getAddress());

            insertStatement.execute();

            // Get autoincrement row id.
            ResultSet result = insertStatement.getGeneratedKeys();
            result.next();

            long uid = result.getLong(1);

            insertStatement.close();

            return uid;
        } catch (SQLIntegrityConstraintViolationException ex) {
            // We have no foreign or unique keys other than primary
            // so this can only be thrown for duplicate users.
            throw new ServiceException(ServiceStatus.APP_USERNAME_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up a user in the database and returns the associated ResultSet with
     * with additional rows for each security UserRole the user has.
     * @throws ServiceException If a SQL error occurs.
     * @param connection The database connection.
     * @param user The user to look up.
     * @return user A ResultSet containing the user. Should contain only a
     * single row.
     */
    public static ResultSet lookupUserWithRoles(Connection connection, String user)
        throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");

        try {
            PreparedStatement lookupStatement =
                connection.prepareStatement(LOOKUP_USER_QUERY);
            lookupStatement.setString(1, user);

            return lookupStatement.executeQuery();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes a user in the database.
     * @throws ServiceException If a SQL error occurs or unable to delete.
     * @param connection The database connection.
     * @param user The username of the user to look up.
     */
    public static void deleteUser(Connection connection, String user)
        throws ServiceException {

        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");

        try {
            PreparedStatement deleteStatement =
                connection.prepareStatement(DELETE_USER_QUERY);
            deleteStatement.setString(1, user);

            // Execute and check that deletion was successful.
            if (deleteStatement.executeUpdate() != 1) {
                deleteStatement.close();
                throw new ServiceException(ServiceStatus.APP_USER_NOT_EXIST);
            }

            deleteStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}