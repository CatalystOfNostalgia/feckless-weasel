package com.fecklessweasel.service;

import java.io.*;
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

    private static final String FILEPATH_PREFIX = "/Users/jamesflinn";

    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        System.out.println("\n\n================\n\n");
        for (Part part : request.getParts()) {
            System.out.println(part.getName());
        }
        System.out.println("\n\n================\n\n");
        Part filePart = request.getPart("file[]");
        String fileName = filePart.getSubmittedFileName();

        String filePath = FILEPATH_PREFIX + "/Documents/files/" + fileName;
        saveFile(filePart.getInputStream(), title);
    }

    /**
     * Saves a file to the server.
     * @param inputStream The file input stream.
     * @param filePath The file path the file will be saved to.
     * @return Returns true if file is successfully saved, false otherwise.
     */
    private boolean saveFile(InputStream inputStream, String filePath) {
        try {
            int read;
            byte[] bytes = new byte[1024];
            OutputStream outputStream = new FileOutputStream(new File(filePath));

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
