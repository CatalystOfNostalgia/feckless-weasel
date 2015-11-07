package com.fecklessweasel.service;

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

/**
 * Downloads the given file for the user.
 * @author James Flinn
 */
@WebServlet("/servlet/file/download")
public class FileDownloadServlet extends HttpServlet {

    private static final String FILEPATH_PREFIX = "files";

    /**
     * When this servlet receives a GET request, downloads the given file.
     * @param request The web request.
     * @param response The web response.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String className = request.getParameter("class");
        int fid = Integer.parseInt(request.getParameter("fid"));

        String filePath = FILEPATH_PREFIX + "/" + className + "/" + fid;
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        ServletContext context = getServletContext();
        // Set mime type
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        // Force download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
        response.setHeader(headerKey, headerValue);

        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
