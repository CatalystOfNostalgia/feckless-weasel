package com.fecklessweasel.service.objectmodel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Object Model Utility functions.
 * @author Christian Gunderman
 */
public final class OMUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private OMUtil() { }

    /**
     * Checks to make sure SQLConnection is non-null.
     * @throws ServiceException If value is null.
     */
    public static void sqlCheck(Connection value) throws ServiceException {
      if (value == null) {
            throw new ServiceException(ServiceStatus.NO_SQL);
        }
    }

    /**
     * Checks object for null value.
     * @throws ServiceException with MALFORMED_REQUEST if value == null.
     */
    public static void nullCheck(Object value) throws ServiceException {
        if (value == null) {
            throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
        }
    }

    /**
     * Checks the string for any unaccepted characters.
     * Allowed: a-z, A-Z, 0-9, _
     * @param str The string to check.
     * @return true if there are no invalid characters in the string.
     */
    public static boolean isValidInput(String str) {
        if (str != null && str.matches("^[a-zA-Z0-9_]*$")){
            return true;
        }

        return false;
    }

    /**
     * Parses the String to an integer value.
     * @param value
     * @throws ServiceException Thrown with MALFORMED_REQUEST
     * if value is not an integer or is null.
     * @return Value as an integer.
     */
    public static int parseInt(String value) throws ServiceException {
        OMUtil.nullCheck(value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new ServiceException(ServiceStatus.MALFORMED_REQUEST);
        }
    }

    /**
     * Checks the string for any unaccepted characters.
     * Allowed: a-z, A-Z, spaces
     * @param str The string to check.
     * @return true if there are no invalid characters in the string.
     */
    public static boolean isValidName(String str) {
        if (str != null && str.trim().equals(str) && str.matches("^[a-zA-Z ]*$")){
            return true;
        }

        return false;
    }

    /**
     * Checks to make sure current User is resource owner or an
     * admin.
     * @throws ServiceException With ACCESS_DENIED if not resource owner
     * or admin.
     */
    public static void adminOrOwnerCheck(User current,
                                         User resourceOwner)
        throws ServiceException {

        if (!current.equals(resourceOwner) &&
            !current.isRole("ROLE_ADMIN")) {
            throw new ServiceException(ServiceStatus.ACCESS_DENIED);
        }
    }

    /**
     * Gets sha256 hash of a string. Useful for password storage.
     * @throws ServiceException With UNKNOWN_ERROR if SHA-256 isn't a supported
     * algorithm on this platform.
     * @param data The string to hash.
     * @return The hashed string.
     */
    public static String sha256(String data) throws ServiceException {
        CodeContract.assertNotNull(data, "data");

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new ServiceException(ServiceStatus.UNKNOWN_ERROR, ex);
        }
    }

    /**
     * Converts an arry of bytes to a String of hex characters.
     * @param bytes Bytes to convert.
     * @return A hex string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }
}
