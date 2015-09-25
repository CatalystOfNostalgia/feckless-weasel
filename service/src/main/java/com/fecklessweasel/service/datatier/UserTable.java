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
        "INSERT INTO User (user, pass, first_name, last_name," +
        "join_date, email)" +
        " VALUES (?,?,?,?,?,?)";

    /**
     * Inserts a user into the MySQL table with some minimal validation.
     * This method should NOT be called directly since most validation
     * occurs in the object model.
     * @param connection Connection to the database from SQLSource.
     * @param user The username.
     * @param pass The password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param joinDate The date that the user joined.
     * @param email The user's email.
     */
    public static long insertUser(Connection connection,
                                  String user,
                                  String pass,
                                  String firstName,
                                  String lastName,
                                  Date joinDate,
                                  InternetAddress email)
        throws ServiceException {

        // Check basic checks for clean arguments.
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(user, "user");
        CodeContract.assertNotNullOrEmptyOrWhitespace(pass, "pass");
        CodeContract.assertNotNullOrEmptyOrWhitespace(firstName, "firstName");
        CodeContract.assertNotNullOrEmptyOrWhitespace(lastName, "lastName");
        CodeContract.assertNotNull(joinDate, "joinDate");
        CodeContract.assertNotNull(email, "email");
        
        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_USER_QUERY,
                                              Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, user);
            insertStatement.setString(2, pass);
            insertStatement.setString(3, firstName);
            insertStatement.setString(4, lastName);
            insertStatement.setDate(5, new java.sql.Date(joinDate.getTime()));
            insertStatement.setString(6, email.getAddress());

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
}
