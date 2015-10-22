package com.fecklessweasel.service.api.v1;

import com.fecklessweasel.service.api.NotFoundExceptionMapper;
import com.fecklessweasel.service.json.FileResponse;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Files resource for downloading/uploading files.
 * @author James Flinn
 */
@Path("v1/files/{class}/{file}")
public class Files1Resource {

    // TODO: This prefix should be changed
    private static final String FILEPATH_PREFIX = "/Users/jamesflinn";

    @Context
    UriInfo uriInfo;

    /**
     * Get request. Downloads the file at FILEPATH_PREFIX/class/{file}.
     * @param className The name of the class the file belongs to.
     * @param fileName The name of the file being downloaded.
     * @return The file requested or NotFoundException if file is not found.
     */
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("class") String className,
                                 @PathParam("file") String fileName) {
        String filePath = FILEPATH_PREFIX + "/" + className +  "/" + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            return new NotFoundExceptionMapper().toResponse(new NotFoundException());
        }

        return Response.ok(file)
                       .header("Content-Disposition", "attachment; filename=" + file.getName())
                       .build();
    }

    /**
     * Post request. Uploads a file to FILEPATH_PREFIX/class/{file}
     * @param className The name of the class the file belongs to.
     * @param fileName The name of the file being uploaded.
     * @param fileInputStream The file input stream.
     * @param contentHeader The file header information.
     * @return JSON response with file location.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postFile(@PathParam("class") String className,
                             @PathParam("file") String fileName,
                             @FormDataParam("file") InputStream fileInputStream,
                             @FormDataParam("file") FormDataContentDisposition contentHeader)
                             throws ServiceException {

        String filePath = FILEPATH_PREFIX + "/" + className + "/" + fileName;

        // TODO: Must check if user is authenticated (must get current user first)

        if (Files.exists(Paths.get(filePath))) {
            return new NotFoundExceptionMapper().toResponse(new NotFoundException());
        }

        if (!saveFile(fileInputStream, filePath)) {
            FileResponse fileResponse = new FileResponse(ServiceStatus.UNKNOWN_ERROR,
                                                         fileName,
                                                         filePath,
                                                         false);
            return Response.ok(fileResponse.serialize()).build();
        }

        FileResponse fileResponse = new FileResponse(ServiceStatus.CREATED,
                                                     fileName,
                                                     filePath,
                                                     true);
        URI newPathURI = uriInfo.getRequestUriBuilder().build();

        return Response.created(newPathURI).entity(fileResponse.serialize()).build();
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
