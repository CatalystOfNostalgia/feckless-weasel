package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.objectmodel.ServiceException;

/**
 * Interface for SQL interactions. Implement this interface and call
 * SQLSource.interact() to perform an action upon the DB.
 * @author Christian Gunderman
 */
public interface SQLInteractionInterface {

    /**
     * This function should be implemented to do the desired action.
     * @param connection An active SQL connection. It will be closed
     * automatically by the SQLSource.
     * @throws ServiceException An application handled error, such as
     * user already exists happened within the DB call. This is passed
     * up the stack.
     * @throws SQLException A database error occurred. This is handled
     * by the SQL source and is passed up the stack as a generic 
     * DATABASE_ERROR.
     */
    public void run(Connection connection) throws ServiceException, SQLException;
}
