package com.fecklessweasel.service.datatier;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * SQLSource class encapsulates connections from the Tomcat MySQL SQL
 * connection pool. This pool is automatically load balanced among current
 * connections.
 * @author Christian Gunderman
 */
public abstract class SQLSource {
    /** Use database query. */
    private static final String USE_DB_QUERY = "USE FecklessWeaselDB";

    /**
     * Obtains a connection from the connection pool and performs the designated
     * actions upon the database, closing the connection when finished.
     * @param actions A function that performs actions upon the database.
     */
    public static <T> T interact(SQLInteractionInterface<T> actions)
        throws ServiceException {

        Connection connection = null;
        T result = null;
        try {
            // Obtain our environment naming context.
            Context initialContext = new InitialContext();
            Context environmentContext = (Context)initialContext.lookup("java:comp/env");

            // Look up our data source.
            DataSource dataSource =
                (DataSource)environmentContext.lookup("jdbc/FecklessWeaselDB");

            // Get the connection and run actions.
            connection = dataSource.getConnection();
            connection.prepareStatement(USE_DB_QUERY).execute();
            result = actions.run(connection);
        } catch (NamingException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        } finally {
            // Free the connection after use.
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
            }
        }

        return result;
    }
}