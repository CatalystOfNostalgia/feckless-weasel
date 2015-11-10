package com.fecklessweasel.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.University;

/**
 * Utility class for looking up universities from servlet request
 * @author Anjana Rao
 */
public final class UniversityUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private static String uniName;
    private static int univid;
    private UniversityUtil() {
    }

    public static University findUniversity(HttpServletRequest request)
            throws ServiceException {
        //create university by looking up request
        uniName = request.getParameter("name");
        return SQLSource.interact(new SQLInteractionInterface<University>() {
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookup(connection, uniName);
            }
        });
    }
    /**helper method retrieves the university with the lookup method**/
    public static University getUniversity(String university)
        throws ServiceException {
        return SQLSource.interact(new SQLInteractionInterface<University>() {
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookup(connection, uniName);
            }
        });
    }
    /**helper method takes in university and returns university with lookup method**/
    public static University getUniversityID(final int univid)
            throws ServiceException {
        return SQLSource.interact(new SQLInteractionInterface<University>() {
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookupID(connection, univid);
            }
        });

    }
}


