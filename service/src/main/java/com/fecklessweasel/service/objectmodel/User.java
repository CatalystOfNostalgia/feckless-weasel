package com.fecklessweasel.service.objectmodel;

import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fecklessweasel.service.datatier.UserTable;
import com.fecklessweasel.service.datatier.UserRoleTable;
import com.fecklessweasel.service.datatier.UserHasRoleTable;
import com.fecklessweasel.service.datatier.FavoritesTable;

/**
 * User API, used for all operations pertaining to a user and his or her
 * relations.
 * @author Christian Gunderman
 */
public final class User {
    /** Minimum user account length. */
    public static final int USER_MIN = 6;
    /**
     * Maximum user account length. You MUST make sure that this value is less
     * than or equal to the column width in UserTable.
     */
    public static final int USER_MAX = 25;
    /** Password minimum length */
    public static final int PASS_MIN = 6;
    /** Maximum password length. */
    public static final int PASS_MAX = 100;
    /** Maximum email length. */
    public static final int EMAIL_MAX = 320;

    /** Unique User id integer. */
    private int uid;
    /** Username. */
    private String username;
    /** Password hashes. */
    private String passwordHash;
    /** Date user joined. */
    private Date joinDate;
    /** User's email address. */
    private String email;
    /** User's security roles. */
    final Set<Role> roles;

    /**
     * Creates a new user and stores in the database.
     * @param sql A connection from SQLSource.
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param emailStr The email address for the new user.
     * @throws ServiceException Thrown if an error occurs.
     * @return Returns the new user object.
     */
    public static User create(Connection sql,
                              String username,
                              String password,
                              String emailStr) throws ServiceException {

        // Null check everything:
        OMUtil.sqlCheck(sql);
        OMUtil.nullCheck(username);
        OMUtil.nullCheck(password);
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

        validatePassword(password);

        InternetAddress emailAddr = validateAndCreateInternetAddress(emailStr);

        // Hash password.
        password = OMUtil.sha256(password);

        // Insert user query.
        Date joinDate = new Date();
        int uid = UserTable.insertUser(sql, username, password,
                                        joinDate, emailAddr);

        // User role query.
        // NOTE: if you remove this line of code, you will break lookup which
        // assumes every user has at least one role.
        UserHasRoleTable.insertUserHasRole(sql,
                                           uid,
                                           UserRoleTable.ROLE_USER_ID);
        User user = new User(uid, username, password, joinDate, emailStr);

        user.roles.add(new Role(UserRoleTable.ROLE_USER_ID,
                                UserRoleTable.ROLE_USER_DESCRIPTION));

        return user;
    }
    
    /**
     * Create a user with data from the database.
     * @param result The ResultSet from the database.
     * @return A user object.
     */
    protected static User fromResultSet(ResultSet result) throws ServiceException{
        try{
            return new User(result.getInt("uid"),
                result.getString("user"),
                result.getString("pass"),
                result.getDate("join_date"),
                result.getString("email"));
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up a user in the datatier and returns it as a User object.
     * @throws ServiceException With APP_USER_NOT_EXIST if user doesn't exist,
     * or another code if SQL error occurs.
     * @param connection The SQL connection.
     * @param username The username of the user to look up.
     * @return A new user object for the user.
     */
    public static User lookup(Connection connection, String username)
        throws ServiceException {

        // Null check everything:
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(username);

        ResultSet result = UserTable.lookupUserWithRoles(connection, username);

        // Build User object.
        try {
            // Get the first (and only) row or throw if user not exist.
            if (!result.next()) {
                throw new ServiceException(ServiceStatus.APP_USER_NOT_EXIST);
            }

            User user = new User(result.getInt("uid"),
                                 result.getString("user"),
                                 result.getString("pass"),
                                 result.getDate("join_date"),
                                 result.getString("email"));

            // Populate user roles.
            // NOTE: this implementation assumes that every user is AT LEAST
            // one role.
            do {
                user.roles.add(new Role(result.getString("role"),
                                        result.getString("description")));
            } while(result.next());

            result.close();
            return user;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Looks up a user by his or her user ID. Package protected intentionally.
     * Keep this method in the objectmodel unless absolutely neccessary since
     * we can and SHOULD be identifying users by their User name in the Service
     * itself.
     * @param connection A SQL Source connection.
     * @param uid The user's ID.
     * @throws ServiceException If unable to lookup or find the user.
     * @return The user.
     */
    static User lookupById(Connection connection, int uid)
        throws ServiceException {

        OMUtil.sqlCheck(connection);

        ResultSet result = UserTable.lookupUserWithRolesById(connection, uid);

        // Build User object
        try {
            // Get the first (and only) row or throw if the user doesnt exist
            if(!result.next()) {
                throw new ServiceException(ServiceStatus.APP_USER_NOT_EXIST);
            }

            User user = new User(result.getInt("uid"),
                                 result.getString("user"),
                                 result.getString("pass"),
                                 result.getDate("join_date"),
                                 result.getString("email"));

            // Populate user roles.
            // NOTE: this implementation assumes that every user is AT LEAST
            // one role.
            do {
                user.roles.add(new Role(result.getString("role"),
                                        result.getString("description")));
            } while(result.next());

            result.close();
            return user;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Deletes a user.
     * @throws ServiceException If a database error occurs.
     * @param connection The SQL connection.
     * @param username The username of the user to delete.
     */
    public static void delete(Connection connection, String username)
        throws ServiceException {

        // Null check everything:
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(username);

        // Try to delete
        UserTable.deleteUser(connection, username);
    }

    /**
     * Deletes this user.
     * @throws ServiceException If a database error occurs.
     * @param connection The SQL connection.
     */
    public void delete(Connection connection) throws ServiceException {
        OMUtil.sqlCheck(connection);
        
        User.delete(connection, this.getUsername());
    }

    /**
     * Updates the user's password in the database and updates
     * this user object.
     * @param connection A connection from SQLSource.
     * @param oldPassword The current password.
     * @param newPassword The new password.
     * @throws ServiceException Thrown if database error occurs or if
     * user does not exist or passwords do not match.
     */
    public void updatePassword(Connection connection,
                               String oldPassword,
                               String newPassword) throws ServiceException {
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(newPassword);
        OMUtil.nullCheck(oldPassword);

        String oldPasswordHash = OMUtil.sha256(oldPassword);

        // Check that the given old password matches the stored password hashes.
        if (!oldPasswordHash.equals(this.getPasswordHash())) {
            throw new ServiceException(ServiceStatus.APP_INVALID_PASSWORD);
        }

        // Check that password meets minimum criterion.
        validatePassword(newPassword);

        // Hash and store new password.
        String newPasswordHash = OMUtil.sha256(newPassword);
        UserTable.updatePassword(connection, this.getID(), newPasswordHash);

        // Update the object's state.
        this.passwordHash = newPasswordHash;
    }

    /**
     * Updates the user's email in the database and updates
     * this user object.
     * @param connection A connection from SQLSource.
     * @param newEmail The new email.
     * @throws ServiceException Thrown if database error occurs or if
     * user does not exist or passwords do not match.
     */
    public void updateEmail(Connection connection,
                            String newEmail) throws ServiceException {
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(newEmail);

        // Validate the email.
        InternetAddress emailAddr = validateAndCreateInternetAddress(newEmail);

        // Update the email in the database.
        UserTable.updateEmail(connection, this.getID(), emailAddr);

        // Update the object state.
        this.email = emailAddr.getAddress();
    }

    /**
     * Returns the trust score of this user.
     * @param conn A connection to the database.
     * @return The trust score of this user.
     */
    public double lookupTrustScore(Connection conn) throws ServiceException{
        OMUtil.sqlCheck(conn);
        double score = 0;
        Iterable<StoredFile> files = StoredFile.lookupUserFiles(conn, this.uid);
        for(StoredFile f : files){
            score += f.lookupRating(conn);
        }
        return score;
    }
    
    /**
     * Checks if two User objects refer to the same User account.
     * @param o The object to compare.
     * @return True if they are the same user.
     */
    @Override
    public boolean equals(Object o) {
        return ((User)o).getID() == this.getID();
    }

    /**
     * Gets username.
     * @return Unique username String.
     */
    public String getUsername() {
        return this.username;
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
    int getID() {
        return this.uid;
    }

    /**
     * Gets the security roles that this user is authorized for.
     * @return An unmodifiable collection of Roles.
     */
    public Collection<Role> getRoles() {
        return Collections.unmodifiableCollection(this.roles);
    }

    /**
     * Checks to see if this user is of the specified Role.
     * @return True if this user is the specified role.
     */
    public boolean isRole(String role) {
        return this.roles.contains(new Role(role, null));
    }

    /**
     * Grants this user the specified role.
     * @throws ServiceException If database error occurs or Role doesn't exist
     * or User already has given Role.
     * @param connection The database connection.
     * @param role A unique role id string.
     * @return The Role object that was added to the User's Roles.
     */
    public Role addRole(Connection connection, String role) throws ServiceException {
        // Clean inputs.
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(role);

        try {
            // Add Role to the user in the DB.
            UserHasRoleTable.insertUserHasRole(connection, this.uid, role);

            // Query Role from DB to get the description.
            ResultSet roleResult = UserRoleTable.lookupUserRole(connection,
                                                                role);

            // Check that results were returned.
            // This should be redundant, but good to have just in case.
            if (!roleResult.next()) {
                throw new ServiceException(ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE);
            }

            // Create a wrapper object.
            Role newRole = new Role(roleResult.getString("role"),
                                    roleResult.getString("description"));

            // Add wrapper object to Roles set for user.
            this.roles.add(newRole);

            return newRole;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Removes a user's security Role.
     * @throws ServiceException If user doesn't have the requested Role.
     * @param connection The database connection.
     * @param role The unique ROLE_[name] id.
     */
    public void removeRole(Connection connection, String role) throws ServiceException {

        // Clean inputs.
        OMUtil.sqlCheck(connection);
        OMUtil.nullCheck(role);

        // Remove Role from the Role set.
        Role roleToRemove = new Role(role, null);

        // Check if we have this Role first.
        if (!this.roles.contains(roleToRemove)) {
            throw new ServiceException(ServiceStatus.APP_USER_NOT_HAVE_ROLE);
        }

        // Remove Role from user in database.
        UserHasRoleTable.deleteUserHasRole(connection, this.uid, role);
    }

    /**
     * Return all Courses that the User has favorited
     * @throws ServiceException if there is a database problem
     * @param connection The database connection
     * @return List of All Courses the User has Favorited
     */
    public Iterable<Course> getFavoriteCourses(Connection connection) throws ServiceException {

        OMUtil.sqlCheck(connection);

        ResultSet results = FavoritesTable.getFavoriteCourses(connection, this.uid);
        ArrayList<Course> courses = new ArrayList<Course>();
        try {
            while (results.next()) {
                courses.add(Course.lookupById(connection, results.getInt("cid")));
            }

            results.close();
            return courses;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Toggle if this user has favorited the input course or not
     * @throws ServiceException on database error
     * @param sql Database connection
     * @param cid ID of the course being favorited
     * @return True if Course is now favorited, False if not
     */
    public boolean toggleFavoriteCourse(Connection sql, int cid) throws ServiceException {
        if (FavoritesTable.favoriteCourseExists(sql, this.uid, cid)) {
            //favorite exists, we want to delete the favorite
            FavoritesTable.deleteFavoriteCourse(sql, this.uid, cid);
            return false;
        } else {
            //favorite doesn't exist, add it
            FavoritesTable.insertFavoriteCourse(sql, this.uid, cid);
            return true;
        }
    }

    /** 
     * Determine if this user has already favorited the course
     * @throw ServiceException on database error
     * @param sql Database Connection
     * @param cid ID of Course
     * @return true if user has favorited course, false if not
     */
    public boolean checkIfFavCourse(Connection sql, int cid) throws ServiceException {
        return FavoritesTable.favoriteCourseExists(sql, this.uid, cid);
    }

    /**
     * Return all Files that the User has favorited
     * @throws ServiceException if there is a database problem
     * @param connection The database connection
     * @return List of All Files the User has Favorited
     */
    public Iterable<StoredFile> getFavoriteFiles(Connection connection) throws ServiceException {

        OMUtil.sqlCheck(connection);

        ResultSet results = FavoritesTable.getFavoriteFiles(connection, this.uid);
        ArrayList<StoredFile> files = new ArrayList<StoredFile>();
        try {
            while (results.next()) {
                files.add(StoredFile.lookup(connection, results.getInt("fid")));
            }

            results.close();
            return files;
        } catch (SQLException ex) {
            throw new ServiceException(ServiceStatus.DATABASE_ERROR, ex);
        }
    }

    /**
     * Toggle if this user has favorited the input file or not
     * @throws ServiceException on database error
     * @param sql Database connection
     * @param fid ID of the File being favorited
     * @return True if file is now favorited, False if not
     */
    public boolean toggleFavoriteFile(Connection sql, int fid) throws ServiceException {
        if (FavoritesTable.favoriteFileExists(sql, this.uid, fid)) {
            //favorite exists, we want to delete the favorite
            FavoritesTable.deleteFavoriteFile(sql, this.uid, fid);
            return false;
        } else {
            //favorite doesn't exist, add it
            FavoritesTable.insertFavoriteFile(sql, this.uid, fid);
            return true;
        }
    }

    /**
     * Get all notes belonging to this user
     * @param sql Database connection
     * @return List of all notes belonging to this user 
     */
    public Iterable<StoredFile> getNotes(Connection sql) throws ServiceException {
        return StoredFile.lookupUserNotes(sql, this.uid);
    }

    /** 
     * Determine if this user has already favorited the file
     * @throw ServiceException on database error
     * @param sql Database Connection
     * @param fid ID of File
     * @return true if user has favorited file, false if not
     */
    public boolean checkIfFavFile(Connection sql, int fid) throws ServiceException {
        return FavoritesTable.favoriteFileExists(sql, this.uid, fid);
    }

    /**
     * Creates a new User OM object. Constructor is private because User
     * objects can only be created internally from calls to create().
     * @param uid The user's ID in the DB.
     * @param username The user's username.
     * @param password The user's password.
     * @param joinDate The date that the user joined.
     * @param email The user's email address.
     */
    User(int uid, String username, String passwordHash,
                 Date joinDate, String email) {
        this.uid = uid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.joinDate = joinDate;
        this.email = email;
        this.roles = new HashSet<Role>();
    }

    /**
     * Checks that a password matches the required criterion.
     * @param password The new password.
     * @throws ServiceException Thrown if password is invalid.
     */
    private static void validatePassword(String password)
        throws ServiceException {
        
        // Check password length.
        if (password.length() < PASS_MIN) {
            throw new ServiceException(ServiceStatus.APP_INVALID_PASS_LENGTH);
        }
        // Check for spaces.
        if (password.contains(" ")){
            throw new ServiceException(ServiceStatus.APP_INVALID_PASSWORD);
        }
    }

    /**
     * Validates the email address and turns it into an InternetAddress object.
     * Validation is based solely upon format.
     * @param email The email to validate.
     * @throws ServiceException Thrown if the email address is of an invalid format.
     * @return The InternetAddress containing the email address.
     */
    private static InternetAddress validateAndCreateInternetAddress(String email)
        throws ServiceException {

        // Check for valid email and convert to internet address.
        // REGEX from: http://www.mkyong.com/regular-expressions/
        // how-to-validate-email-address-with-regular-expression/
        if (email.length() > EMAIL_MAX ||
            !email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                              "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            throw new ServiceException(ServiceStatus.APP_INVALID_EMAIL);
        }

        InternetAddress emailAddr = null;
        try {
            emailAddr = new InternetAddress(email, /*Strict:*/ true);
            emailAddr.validate();
        } catch(AddressException e) {
            throw new ServiceException(ServiceStatus.APP_INVALID_EMAIL);
        }

        return emailAddr;
    }

    /**
     * A Security Role.
     * Get a user's Roles with getRoles() or isRole().
     * @author Christian Gunderman
     */
    public static class Role {
        /** Minimum length for a Role id. */
        private static final int  ROLE_MIN = 7;
        /** Maximum length for a Role id. */
        private static final int ROLE_MAX = 25;
        /** Max length for a description. */
        private static final int DESCRIPTION_MAX = 255;
        /** Beginning of all ROLE ids. */
        private static final String ROLE_PREFIX = "ROLE_";

        /** The unique role id. */
        public final String id;
        /** The role description message. */
        public final String description;

        /**
         * Creates a new Role and stores in the database.
         * @throws ServiceException If database error occurs or a role with
         * the given id already exists.
         * @param connection The SQL connection.
         * @param role The unique role id string ROLE_[name] with [name] at
         * least 2 characters, no more than 20.
         * @param description The 255 character description of the role.
         * @return The created Role.
         */
        public static Role create(Connection connection,
                                  String role,
                                  String description) throws ServiceException {

            // Clean inputs.
            OMUtil.sqlCheck(connection);
            OMUtil.nullCheck(role);
            OMUtil.nullCheck(description);

            // Check Role string length.
            if (role.length() < ROLE_MIN || role.length() > ROLE_MAX) {
                throw new ServiceException(ServiceStatus.APP_INVALID_ROLE_LENGTH);
            }

            if (description.length() > DESCRIPTION_MAX) {
                throw new ServiceException(ServiceStatus.APP_INVALID_ROLE_DESC_LENGTH);
            }

            // Check Role prefix.
            if (!role.startsWith(ROLE_PREFIX)) {
                throw new ServiceException(ServiceStatus.APP_INVALID_ROLE);
            }

            // Create Role in database.
            UserRoleTable.insertUserRole(connection, role, description);

            return new Role(role, description);
        }

        /**
         * Deletes the Role identified by the given Role id.
         * @throws ServiceException If database error or the Role doesn't exist.
         * @param connection The connection to the SQL database.
         * @param Unique Role id ROLE_[name].
         */
        public static void delete(Connection connection,
                                  String role) throws ServiceException {
            // Clean inputs.
            OMUtil.sqlCheck(connection);
            OMUtil.nullCheck(role);

            UserRoleTable.deleteUserRole(connection, role);
        }

        /**
         * Checks to see if this role is equalivalent to another based upon
         * their id.
         * @return True if the same role, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            return this.id.equals(((Role)o).id);
        }

        /**
         * Hashes the Role based upon its id.
         * @return A semi-unique hash integer.
         */
        @Override
        public int hashCode() {
            return this.id.hashCode();
        }

        /**
         * Creates a new Role object. Only used inside of User class. No public
         * construction.
         * @param id The unique role id.
         * @param description The role description string.
         */
        Role(String id, String description) {
            this.id = id;
            this.description = description;
        }
    }
}
