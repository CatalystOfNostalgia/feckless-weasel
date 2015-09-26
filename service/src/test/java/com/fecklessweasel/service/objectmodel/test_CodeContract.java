package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for CodeContract class functions.
 */
public class test_CodeContract {
    
    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNull_NullArgName_ExceptionThrown() throws Exception {
        CodeContract.assertNotNull("my string", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNull_EmptyArgName_ExceptionThrown() throws Exception {
        CodeContract.assertNotNull("my string", "");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNull_NullObj_ExceptionThrown() throws Exception {
        CodeContract.assertNotNull(null, "argName");
    }

    @Test
    public void test_assertNotNull_NotNullObj_NoExceptionThrown() throws Exception {
        // Empty string is still valid for assertNotNull.
        CodeContract.assertNotNull("", "argName");
    }
 
    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNullOrEmptyOrWhitespace_NullArgName_ExceptionThrown() throws Exception {
        CodeContract.assertNotNullOrEmptyOrWhitespace("my string", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNullOrEmptyOrWhitespace_EmptyArgName_ExceptionThrown() throws Exception {
        CodeContract.assertNotNullOrEmptyOrWhitespace("my string", "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNullOrEmptyOrWhitespace_EmptyStr_ExceptionThrown() throws Exception {
        CodeContract.assertNotNullOrEmptyOrWhitespace("", "argName");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_assertNotNullOrEmptyOrWhitespace_WhitespaceStr_ExceptionThrown() throws Exception {
        CodeContract.assertNotNullOrEmptyOrWhitespace("     ", "argName");
    }

    @Test
    public void test_assertNotNullOrEmptyOrWhitespace_GoodInput_ExceptionThrown() throws Exception {
        CodeContract.assertNotNullOrEmptyOrWhitespace("my string", "argName");
    }   
}
