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
 * The servlet for rating files.
 */
@WebServlet("/servlet/file/rate")
public final class RatingServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request,
        final HttpServletResponse response)
        throws ServletException, IOException {
        
            final int rating = OMUtil.parseInt(request.getParameter("rating"));
            final String username = request.getParameter("username");
            final int fid = OMUtil.parseInt(request.getParameter("fid"));
            
             SQLSource.interact(new SQLInteractionInterface<Boolean>() {
                 @Override 
                 public Boolean run(Connection connection)
                     throws ServiceException {
                     
                     UserSession session = UserSessionUtil.resumeSession(connection, request);
                     final StoredFile file = StoredFile.lookup(connection, fid);
                     final User user = session.getUser();
                     Rating.Create(connection, user, file, rating);
                     return true;
                 }
             });
     
            response.sendRedirect("/course/file.jsp?fid=" + fid);
    }
}
