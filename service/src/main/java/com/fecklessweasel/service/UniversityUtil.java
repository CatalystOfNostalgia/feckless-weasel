package com.fecklessweasel.service;

import java.io.IOException;
import java.lang.NumberFormatException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.CodeContract;
import com.fecklessweasel.service.objectmodel.OMUtil;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.University;

/**
 * Utility class for looking up universities from servlet request
 *
 * @author Anjana Rao
 */
public final class UniversityUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private UniversityUtil() {
    }

    /**
     * receives request and gets corresponding university
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    public static University findUniversity(HttpServletRequest request)
            throws ServiceException {

        // Create university by looking up request.
        final int univid = OMUtil.parseInt(request.getParameter("uid"));

        // Open a SQL connection, find University in database
        return SQLSource.interact(new SQLInteractionInterface<University>() {
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookup(connection, univid);
            }
        });
    }

    /**
     * Returns University given id
     *
     * @param univid
     * @return
     * @throws ServiceException
     */
    public static University getUniversityID(final int univid)
            throws ServiceException {
        return SQLSource.interact(new SQLInteractionInterface<University>() {
            /**
             *
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookup(connection, univid);
            }
        });

    }
}


