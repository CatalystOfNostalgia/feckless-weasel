package com.fecklessweasel.service.api;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.json.JsonResponse;
import com.fecklessweasel.service.json.ErrorJsonResponse;

/**
 * UncheckedException mapper. Maps unchecked exceptions (bugs) to a
 * general server error message.
 * @author Christian Gunderman
 */
@Provider
public class UncheckedExceptionMapper implements ExceptionMapper<Throwable> {

    /**
     * Serializes Unchecked exceptions to generic error message.
     */
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(Throwable ex) {

	// TODO: Log these exceptions somewhere for debug purposes.
        return Response.status(ServiceStatus.UNKNOWN_ERROR.httpCode)
            .entity(new ErrorJsonResponse(ServiceStatus.UNKNOWN_ERROR, ex).serialize())
            .build();
    }
}
