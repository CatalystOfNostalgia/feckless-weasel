package com.fecklessweasel.service.json;

import com.fecklessweasel.service.objectmodel.ServiceStatus;
import flexjson.JSON;

/**
 * File JSON response from server to client.
 * @author James Flinn
 */
public class FileResponse extends JsonResponse {
    /** Filename. */
    @JSON(include=true, name="file_name")
    public String fileName;

    /** Path to file */
    @JSON(include=true, name="file_path")
    public String filePath;

    /** If file has been saved on server */
    @JSON(include=true, name="file_saved")
    public boolean fileSaved;

    /**
     * Create an uninitialized FileResponse.
     */
    public FileResponse() {
        this(null, null, null, false);
    }

    /**
     * Create a new FileResponse object.
     * @param fileName The name of the file.
     * @param filePath The path to the file.
     */
    public FileResponse(ServiceStatus status, String fileName, String filePath, boolean fileSaved) {
        super(status);
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSaved = fileSaved;
    }
}
