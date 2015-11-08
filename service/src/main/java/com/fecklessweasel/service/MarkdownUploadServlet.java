package com.fecklessweasel.service;

import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.FileMetadata;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.UserSession;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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

    private static final String FILEPATH_PREFIX = "files";

    /**
     * Uploads the markdown file from the web editor to the server.
     * @param request The web request.
     * @param response The web response.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        // Get title, description, and file contents from form
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String text = request.getParameter("markdown");

        final UserSession session = UserSessionUtil.resumeSession(request);
        // If user is not authenticated
        if (session == null) {
            throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
        }

        // Open a SQL connection and create the file meta data.
        FileMetadata fileMetadata = SQLSource.interact(new SQLInteractionInterface<FileMetadata>() {
            @Override
            public FileMetadata run(Connection connection)
                    throws ServiceException, SQLException {

                int classId = Integer.parseInt(request.getParameter("class"));
                return FileMetadata.create(connection, session.getUser(), classId, new Date());
            }
        });

        String filePath = FILEPATH_PREFIX + "/" + fileMetadata.getCourse() + "/";
        String fileName = fileMetadata.getFid() + ".md";
        if (!saveFile(text, filePath, fileName)) {
            throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
        }
    }

    /**
     * Saves the markdown file to the server.
     * @param text The text of the markdown file.
     * @param filePath The file path the file will be saved to.
     * @return Returns true if file is successfully saved, false otherwise.
     */
    private boolean saveFile(String text, String filePath, String fileName) {
        try {
            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File markdownFile = new File(filePath + fileName);
            markdownFile.createNewFile();
            // Write the markdown text to the file
            PrintWriter writer = new PrintWriter(markdownFile);
            writer.print(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
