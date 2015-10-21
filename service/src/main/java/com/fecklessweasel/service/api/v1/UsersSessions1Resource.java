package com.fecklessweasel.service.api.v1;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fecklessweasel.service.json.CreateUserSessionRequest;
import com.fecklessweasel.service.json.JsonResponse;
import com.fecklessweasel.service.json.UserSessionResponse;
import com.fecklessweasel.service.datatier.SQLInteractionInterface;
import com.fecklessweasel.service.datatier.SQLSource;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;
import com.fecklessweasel.service.objectmodel.UserSession;
import com.fecklessweasel.service.objectmodel.OMUtil;

/**
 * Api Resource for creating a new session, a.k.a. logging in and out.
 * @author Christian Gunderman
 */
@Path("v1/users/{username}/sessions")
public class UsersSessions1Resource {

    /** Injected information about the URI of the current request. */
    @Context
    UriInfo uriInfo;

    /**
     * Creates a new session for the given user with the given password.
     * @throws ServiceException If database error occurs, password is incorrect,
     * or some other known error occurs.
     * @param username The username of the person logging in. This param
     * is part of the URL but is ignored. Actual username comes in via Json.
     * @param jsonBody The Json request.
     * @return 201 CREATED upon completion.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSession(@PathParam("username")String username,
                                  String jsonBody) throws ServiceException {
        final CreateUserSessionRequest request = new CreateUserSessionRequest();
        request.deserializeFrom(jsonBody);

        // Open a SQL connection and create the session.
        UserSession session = SQLSource.interact(new SQLInteractionInterface<UserSession>() {
                @Override
                public UserSession run(Connection connection)
                    throws ServiceException, SQLException {

                    return UserSession.start(connection,
                                             request.user,
                                             request.pass);
                }
            });

        URI newUri = uriInfo.getRequestUriBuilder()
            .path(session.getSessionId().toString()).build();

        UserSessionResponse response
            = new UserSessionResponse(ServiceStatus.CREATED,
                                      session.getUser().getUsername(),
                                      session.getSessionId());

        return Response.created(newUri)
            .entity(response.serialize()).build();
    }

    /**
     * Gets a session's info. This end point requires authentication
     * and can only be accessed by ROLE_ADMIN users and the owner
     * of the session being browsed.
     * @param sessionHeader The authentication header. Feed into
     * UserSession.resume().
     * @param username The username from the URL.
     * @param sessionId The unique session identifier.
     * @return 200 OK.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{sessionId}")
    public Response getSession(@HeaderParam(UserSession.HEADER)final String sessionHeader,
                               @PathParam("username")final String username,
                               @PathParam("sessionId")final String sessionId)
        throws ServiceException {

        UserSession desired = SQLSource.interact(new SQLInteractionInterface<UserSession>() {
                @Override
                public UserSession run(Connection connection)
                    throws ServiceException, SQLException {

                    UserSession session = UserSession.resumeFromSessionString(connection,
                                                                              sessionHeader);
                    UserSession desired = UserSession.resume(connection,
                                                             username,
                                                             sessionId);
                    
                    OMUtil.adminOrOwnerCheck(session.getUser(), desired.getUser());

                    return desired;
                }
            });
        
        UserSessionResponse response = new UserSessionResponse(ServiceStatus.OK,
                                                               desired.getUser().getUsername(),
                                                               desired.getSessionId());
        return Response.ok(response.serialize()).build();
    }

    /**
     * Deletes a single session with the given Id. Requires authentication
     * and can only be accessed by ROLE_ADMIN users and the owner of the
     * session being deleted.
     * @param sessionHeader The session authentication header.
     * @param username The username of the session owner.
     * @param sessionId The id of the session to be deleted.
     * @return 225 DELETED.
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{sessionId}")
    public Response deleteSession(@HeaderParam(UserSession.HEADER)final String sessionHeader,
                                  @PathParam("username")final String username,
                                  @PathParam("sessionId") final String sessionId)
        throws ServiceException {

        UserSession forsaken = SQLSource.interact(new SQLInteractionInterface<UserSession>() {
                @Override
                public UserSession run(Connection connection)
                    throws ServiceException, SQLException {
                    UserSession session = UserSession.resumeFromSessionString(connection, sessionHeader);
                    UserSession forsaken = UserSession.resume(connection, username, sessionId);

                    OMUtil.adminOrOwnerCheck(session.getUser(), forsaken.getUser());
            
                    forsaken.end(connection);

                    return forsaken;
                }
            });

        UserSessionResponse response = new UserSessionResponse(ServiceStatus.DELETED,
                                                               forsaken.getUser().getUsername(),
                                                               forsaken.getSessionId());
        return Response.ok(response.serialize()).build();
    }

    /**
     * Deletes all of a user's sessions. Requires authentication by the session
     * owner or a ROLE_ADMIN user.
     * @param sessionHeader The authentication information of the caller.
     * @param username The username of the session owner.
     * @return 200 OK.
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllSessions(@HeaderParam(UserSession.HEADER) final String sessionHeader,
                                      @PathParam("username") final String username)
        throws ServiceException {

        SQLSource.interact(new SQLInteractionInterface<UserSession>() {
                @Override
                public UserSession run(Connection connection)
                    throws ServiceException, SQLException {
                    UserSession session = UserSession.resumeFromSessionString(connection, sessionHeader);

                    if (!session.getUser().getUsername().equals(username) &&
                        !session.getUser().isRole("ROLE_ADMIN")) {
                        throw new ServiceException(ServiceStatus.ACCESS_DENIED);
                    }

                    UserSession.endAll(connection, username);

                    return session;
                }
            });
        
        // TODO: we should probably return the sessions that were killed.
        JsonResponse response = new JsonResponse(ServiceStatus.DELETED);
        return Response.ok(response.serialize()).build();
    }
}
