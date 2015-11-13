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
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.University;
import com.fecklessweasel.service.objectmodel.Department;

/**
 * Utility class for looking up universities from servlet request
 *
 * @author Anjana Rao
 */
public final class DepartmentUtil {

    private DepartmentUtil() {
    }

    /**
     * gets request and returns department
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    public static Department findDepartment(HttpServletRequest request)
            throws ServiceException {

        String deptID = request.getParameter("did");
        if (deptID == null) {
            throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
        }
        final int did = Integer.parseInt(deptID);

        // Open a SQL connection, find department in database
        return SQLSource.interact(new SQLInteractionInterface<Department>() {
            /**
             * connects to databsae and looks up department in table
             *
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public Department run(Connection connection)
                    throws ServiceException, SQLException {
                return Department.lookup(connection, did);
            }
        });
    }

    /**
     * takes in the deptID and returns corresponding department
     * @param deptID
     * @return
     */
    public static Department findDepartmentID(int deptID)
            throws ServiceException {

        final int did = deptID;
        return SQLSource.interact(new SQLInteractionInterface<Department>() {
            /**
             * connects to database and looks up department in table
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public Department run(Connection connection)
                    throws ServiceException, SQLException {
                return Department.lookup(connection, did);
            }
        });
    }
}

