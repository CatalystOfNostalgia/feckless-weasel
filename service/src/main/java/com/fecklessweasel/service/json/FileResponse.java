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

    /**
     * Create an uninitialized FileResponse.
     */
    public FileResponse() {
        this(null, null, null);
    }

    /**
     * Create a new FileResponse object.
     * @param fileName The name of the file.
     * @param filePath The path to the file.
     */
    public FileResponse(ServiceStatus status, String fileName, String filePath) {
        super(status);
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
