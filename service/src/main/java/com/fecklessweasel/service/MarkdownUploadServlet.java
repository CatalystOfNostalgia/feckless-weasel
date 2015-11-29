package com.fecklessweasel.service;

import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.*;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Uploads markdown files from the web editor to the server.
 * @author James Flinn
 */
@WebServlet("/servlet/markdown/upload")
public class MarkdownUploadServlet extends HttpServlet {

    /**
     * Uploads the markdown file from the web editor to the server.
     * @param request The web request.
     * @param response The web response.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        final UserSession session = UserSessionUtil.resumeSession(request);

        // If user is not authenticated then throw.
        if (session == null) {
            throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
        }

        // Get title, description, and file contents from form
        final String title = request.getParameter("title");
        final String description = request.getParameter("description");
        final String markdownText = request.getParameter("markdown");

        // Open a SQL connection and create the file meta data.
        StoredFile fileMetadata = SQLSource.interact(new SQLInteractionInterface<StoredFile>() {
            @Override
            public StoredFile run(Connection connection)
                    throws ServiceException, SQLException {

                int courseID = OMUtil.parseInt(request.getParameter("cid"));

                // Write and store file.
                return StoredFile.create(connection,
                        session.getUser(),
                        Course.lookupById(connection, courseID),
                        title,
                        description,
                        markdownText);
            }
        });
    }
}
