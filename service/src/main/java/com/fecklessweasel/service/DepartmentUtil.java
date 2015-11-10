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
import com.fecklessweasel.service.objectmodel.Department;

/**
 * Utility class for looking up universities from servlet request
 * @author Anjana Rao
 */
public final class DepartmentUtil{
    /**
     * Private constructor to prevent instantiation.
     */
    private static int did;
    private DepartmentUtil() {}
    public static Department findDepartment(HttpServletRequest request)
            throws ServiceException{
        //create department by looking up request
        did = Integer.parseInt(request.getParameter("did"));

        // Open a SQL connection, find department in database
        return SQLSource.interact(new SQLInteractionInterface<Department>() {
            @Override
            public Department run(Connection connection)
                    throws ServiceException, SQLException{
                return Department.lookup(connection, did);
            }
        });
    }
}
