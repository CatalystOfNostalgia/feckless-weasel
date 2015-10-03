package com.fecklessweasel.service.json;

import java.util.Date;

import flexjson.JSON;

import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * User JSON Response from server to client.
 * @author Christian Gunderman
 */
public class UserResponse extends JsonResponse {

    /** Username. */
    @JSON(include=true, name="user")
    public String user;

    /** Password, always null. We NEVER pass it back. */
    @JSON(include=true, name="pass")
    public String pass = null;

    /** Date user joined. Determined serverside and passed back. */
    @JSON(include=true, name="join_date")
    public Date joinDate;

    /** User's email address. */
    @JSON(include=true, name="email")
    public String email;

    /**
     * Creates uninitialized UserResponse for client side deserialization.
     */
    public UserResponse() {
        this(null, null, null, null);
    }

    /**
     * Creates a new UserResponse object.
     * @param status The status of the operation.
     * @param user The username of the new user.
     * @param joinDate The date the new user joined.
     */
    public UserResponse(ServiceStatus status,
                        String user,
                        Date joinDate,
                        String email) {
        super(status);
        this.user = user;
        this.pass = null;
        this.joinDate = joinDate;
        this.email = email;
    }
}
