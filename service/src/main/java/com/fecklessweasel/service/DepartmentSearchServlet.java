
package com.fecklessweasel.service;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.*;
import com.fecklessweasel.service.*;



/**
 * Servlet returns list of departments
 */
@WebServlet("/servlet/DepartmentSearch")
public final class DepartmentSearchServlet extends HttpServlet {

    /**
     * receives request of univID form header and returns list of departments in university
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        final int univID = SQLSource.interact(new SQLInteractionInterface<Integer>() {
            /**
             * Creates a connection to access database
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public Integer run(Connection connection)
                    throws ServiceException, SQLException {
                int univId=0;
                if(request.getParameter("univId")!=null) {
                    univId = Integer.parseInt(request.getParameter("univId"));;
                }

                return univId;
            }
        });

        final University university =  SQLSource.interact(new SQLInteractionInterface<University>() {
            /**
             * Creates a connection to access database
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public University run(Connection connection)
                    throws ServiceException, SQLException {
                return University.lookup(connection, univID);
            }
        });
        List<Department> departments = SQLSource.interact(new SQLInteractionInterface<List<Department>>() {
            /**
             * Creates a connection to access database
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public List<Department> run(Connection connection)
                    throws ServiceException, SQLException {
                return university.getAllDepts(connection);
            }
        });
        Map<String, String> deptsAndIds = new HashMap<String, String>();
        for(int i=0;i< departments.size();i++){
            deptsAndIds.put(Integer.toString(departments.get(i).getID()),departments.get(i).getAcronym());
        }
        String json = new Gson().toJson(deptsAndIds);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}