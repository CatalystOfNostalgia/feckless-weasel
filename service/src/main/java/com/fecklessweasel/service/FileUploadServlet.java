package com.fecklessweasel.service;

import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.Course;
import com.fecklessweasel.service.objectmodel.OMUtil;
import com.fecklessweasel.service.objectmodel.StoredFile;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.UserSession;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet for file uploads.
 * @author James Flinn
 * @author Christian Gunderman
 */
@WebServlet("/servlet/file/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    /**
     * HTTP Post servlet method.
     * @param request The HTTP POST request from the client.
     * @param response The HTTP response to return to the client.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        final UserSession session = UserSessionUtil.resumeSession(request);

        // If user is not authenticated throw.
        if (session == null) {
            throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
        }

        final String title = request.getParameter("title");
        final String description = request.getParameter("description");
        final String tag = request.getParameter("tag");
        final Part filePart = request.getPart("file");
        // filePart is dereferenced in this file so we can't depend on the
        // objectmodel to null check it.
        OMUtil.nullCheck(filePart);

        // Open a SQL connection and create the file meta data.
        StoredFile fileMetadata = SQLSource.interact(new SQLInteractionInterface<StoredFile>() {
            @Override
            public StoredFile run(Connection connection)
                throws ServiceException, SQLException {

                // Parse course ID.
                try {
                    int courseID = OMUtil.parseInt(request.getParameter("class"));
                    String extension = filePart.getSubmittedFileName().split("\\.")[1];

                    // Create and store file.
                    return StoredFile.create(connection,
                                             session.getUser(),
                                             Course.lookupById(connection, courseID),
                                             title,
                                             description,
                                             tag,
                                             extension,
                                             filePart.getInputStream());
                } catch (IOException ex) {
                    throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
                }
            }
        });

       response.sendRedirect("/course/index.jsp?cid=" + request.getParameter("class"));
    }
}
