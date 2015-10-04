package com.fecklessweasel.service.datatier;

import java.sql.Connection;
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
 * Wrapper class for SQL UserRole Table. Defines UserRoles.
 * @author Christian Gunderman
 */
public abstract class UserRoleTable {

    /** Create role query. */
    public static final String INSERT_ROLE_QUERY
        = "INSERT INTO UserRole (role, description) VALUES (?,?)";

    /** Lookup Role query. */
    public static final String LOOKUP_ROLE_QUERY
        = "SELECT * FROM UserRole WHERE role=?";

    /** Delete Role query. */
    public static final String DELETE_ROLE_QUERY
        = "DELETE FROM UserRole WHERE role=?";

    /** Admin Role ID. */
    public static final String ROLE_ADMIN_ID = "ROLE_ADMIN";

    /** Admin Role Description. */
    private static final String ROLE_ADMIN_DESCRIPTION
        = "Administrator with unrestricted access.";

    /** User Role ID. */
    public static final String ROLE_USER_ID = "ROLE_USER";

    /** User Role Description. */
    private static final String ROLE_USER_DESCRIPTION
        = "Standard user account.";

    /**
     * Adds a new UserRole to the table with the specified Role ID
     * and descriptions.
     * @param connection The MySQL connection from SQLSource.
     * @param roleId The unique id string for this role.
     * @param description The role's description.
     */
    public static void insertUserRole(Connection connection,
                                      String roleId,
                                      String description) throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(roleId, "roleId");
        CodeContract.assertNotNullOrEmptyOrWhitespace(description, "description");
        
        try {
            PreparedStatement insertStatement
                = connection.prepareStatement(INSERT_ROLE_QUERY);
            insertStatement.setString(1, roleId);
            insertStatement.setString(2, description);

            insertStatement.execute();
            insertStatement.close();
        } catch (SQLIntegrityConstraintViolationException ex) {
            // Thrown if role id already exists.
            throw new ServiceException(ServiceStatus.APP_ROLE_ID_TAKEN, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up a role by unique Role id string.
     * @param connection The connection from SQLSource.
     * @param roleId The id String for the role to look up.
     * @return A ResultSet containing the Role columns.
     */
    public static ResultSet lookupUserRole(Connection connection,
                                           String roleId) throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(roleId, "roleId");
        
        try {
            PreparedStatement lookupStatement
                = connection.prepareStatement(LOOKUP_ROLE_QUERY);

            lookupStatement.setString(1, roleId);

            ResultSet result = lookupStatement.executeQuery();
            lookupStatement.close();
            
            return result;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR);
        }
    }

    /**
     * Deletes a User Role by it's String id.
     * @param connection SQL connection from SQLSource.
     * @param roleId The unique Role string id.
     */
    public static void deleteUserRole(Connection connection,
                                      String roleId) throws ServiceException {
        CodeContract.assertNotNull(connection, "connection");
        CodeContract.assertNotNullOrEmptyOrWhitespace(roleId, "roleId");

        try {
            PreparedStatement deleteStatement
                = connection.prepareStatement(DELETE_ROLE_QUERY);

            deleteStatement.setString(1, roleId);

            if (deleteStatement.executeUpdate() != 1) {
                throw new ServiceException(ServiceStatus.APP_ROLE_NOT_EXIST);
            }

            deleteStatement.close();
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR);
        }
    }
}
