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
 * The servlet for doing commenting on files.
 */
@WebServlet("/servlet/file/comment")
public final class FileServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest request,
        final HttpServletResponse response)
        throws ServletException, IOException {
        
            final String text = request.getParameter("text");
            final int fileID = OMUtil.parseInt(request.getParameter("fileid"));
            final String username = request.getParameter("username");
            
             SQLSource.interact(new SQLInteractionInterface<Boolean>() {
                 @Override 
                 public Boolean run(Connection connection)
                     throws ServiceException {
                     
                     final StoredFile file = StoredFile.lookup(connection, fileID);
                     final User user = User.lookup(connection, username);
                     Comment.Create(connection, user, file, text);
                     return true;
                 }
             });
     
            response.sendRedirect("/course/file.jsp?fid=" + fileID);
    }
}
