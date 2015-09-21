package com.fecklessweasel.service.objectmodel;

/**
 * ServiceException for errors originating on the server side.
 * @author Christian Gunderman
 */
public class ServiceException extends Exception {

    /** Application status code. */
    public final ServiceStatus status;

    /**
     * Creates a new Exception.
     * @param status The application status that is representative of the
     * error.
     */
    public ServiceException(ServiceStatus status) {
        this(status, null);
    }

    /**
     * Creates a new Exception.
     * @param status The application status that is representative of the
     * error.
     * @param cause The exception that caused this exception.
     */
    public ServiceException(ServiceStatus status, Throwable cause) {
        super(status.message, cause);
        this.status = status;
    }

    /**
     * Gets string representation of this exception.
     * @return The enum name of the error associated with this exception.
     */
    @Override
    public String toString() {
        return this.status.status;
    }
}
