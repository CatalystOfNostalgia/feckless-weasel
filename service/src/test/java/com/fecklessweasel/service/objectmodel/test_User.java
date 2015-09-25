package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.mail.internet.InternetAddress;
import java.util.Date;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for object model User.
 */
public class test_User {
    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test
    public void test_create_NullSQL_Exception() throws Exception {
        try {
            User.create(null,
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        null,
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullPassword_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        null,
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }


    @Test
    public void test_create_NullEmail_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_TooShortUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunde",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USER_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_TooLongUser_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZA",
                        "haha_nice_try",.
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USER_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_UserWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunderman c",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USERNAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_UserWithSymbols_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gunderman$c",
                        "haha_nice_try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_USERNAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_PasswordTooShort_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "12345",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASS_LENGTH, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_PasswordWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha nice try",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASSWORD, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@gmail.");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail2_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail3_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@sfsf@sfsf.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_MalformedEmail4_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "gundermanc@com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_EmailTooLong_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMN@PQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "ABCDE.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_EMAIL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }
}
