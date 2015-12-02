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
@WebServlet("/servlet/CourseSearch")
public final class CourseSearchServlet extends HttpServlet {

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

        final int deptID = SQLSource.interact(new SQLInteractionInterface<Integer>() {
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
                int deptId=0;
                if(request.getParameter("deptId")!=null) {
                    deptId = Integer.parseInt(request.getParameter("deptId"));;
                }

                return deptId;
            }
        });

        final Department department =  SQLSource.interact(new SQLInteractionInterface<Department>() {
            /**
             * Creates a connection to access database
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public Department run(Connection connection)
                    throws ServiceException, SQLException {
                return Department.lookup(connection, deptID);
            }
        });
        List<Course> courses = SQLSource.interact(new SQLInteractionInterface<List<Course>>() {
            /**
             * Creates a connection to access database
             * @param connection
             * @return
             * @throws ServiceException
             * @throws SQLException
             */
            @Override
            public List<Course> run(Connection connection)
                    throws ServiceException, SQLException {
                return department.getAllCourses(connection);
            }
        });
        Map<String, String> coursesAndIds = new HashMap<String, String>();
        for(int i=0;i< courses.size();i++){
            coursesAndIds.put(Integer.toString(courses.get(i).getID()),Integer.toString(courses.get(i).getCourseNum()));
        }
        String jsonClass = new Gson().toJson(coursesAndIds);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonClass);
    }
}