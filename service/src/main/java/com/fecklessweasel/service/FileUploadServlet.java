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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/servlet/file/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private static final String FILEPATH_PREFIX = "files";

    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {

        // TODO: These should go into database eventually
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Part filePart = request.getPart("file[0]");
        String fileName = filePart.getSubmittedFileName();

        final UserSession session = UserSessionUtil.resumeSession(request);
        // If user is not authenticated
        if (session == null) {
            throw new ServiceException(ServiceStatus.NOT_AUTHENTICATED);
        }

        // Open a SQL connection and create the file meta data.
//        FileMetadata fileMetadata = SQLSource.interact(new SQLInteractionInterface<FileMetadata>() {
//            @Override
//            public FileMetadata run(Connection connection)
//                    throws ServiceException, SQLException {
//
//                int classId = Integer.parseInt(request.getParameter("class"));
//                return FileMetadata.create(connection, session.getUser(), classId, new Date());
//            }
//        });

//        String filePath = FILEPATH_PREFIX + fileMetadata.getCourse() + fileName;
        String filePath = FILEPATH_PREFIX + "/" + fileName;
        if (!saveFile(filePart.getInputStream(), filePath, fileName)) {
            throw new ServiceException(ServiceStatus.SERVER_UPLOAD_ERROR);
        }
    }

    /**
     * Saves a file to the server.
     * @param inputStream The file input stream.
     * @param filePath The file path the file will be saved to.
     * @return Returns true if file is successfully saved, false otherwise.
     */
    private boolean saveFile(InputStream inputStream, String filePath, String fileName) {
        try {
            int read;
            byte[] bytes = new byte[1024];

            File directory = new File(filePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(new File(filePath + fileName));

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
