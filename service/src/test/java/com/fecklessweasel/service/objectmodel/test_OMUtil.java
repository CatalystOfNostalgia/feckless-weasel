package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for OMUtil class functions.
 */
public class test_OMUtil {
    
    @Test
    public void test_sqlCheck_NotNullConnection_NoException() throws Exception {
        OMUtil.sqlCheck(mock(Connection.class));
    }

    @Test(expected = ServiceException.class)
    public void test_sqlCheck_NullConnection_Exception() throws Exception {
        OMUtil.sqlCheck(null);
    }

    @Test
    public void test_nullCheck_NotNullConnection_NoException() throws Exception {
        OMUtil.nullCheck(new Object());
    }

    @Test(expected = ServiceException.class)
    public void test_nullCheck_NullConnection_Exception() throws Exception {
        OMUtil.nullCheck(null);
    }

    @Test
    public void test_isValidInput_NullString_False() throws Exception {
        assertFalse(OMUtil.isValidInput(null));
    }

    @Test
    public void test_isValidInput_EmptyString_True() throws Exception {
        assertTrue(OMUtil.isValidInput(""));
    }

    @Test
    public void test_isValidInput_StringWithSpaces_False() throws Exception {
        assertFalse(OMUtil.isValidInput("A B"));
    }

    @Test
    public void test_isValidInput_StringWithUnderScores_True() throws Exception {
        assertTrue(OMUtil.isValidInput("A_b"));
    }

    @Test
    public void test_isValidInput_Good_True() throws Exception {
        assertTrue(OMUtil.isValidInput("A_b_C_D"));
    }

    @Test
    public void test_isValidInput_Symbols_False() throws Exception {
        assertFalse(OMUtil.isValidInput("A_b/C_D"));
    }

    @Test
    public void test_adminOrOwnerCheck() throws Exception {
        fail("Not implemented");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_sha256_NullData_Exception() throws Exception {
        OMUtil.sha256(null);
    }

    @Test
    public void test_sha256_EmptyData_ExpectedResult() throws Exception {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                     OMUtil.sha256(""));
    }

    @Test
    public void test_sha256_HelloWorldData_ExpectedResult() throws Exception {
        assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e",
                     OMUtil.sha256("Hello World"));
    }
}
