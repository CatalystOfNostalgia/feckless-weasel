package com.fecklessweasel.service.json;

import flexjson.JSON;

import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * JSON Response passed to client from server after request.
 * @author Christian Gunderman
 */
public class JsonResponse extends Json {

    /** Server Status Enum as String. */
    @JSON(include=true, name="status")
    public String status;

    /** Server error code integer. */
    @JSON(include=true, name="code")
    public int code;

    /** HTTP response code as integer. */
    @JSON(include=true, name="http_code")
    public int httpCode;

    /** Status message as String. */
    @JSON(include=true, name="message")
    public String message;

    /**
     * Creates a new Response object.
     * @param status The status of the request.
     */
    public JsonResponse(ServiceStatus status) {
        super();

        // Accept null status.
        if (status == null) {
            return;
        }

        this.status = status.status;
        this.code = status.code;
        this.httpCode = status.httpCode;
        this.message = status.message;
    }
}
