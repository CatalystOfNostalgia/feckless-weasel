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

    /** User's first name. */
    @JSON(include=true, name="first_name")
    public String firstName;

    /** User's last name. */
    @JSON(include=true, name="last_name")
    public String lastName;

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
        this(null, null, null, null, null, null);
    }

    /**
     * Creates a new UserResponse object.
     * @param status The status of the operation.
     * @param user The username of the new user.
     * @param firstName The user's first name.
     * @param lastName The last name of the user.
     * @param joinDate The date the new user joined.
     */
    public UserResponse(ServiceStatus status,
                        String user,
                        String firstName,
                        String lastName,
                        Date joinDate,
                        String email) {
        super(status);
        this.user = user;
        this.pass = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.email = email;
    }
}
