package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.UserTable;

/**
 * User API, used for all operations pertaining to a user and his or her
 * relations.
 * @author Christian Gunderman
 */
public class User {
    /** Minimum user account length. */
    private static final int USER_MIN = 6;
    /**
     * Maximum user account length. You MUST make sure that this value is less
     * than or equal to the column width in UserTable.
     */
    private static final int USER_MAX = 25;
    /** Password minimum length */
    private static final int PASS_MIN = 6;
    /** Maximum password length. */
    private static final int PASS_MAX = 100;
    /** First name minimum length. */
    private static final int FIRST_NAME_MIN = 2;
    /** First name maximum length. */
    private static final int FIRST_NAME_MAX = 25;
    /** Last name minimum length. */
    private static final int LAST_NAME_MIN = 2;
    /** Last name maximum length. */
    private static final int LAST_NAME_MAX = 25;
    /** Maximum email length. */
    private static final int EMAIL_MAX = 320;

    /** Unique User id integer. */
    private int uid;
    /** Username. */
    private String username;
    /** Password hashes. */
    private String passwordHash;
    /** First name. */
    private String firstName;
    /** Last name. */
    private String lastName;
    /** Date user joined. */
    private Date joinDate;
    /** User's birthday. */
    private Date birthday;
    /** User's email address. */
    private String email;

    public static User create(Connection sql,
                              String username,
                              String password,
                              String firstName,
                              String lastName,
                              String emailStr) throws ServiceException {

        // Null check everything:
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(username);
        OMUtil.nullCheck(password);
        OMUtil.nullCheck(firstName);
        OMUtil.nullCheck(lastName);
        OMUtil.nullCheck(emailStr);

        // Check username length.
        username = username.toLowerCase();
        if (username.length() > USER_MAX || username.length() < USER_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_USER_LENGTH);
        }
        // Check username characters.
        if(!OMUtil.isValidInput(username)){
            throw new ServiceException(ServiceStatus.APP_INVALID_USERNAME);
        }

        // Check password length.
        if (password.length() > PASS_MAX || password.length() < PASS_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_PASS_LENGTH);
        }
        // Check for spaces.
        if (password.contains(" ")){
            throw new ServiceException(ServiceStatus.APP_INVALID_PASSWORD);
        }

        // Check first name length.
        if (firstName.length() > FIRST_NAME_MAX ||
            firstName.length() < FIRST_NAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_NAME);
        }

        // Check last name length.
        if (lastName.length() > LAST_NAME_MAX ||
            lastName.length() < LAST_NAME_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_NAME);
        }

        // Check for valid email and convert to internet address.
        if(emailStr.length() > EMAIL_MAX) {
            throw new ServiceException(ServiceStatus.APP_INVALID_EMAIL);
        }
        InternetAddress emailAddr = null;
        try {
            emailAddr = new InternetAddress(emailStr, /*Strict:*/ true);
            emailAddr.validate();
        } catch(AddressException e) {
            throw new ServiceException(ServiceStatus.APP_INVALID_EMAIL);
        }

        // Hash password.
        password = OMUtil.sha256(password);

        // Insert user query.
        Date joinDate = new Date();
        int uid = UserTable.insertUser(sql, username, password,
                                       firstName, lastName, joinDate,
                                       emailAddr);

        // User role query.
        // NOTE: if you remove this line of code, you will break lookup which
        // assumes every user has at least one role.
        /*UserHasRoleTable.insertUserHasRole(sql,
                                           uid,
                                           UserRoleTable.ROLE_USER_ID);*/
        
        return new User(uid, username, password, firstName, lastName,
                        joinDate, emailStr);
    }

    /**
     * Creates a new User OM object. Constructor is private because User
     * objects can only be created internally from calls to create().
     * @param username The user's username.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     */
    private User(int uid, String username, String passwordHash,
                 String firstName, String lastName,
                 Date joinDate, String email) {
        this.uid = uid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.email = email;
        // this.roles = new HashSet<Role>();
    }
}
