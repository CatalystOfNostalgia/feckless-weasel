package com.fecklessweasel.service.api;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.json.ErrorJsonResponse;

/**
 * ApiException mapper. Maps ApiExceptions thrown in resources into appropriate
 * JSON serialized format.
 * @author Christian Gunderman
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<ServiceException> {

    /**
     * Serializes ApiExceptions to ErrorJsonResponse.
     */
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(ServiceException ex) {
        return Response.status(ex.status.httpCode)
            .entity(new ErrorJsonResponse(ex.status, ex.getCause()).serialize()).build();
    }
}

