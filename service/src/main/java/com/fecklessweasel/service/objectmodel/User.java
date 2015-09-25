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
    private long uid;
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
        if(!username.matches("^[a-zA-Z0-9_]*$")){
            throw new ServiceException(ServiceStatus.APP_INVALID_USERNAME);
        }

        // Check password length.
        if (password.length() < PASS_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_PASS_LENGTH);
        }
        // Check for spaces.
        if (password.contains(" ")){
            throw new ServiceException(ServiceStatus.APP_INVALID_PASSWORD);
        }

        // Check name.
        if (firstName.length() > FIRST_NAME_MAX ||
            firstName.length() < FIRST_NAME_MIN ||
            !firstName.matches("^[a-zA-z]*$") ||
            lastName.length() > LAST_NAME_MAX ||
            lastName.length() < LAST_NAME_MIN ||
            !lastName.matches("^[a-zA-z]*$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_NAME);
        }

        // Check for valid email and convert to internet address.
        // REGEX from: http://www.mkyong.com/regular-expressions/
        // how-to-validate-email-address-with-regular-expression/
        if (emailStr.length() > EMAIL_MAX ||
            !emailStr.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                              "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
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
        long uid = UserTable.insertUser(sql, username, password,
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
     * Gets username.
     * @return Unique username String.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets this user's first name.
     * @return First name.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gets this user's last name.
     * @return Last name.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Gets date user joined the system.
     * @return Joined date.
     */
    public Date getJoinDate() {
        return this.joinDate;
    }

    /**
     * Gets the user's email address.
     * @return The user's email address.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the user's password hash. This method is intentionally
     * package protected to keep password hashes from leaving the objectmodel.
     * @return The SHA256 hashed user password.
     */
    String getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * Gets the user's table UID. This method is intentionally package
     * protected.
     * @return The user's AUTO_INCREMENT id.
     */
    long getUid() {
        return this.uid;
    }

    /**
     * Creates a new User OM object. Constructor is private because User
     * objects can only be created internally from calls to create().
     * @param username The user's username.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     */
    private User(long uid, String username, String passwordHash,
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
