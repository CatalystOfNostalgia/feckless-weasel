package com.fecklessweasel.service.api;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.sun.jersey.api.NotFoundException;

import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.json.ErrorJsonResponse;

/**
 * NotFoundException mapper. Maps resource not found into appropriate API error
 * in JSON serialized format.
 * @author Christian Gunderman
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    /**
     * Serializes NotFoudnException to JSON ErrorApiResponse.
     */
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(NotFoundException ex) {
        return Response.status(ServiceStatus.NOT_FOUND.httpCode)
            .entity(new ErrorJsonResponse(ServiceStatus.NOT_FOUND, ex)
                    .serialize()).build();
    }
}

