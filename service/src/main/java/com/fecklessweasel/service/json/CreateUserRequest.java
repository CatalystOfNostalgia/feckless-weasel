package com.fecklessweasel.service.json;

import java.util.Date;

import flexjson.JSON;

/**
 * JSON Request for Creating a user.
 * @author Christian Gunderman
 */
public class CreateUserRequest extends JsonRequest {

    /** New user's username. */
    @JSON(include=true, name="user")
    public String user;

    /** New user's password. */
    @JSON(include=true, name="pass")
    public String pass;

    /** New user's first name. */
    @JSON(include=true, name="first_name")
    public String firstName;

    /** New user's last name. */
    @JSON(include=true, name="last_name")
    public String lastName;

    /** New user's email address. */
    @JSON(include=true, name="email")
    public String email;


    /**
     * Creates a new CreateUserRequest with the provided field values.
     * @param user User name of the new user.
     * @param pass The new user's password.
     * @param firstName The new user's first name.
     * @param lastName The new user's last name.
     * @param email The user's email address.
     */
    public CreateUserRequest(String user,
                             String pass,
                             String firstName,
                             String lastName,
                             String email) {
        this.user = user;
        this.pass = pass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Creates a CreateUserRequest object with all null fields.
     */
    public CreateUserRequest() { }
}
