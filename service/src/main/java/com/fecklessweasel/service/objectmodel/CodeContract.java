package com.fecklessweasel.service.objectmodel;

/**
 * Validation routines for function contracts. Checks for things
 * like null objects and invalid string contents and throws exceptions
 * if needed.
 * @author Christian Gunderman
 */
public abstract class CodeContract {
    /**
     * Checks that the given object is not null.
     * @param obj The object to check.
     * @param argName The argument name of the object.
     * @throws IllegalArgumentException The argument is null.
     */
    public static void assertNotNull(Object obj, String argName) {
        if (obj == null) {
            throw new IllegalArgumentException(argName + " cannot be null.");
        }
    }

    /**
     * Checks that the given String is not null, empty, or only whitespace.
     * @param str The String.
     * @param argName The argument is null, empty, or whitespace.
     */
    public static void assertNotNullOrEmptyOrWhitespace(String str,
                                                        String argName) {
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            throw new IllegalArgumentException(argName
                + " cannot be null, empty, or whitespace.");
        }
    }
}
