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
 * SQLSource dependency injection interface.
 * @author Christian Gunderman
 */
public interface SQLSourceInterface {
    /**
     * Obtains a connection from the connection pool and performs the designated
     * actions upon the database, closing the connection when finished.
     * @param actions A function that performs actions upon the database.
     */
    public static void interact(SQLInteractionInterface actions)
        throws ServiceException;
}
