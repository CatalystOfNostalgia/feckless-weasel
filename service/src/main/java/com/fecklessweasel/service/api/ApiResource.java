package com.fecklessweasel.service.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

/**
 * Api Root resource.
 */
@Path("")
public class ApiResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        return Response.ok("API Root").build();
    }
}
