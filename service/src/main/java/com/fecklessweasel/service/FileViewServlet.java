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

/**
 * The file servlet for handling some file operations
 * @author Hayden Schmackpfeffer
 */
@WebServlet("/servlet/file")
public final class FileViewServlet extends HttpServlet {

    /**
     * Handles get requests the file page to handle Favoriting this file
     * @param request contains parameters username and fid
     * @param response directs to course/file.jsp
     * @throws ServletException When any handled error happens.
     * @throws IOException Server was unable to process the request.
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        final String username = request.getParameter("username");
        final int fileID = OMUtil.parseInt(request.getParameter("fid"));

        Boolean b =
        SQLSource.interact(new SQLInteractionInterface<Boolean>() {
            @Override 
            public Boolean run(Connection connection)
                throws ServiceException {
  
                final User user = User.lookup(connection, username);
                return (Boolean) user.toggleFavoriteFile(connection, fileID);
            }
        });

        response.sendRedirect("/course/file.jsp?fid=" + fileID);
    }
}