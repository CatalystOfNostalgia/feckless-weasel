package com.fecklessweasel.service.api.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.WebApplicationException;

import java.sql.SQLException;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Version1 Resource.
 * @author Christian Gunderman
 */
@Path("v1")
public class Version1Resource {

    /**
     * Just show "API v1" to establish that there is something here.
     * TODO: Take out as development progresses or return a JSON index of
     * implemented resources.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() throws ServiceException {
        return Response.ok("API v1").build();
    }
}
