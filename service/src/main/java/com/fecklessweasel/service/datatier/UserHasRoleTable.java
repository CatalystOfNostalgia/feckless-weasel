package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import java.util.Date;

import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

import javax.mail.internet.InternetAddress;

/**
 * Implements SQL queries for the checking if a User has a specific role.
 * @author Christian Gunderman
 */
public abstract class UserHasRoleTable {

    /** Mark user as having a Role. */
    public static final String INSERT_USER_HAS_ROLE_QUERY
        = "INSERT INTO UserHasRole (uid, rid) VALUES" +
        " (?, (SELECT rid FROM UserRole R WHERE R.role=?))";

    /** Mark user as having a Role by Role String id. */
    public static final String DELETE_USER_HAS_ROLE_NAME_QUERY
        = "DELETE FROM UserHasRole WHERE uid=?" +
        " AND rid=(SELECT rid FROM UserRole R WHERE R.role=?)";

    /**
     * Give User requested Role.
     * @throws ServiceException If database error or user already has Role, or
     * requested Role doesn't exist.
     * @param connection The SQL connection from SQLSource.
     * @param uid The user's unique AUTO_INCREMENT id from the table.
     * @param roleName The String id of the Role.
     */
    public static void insertUserHasRole(Connection connection,
                                         long uid,
                                         String roleName) throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(roleName, "roleName");

        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_USER_HAS_ROLE_QUERY);
            insertStatement.setLong(1, uid);
            insertStatement.setString(2, roleName);

            insertStatement.execute();
        } catch (SQLIntegrityConstraintViolationException ex) {
            // User is already of given Role or Role not exist.
            throw new ServiceException(
                ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Remove user from Role.
     * @throws ServiceException If database error or user doesn't have Role.
     * @param connection The connection from SQLSource.
     * @param uid The user's unique AUTO_INCREMENT id from the table.
     * @param role The name of the role to delete.
     * @return The ResultSet containing the Role information.
     */
    public static void deleteUserHasRole(Connection connection,
                                         long uid,
                                         String roleName) throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(roleName, "roleName");

        try {
            PreparedStatement deleteStatement
                = connection.prepareStatement(DELETE_USER_HAS_ROLE_NAME_QUERY);
            deleteStatement.setLong(1, uid);
            deleteStatement.setString(2, roleName);

            deleteStatement.execute();

            deleteStatement.close();
        } catch (SQLIntegrityConstraintViolationException ex) {
            // User doesn't have Role.
            throw new ServiceException(
                ServiceStatus.APP_USER_NOT_HAVE_ROLE, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }
}
