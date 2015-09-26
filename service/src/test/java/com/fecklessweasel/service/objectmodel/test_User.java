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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullFirstName_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        null,
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_NullLastName_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Christian",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "haha_nice_try",
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASSWORD, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_FirstNameTooShort_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "C",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_FirstNameTooLong_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_FirstNameWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Chris tian",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_FirstNameWithSymbols_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Chris$tian",
                        "Gunderman",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_LastNameTooShort_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Christian",
                        "G",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_LastNameTooLong_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Christian",
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_LastNameWithSpaces_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Christian",
                        "Gun der man",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_create_LastNameWithSymbols_Exception() throws Exception {
        try {
            User.create(mock(Connection.class),
                        "gundermanc",
                        "haha_nice_try",
                        "Christian",
                        "Gunderm@n",
                        "gundermanc@gmail.com");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_NAME, ex.status);
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
                        "Christian",
                        "Gunderman",
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
