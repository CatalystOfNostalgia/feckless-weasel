package com.fecklessweasel.service.objectmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.UUID;

import com.fecklessweasel.service.datatier.UserSessionTable;
import com.fecklessweasel.service.objectmodel.OMUtil;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for object model User.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({User.class, UserSessionTable.class})
public class test_UserSession {
    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        PowerMockito.mockStatic(UserSessionTable.class);
        PowerMockito.mockStatic(User.class);
    }

    @Test
    public void test_start_NullConnection() throws Exception {
        try {
            UserSession.start(null,
                              "gundermanc",
                              "haha_nice_try");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_start_NullUsername() throws Exception {
        try {
            UserSession.start(this.mockConnection,
                              null,
                              "haha_nice_try");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_start_NullPassword() throws Exception {
        try {
            UserSession.start(this.mockConnection,
                              "gundermanc",
                              null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_start_InvalidPassword() throws Exception {
        PowerMockito.doReturn(new User(1,
                                       "gundermanc",
                                       OMUtil.sha256("my_pass"),
                                       new Date(),
                                       "email@gc.com"))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        try {
            UserSession.start(this.mockConnection,
                              "gundermanc",
                              "not_my_pass");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_INVALID_PASSWORD, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_start_Success() throws Exception {
        PowerMockito.doReturn(new User(1,
                                       "gundermanc",
                                       OMUtil.sha256("my_pass"),
                                       new Date(),
                                       "email@gc.com"))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        PowerMockito.doNothing().when(UserSessionTable.class,
                                      "insertSession",
                                      any(Connection.class),
                                      any(Integer.class),
                                      any(UUID.class));
        UserSession session = UserSession.start(this.mockConnection,
                                                "gundermanc",
                                                "my_pass");

        assertEquals(session.getUser().getID(), 1);
    }

    @Test
    public void test_resumeFromSessionString_NullConnection() throws Exception {
        try {
            UserSession.resumeFromSessionString(null,
                                                "gundermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString_NullHeader() throws Exception {
        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.ACCESS_DENIED, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString_NoHeaderDelimiter() throws Exception {
        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                "gundermanc8d4765ac-8a16-11e5-af63-feff819cdc9f");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.INVALID_SESSION_HEADER, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString_TooManyDelimiters() throws Exception {
        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                "gun!dermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.INVALID_SESSION_HEADER, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString_InvalidUUID() throws Exception {

        PowerMockito.doReturn(new User(1,
                                       "gundermanc",
                                       OMUtil.sha256("my_pass"),
                                       new Date(),
                                       "email@gc.com"))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                "gundermanc!8d4765ac-8a16-11e5-a");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString() throws Exception {

        PowerMockito.doThrow(new ServiceException(ServiceStatus.APP_USER_NOT_EXIST))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                "gundermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.INVALID_SESSION_HEADER, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_resumeFromSessionString_InvalidSession() throws Exception {

        PowerMockito.doReturn(new User(1,
                                       "gundermanc",
                                       OMUtil.sha256("my_pass"),
                                       new Date(),
                                       "email@gc.com"))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        PowerMockito.when(UserSessionTable.sessionExists(this.mockConnection,
                                                         1,
                                                         UUID.fromString("8d4765ac-8a16-11e5-af63-feff819cdc9f")))
            .thenReturn(false);

        try {
            UserSession.resumeFromSessionString(this.mockConnection,
                                                "gundermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.INVALID_SESSION, ex.status);
            return;
        }
    }

    @Test
    public void test_resumeFromSessionString_Success() throws Exception {

        PowerMockito.doReturn(new User(1,
                                       "gundermanc",
                                       OMUtil.sha256("my_pass"),
                                       new Date(),
                                       "email@gc.com"))
            .when(User.class, "lookup", this.mockConnection, "gundermanc");

        PowerMockito.when(UserSessionTable.sessionExists(this.mockConnection,
                                                         1,
                                                         UUID.fromString("8d4765ac-8a16-11e5-af63-feff819cdc9f")))
            .thenReturn(true);

        UserSession session = UserSession.resumeFromSessionString(this.mockConnection,
                                                "gundermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");

        assertEquals(session.getSessionString(), "gundermanc!8d4765ac-8a16-11e5-af63-feff819cdc9f");
    }

    @Test
    public void test_endAllStatic_NullConnection() throws Exception {
        try {
            UserSession.endAll(null,
                               "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_endAllStatic_NullUsername() throws Exception {
        try {
            UserSession.endAll(this.mockConnection,
                               null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_endAllStatic_Success() throws Exception {
        PowerMockito.doNothing()
            .when(UserSessionTable.class, "deleteAllSessions",
                  this.mockConnection, "gundermanc");

        UserSession.endAll(this.mockConnection,
                           "gundermanc");
    }

    @Test
    public void test_endAll_NullConnection() throws Exception {
        try {
            UserSession session = new UserSession(UUID.randomUUID(),
                                                  new User(1,
                                                           "gundermanc",
                                                           OMUtil.sha256("my_pass"),
                                                           new Date(),
                                                           "email@gc.com"));

            session.endAll(null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_endAll_Success() throws Exception {
        PowerMockito.doNothing()
            .when(UserSessionTable.class, "deleteAllSessions",
                  this.mockConnection, 1);
        UserSession session = new UserSession(UUID.randomUUID(),
                                              new User(1,
                                                       "gundermanc",
                                                       OMUtil.sha256("my_pass"),
                                                       new Date(),
                                                       "email@gc.com"));

        session.endAll(this.mockConnection);
    }
}
