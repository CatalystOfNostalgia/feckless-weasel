package com.fecklessweasel.service;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.objectmodel.*;
import com.fecklessweasel.service.*;

@WebServlet("/servlet/department")
public final class DepartmentServlet extends HttpServlet {

    /**
     * Handles post requests to this end point. Performs the creation
     * of departments given a set of parameters.
     * **/
    String uni;
    String deptName;
    String acronym;
    public int ID;
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        SQLSource.interact(new SQLInteractionInterface<Integer>() {
                               @Override
                               public Integer run(Connection connection)
                                       throws ServiceException, SQLException {
                                   uni = request.getParameter("university");
                                   University university = UniversityUtil.getUniversity(uni);
                                   deptName = request.getParameter("deptName");
                                   acronym = request.getParameter("acronym");
                                   // Create university
                                   Department department = Department.create(connection, university, deptName, acronym);
                                   //get dept ID from database
                                   ID = department.getID();
                                   // return int value
                                   return 0;

                               }
                           }
        );
        // Redirect to homepage.
        response.sendRedirect("/department/index.jsp?did=" + ID);
    }
}
