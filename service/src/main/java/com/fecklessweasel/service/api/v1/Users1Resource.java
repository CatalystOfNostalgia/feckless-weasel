package com.fecklessweasel.service.api.v1;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.json.CreateUserRequest;
import com.fecklessweasel.service.json.UserResponse;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.User;

/**
 * Users resource for creation and querying of users version 1.
 * @author Christian Gunderman
 */
@Path("v1/users")
public class Users1Resource {

    /** Injected information about the URI of the current request. */
    @Context
    UriInfo uriInfo;
    
    /**
     * Post Request. Creates a new user in the database and returns a
     * CreateUserResponse, or an ErrorApiResponse in JSON.
     * @param jsonBody The POST json body.
     * @return The input values in a CreateUserResponse with SUCCESS,
     * or an ErrorApiResponse upon an error.
     * Note: usernames must be unique.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonBody) throws ServiceException {
        final CreateUserRequest request = new CreateUserRequest();
        request.deserializeFrom(jsonBody);

        // Open a SQL connection and create the user.
        User user = SQLSource.interact(new SQLInteractionInterface<User>() {
                @Override
                public User run(Connection connection)
                    throws ServiceException, SQLException {

                    return User.create(connection,
                                       request.user,
                                       request.pass,
                                       request.firstName,
                                       request.lastName,
                                       request.email);
                }
            });
                    
        UserResponse userResponse = new UserResponse(ServiceStatus.CREATED,
                                                     user.getUsername(),
                                                     user.getFirstName(),
                                                     user.getLastName(),
                                                     user.getJoinDate(),
                                                     user.getEmail());

        URI newUserUri = uriInfo.getRequestUriBuilder()
            .path(user.getUsername()).build();
        
        return Response.created(newUserUri)
            .entity(userResponse.serialize()).build();
    }
}
