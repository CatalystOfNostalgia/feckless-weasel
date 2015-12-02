package com.fecklessweasel.service;

import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.OMUtil;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.StoredFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Downloads the given file for the user.
 * @author James Flinn
 */
@WebServlet("/servlet/file/download")
public class FileDownloadServlet extends HttpServlet {

    /**
     * When this servlet receives a GET request, downloads the given file.
     * @param request The web request.
     * @param response The web response.
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        final int fid = OMUtil.parseInt(request.getParameter("fid"));

        StoredFile fileMetadata = SQLSource.interact(new SQLInteractionInterface<StoredFile>() {
            @Override
            public StoredFile run(Connection connection)
                    throws ServiceException, SQLException {

                return StoredFile.lookup(connection, fid);
            }
        });

        File file = new File(fileMetadata.getFilePath());
        FileInputStream fileInputStream = new FileInputStream(file);

        try {
            String fileName = fileMetadata.getTitle() + "." + fileMetadata.getExtension();
            ServletContext context = getServletContext();
            // Set mime type
            String mimeType = context.getMimeType(fileMetadata.getFilePath());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setContentLength((int) file.length());
            // Force download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);

            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        } catch(IOException e) {
            throw new ServiceException(ServiceStatus.FILE_READ_ERROR);
        } finally {
            fileInputStream.close();
        }
    }
}
