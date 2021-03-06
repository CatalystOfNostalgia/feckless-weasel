package com.fecklessweasel.service.objectmodel;

/**
 * ServiceStatus and error codes and messages.
 * @author Christian Gunderman
 */
public enum ServiceStatus {

    /*
     * DANGER!!!!
     * Make sure that the first param for each value is UNIQUE when merging.
     */

    // Success codes.
    OK(200, 200, "Success."),
    CREATED(201, 201, "Created."),
    DELETED(225, 200, "Deleted."),

    // Client errors.
    MALFORMED_REQUEST(400, 400, "Server received malformed request."),
    INVALID_SESSION_HEADER(401, 400, "Malformed request. Invalid session header."),
    INVALID_SESSION(402, 400, "Invalid session."),
    JSON_DS_ERROR(403, 400, "Unable to deserialize JSON request."),
    NOT_FOUND(404, 404, "Server resource not found."),
    INVALID_GENDER(405, 400, "Invalid gender. Gender must be MALE or FEMALE."),
    NOT_AUTHENTICATED(406, 401, "Must log in to access this resource."),

    // Server errors.
    UNKNOWN_ERROR(500, 500, "Unknown error occurred."),
    SERVER_UPLOAD_ERROR(501, 500, "Upload to server failed."),
    SERVER_DELETE_ERROR(502, 500, "Deleting file from server failed."),
    DATABASE_ERROR(503, 500, "Error processing database request."),
    INSTALL_ERROR(504, 500, "Incorrect server configuration."),
    NO_SQL(505, 500, "No SQL instance provided."),
    ACCESS_DENIED(506, 403, "Access denied."),
    FILE_READ_ERROR(507, 500, "Reading file from server failed."),
    INVALID_FILE_TYPE(508, 500, "Invalid file type for attempted operation."),

    // App logic errors.
    APP_INVALID_USER_LENGTH(701, 400, "Username is either too short or too long."),
    APP_INVALID_PASS_LENGTH(702, 400, "Password is too short."),
    APP_INVALID_BIRTHDAY(703, 400, "Birthday is in the future."),
    APP_USERNAME_TAKEN(704, 400, "A user with this username already exists."),
    APP_USER_NOT_EXIST(705, 400, "The requested user does not exist."),
    APP_INVALID_EMAIL(706, 400, "Email address is invalid."),
    APP_INVALID_USERNAME(707, 400, "Username contains invalid character(s)."),
    APP_INVALID_PASSWORD(708, 403, "Invalid password."),
    APP_EVENT_NOT_EXIST(709, 400, "No events found with given id."),
    APP_INVALID_EVENT_NAME_LENGTH(710, 400, "Event name is either too short or too long."),
    APP_INVALID_EVENT_LOC_LENGTH(711, 400, "Event location is either too short or too long."),
    APP_INVALID_EVENT_DATE(712, 400, "Event date is in the past."),
    APP_INVALID_ROLE_LENGTH(707, 400, "Invalid Role ID length."),
    APP_INVALID_ROLE(708, 400, "Invalid Role ID. Must start with ROLE_"),
    APP_INVALID_ROLE_DESCRIPTION_LENGTH(709, 400, "Invalid Role description length."),
    APP_ROLE_ID_TAKEN(710, 400, "Can't add Role. Role already exists."),
    APP_USER_HAS_ROLE_DUPLICATE(711, 400, "User already has Role, or Role doesn't exist."),
    APP_USER_NOT_HAVE_ROLE(712, 400, "User does not have Role."),
    APP_ROLE_NOT_EXIST(713, 400, "Role does not exist."),
    APP_INVALID_NAME(714, 400, "First name or last name is too short or too long."),

    APP_INVALID_UNIV_NAME_LENGTH(715, 400, "University name is too long or too short."),
    APP_INVALID_UNIV_NAME_CHARS(716, 400, "University name contains invalid characters."),
    APP_INVALID_UNIV_ACRONYM_LENGTH(717, 400, "University acronym is too long or too short."),
    APP_INVALID_ACRONYM_CHARS(718, 400, "University acronym contains invalid characters."),
    APP_INVALID_CITY_LENGTH(719, 400, "University city name is too long or too short."),
    APP_INVALID_CITY_CHARS(720, 400, "University city name contains invalid characters."),
    APP_INVALID_STATE(721, 400, "University state is invalid."),
    APP_INVALID_COUNTRY_LENGTH(722, 400, "University country name is too long or too short."),
    APP_INVALID_COUNTRY_CHARS(723, 400, "University country name contains invalid characters."),
    APP_INVALID_DEPTNAME_LENGTH(724, 400, "Department name is too long or too short."),
    APP_INVALID_DEPTNAME_CHARS(725, 400, "Department name contains invalid characters."),
    APP_INVALID_DEPT_ACRONYM(726, 400, "Department acronym is invalid."),
    APP_INVALID_COURSE_NUMBER(727, 400, "Class number in invalid."),
    APP_COURSE_NOT_EXIST(728, 400, "Course does not exist."),
    APP_FILE_NOT_EXIST(729, 400, "The requested file does not exist"),
    APP_DEPARTMENT_NOT_EXIST(730, 400, "Department does not exist."),
    APP_INVALID_TITLE_LENGTH(731, 400, "Title is either too long or too short."),
    APP_INVALID_DESCRIPTION_LENGTH(732, 400, "Title is either too long or too short."),
    APP_COURSE_TAKEN(733, 400, "Course already exists."),
    APP_DEPT_TAKEN(734, 400, "Department already exists."),
    APP_UNIV_TAKEN(735, 400, "University already exists."),
    APP_RATING_TAKEN(736, 400, "Rating already exists."),
    APP_COMMENT_TAKEN(737, 400, "Comment already exists."),
    APP_INVALID_RATING(738, 400, "Rating is too high or too low."),
    APP_INVALID_COMMENT_TEXT(739, 400, "Comment is too long or too short."),
    APP_INVALID_ROLE_DESC_LENGTH(740, 400, "Description is too long."),
    APP_INVALID_EXTENSION_LENGTH(741, 400, "File extension is either too long or too short."),
    APP_COURSE_NAME_INVALID(742, 400, "Course name is either too long or too short.");


    /** The String name of the state (OK, MALFORMED_REQUEST, ...) */
    public final String status;
    /** The integer code for this error or state. */
    public final int code;
    /** The http code that should be returned from this error. */
    public final int httpCode;
    /** The String message explaining this error. */
    public final String message;

    /**
     * Constructor for error messages and status codes.
     * @param code The unique integer error code.
     * @param httpCode One of the standardized http return codes.
     * @param message The message for this error or state.
     */
    ServiceStatus(int code, int httpCode, String message) {
        this.status = this.name();
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
    }
}
