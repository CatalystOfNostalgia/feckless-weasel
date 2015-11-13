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

import com.fecklessweasel.service.datatier.UserTable;
import com.fecklessweasel.service.datatier.UserHasRoleTable;
import com.fecklessweasel.service.datatier.UserRoleTable;
import com.fecklessweasel.service.objectmodel.OMUtil;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for object model User.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserTable.class, UserHasRoleTable.class, UserRoleTable.class})
public class test_User {
    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        PowerMockito.mockStatic(UserTable.class);
        PowerMockito.mockStatic(UserHasRoleTable.class);
        PowerMockito.mockStatic(UserRoleTable.class);
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
                        "haha_nice_try",
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

    @Test
    public void test_create_Success() throws Exception {
        PowerMockito.when(UserTable.insertUser(this.mockConnection,
                                  "gundermanc",
                                  "haha_nice_try",
                                  new Date(),
                                  new InternetAddress("gundermanc@gmail.com"))).thenReturn(1);

        PowerMockito.doNothing().when(UserRoleTable.class, "insertUserRole",
                                      any(Connection.class),
                                      any(String.class),
                                      any(String.class));

        // Make sure no exceptions are thrown in proper case.
        User user = User.create(this.mockConnection,
                                "gundermanc",
                                "haha_nice_try",
                                "gundermanc@gmail.com");

        //assertEquals(1, user.getID());
        assertEquals("gundermanc", user.getUsername());
        assertEquals(OMUtil.sha256("haha_nice_try"), user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertTrue(user.isRole("ROLE_USER"));
    }

    @Test
    public void test_lookup_NullSQL_Exception() throws Exception {
        try {
            User.lookup(null,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookup_NullUser_Exception() throws Exception {
        try {
            User.lookup(mock(Connection.class),
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookup_UserNotExist() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);
        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);

        try {
            User.lookup(this.mockConnection,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_NOT_EXIST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_lookup_SQLException() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenThrow(new SQLException());
        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);
        try {
            // Make sure no exceptions are thrown in proper case.
            User user = User.lookup(this.mockConnection,
                                    "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }

        fail("no exception was thrown");
    }

    @Test
    public void test_lookup_Success() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(false);
        when(mockResultSet.getInt("uid")).thenReturn(2);
        when(mockResultSet.getString("user")).thenReturn("gundermanc");
        when(mockResultSet.getString("pass")).thenReturn("haha_nice_try");
        when(mockResultSet.getDate("join_date")).thenReturn(date);
        when(mockResultSet.getString("email")).thenReturn("gundermanc@gmail.com");
        when(mockResultSet.getString("role")).thenReturn(UserRoleTable.ROLE_USER_ID);
        when(mockResultSet.getString("description")).thenReturn(UserRoleTable.ROLE_USER_DESCRIPTION);

        PowerMockito.when(UserTable.lookupUserWithRoles(this.mockConnection,
                                                        "gundermanc")).thenReturn(mockResultSet);

        // Make sure no exceptions are thrown in proper case.
        User user = User.lookup(this.mockConnection,
                                "gundermanc");

        assertEquals("gundermanc", user.getUsername());
        assertEquals("haha_nice_try", user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertEquals(date, user.getJoinDate());
    }

    @Test
    public void test_lookupById_NullSQL_Exception() throws Exception {
        try {
            User.lookupById(null,
                            1);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_lookupById_UserNotExist() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(false);
        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            2)).thenReturn(mockResultSet);

        try {
            User.lookupById(this.mockConnection,
                            2);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_USER_NOT_EXIST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_lookupById_SQLException() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenThrow(new SQLException());
        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            1)).thenReturn(mockResultSet);
        try {
            // Make sure no exceptions are thrown in proper case.
            User user = User.lookupById(this.mockConnection,
                                    1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }

        fail("no exception was thrown");
    }

    @Test
    public void test_lookupById_Success() throws Exception {
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(false);
        when(mockResultSet.getInt("uid")).thenReturn(2);
        when(mockResultSet.getString("user")).thenReturn("gundermanc");
        when(mockResultSet.getString("pass")).thenReturn("haha_nice_try");
        when(mockResultSet.getDate("join_date")).thenReturn(date);
        when(mockResultSet.getString("email")).thenReturn("gundermanc@gmail.com");
        when(mockResultSet.getString("role")).thenReturn(UserRoleTable.ROLE_USER_ID);
        when(mockResultSet.getString("description")).thenReturn(UserRoleTable.ROLE_USER_DESCRIPTION);

        PowerMockito.when(UserTable.lookupUserWithRolesById(this.mockConnection,
                                                            4)).thenReturn(mockResultSet);

        // Make sure no exceptions are thrown in proper case.
        User user = User.lookupById(this.mockConnection,
                                    4);

        assertEquals("gundermanc", user.getUsername());
        assertEquals("haha_nice_try", user.getPasswordHash());
        assertEquals("gundermanc@gmail.com", user.getEmail());
        assertEquals(date, user.getJoinDate());
    }
    
    @Test
    public void test_deleteStatic_NullSQL_Exception() throws Exception {
        try {
            User.delete(null,
                        "gundermanc");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_deleteStatic_NullUsername_Exception() throws Exception {
        try {
            User.delete(this.mockConnection,
                        null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.MALFORMED_REQUEST, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_deleteStatic_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class, "deleteUser",
                                      this.mockConnection, "gundermanc");
        User.delete(this.mockConnection,
                    "gundermanc");
    }

    @Test
    public void test_delete_NullSQL_Exception() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").delete(null);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.NO_SQL, ex.status);
            return;
        }

        fail("No service exception thrown");
    }

    @Test
    public void test_delete_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "deleteUser",
                                      this.mockConnection,
                                      "gundermanc");
        new User(1,
                 "gundermanc",
                 "hashes",
                 new Date(),
                 "email@gc.com").delete(this.mockConnection);
    }

    @Test
    public void test_updatePassword_NullConnection() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(null, "old_pass", "new_pass");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_NullOldPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, null, "new_pass");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_NullNewPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, "old_pass", null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_InvalidPassword() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     OMUtil.sha256("old_pass"),
                     new Date(),
                     "email@gc.com").updatePassword(this.mockConnection, "not_old_pass", "new_password");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_INVALID_PASSWORD);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updatePassword_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "updatePassword",
                                      this.mockConnection,
                                      1,
                                      OMUtil.sha256("new_password"));
        User user = new User(1,
                             "gundermanc",
                             OMUtil.sha256("old_pass"),
                             new Date(),
                             "email@gc.com");

        user.updatePassword(this.mockConnection, "old_pass", "new_password");

        assertEquals(user.getPasswordHash(), OMUtil.sha256("new_password"));
    }




















    @Test
    public void test_updateEmail_NullConnection() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updateEmail(null, "newemail@gc.com");
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.NO_SQL);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updateEmail_NullEmail() throws Exception {
        try {
            new User(1,
                     "gundermanc",
                     "hashes",
                     new Date(),
                     "email@gc.com").updateEmail(this.mockConnection, null);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.MALFORMED_REQUEST);
            return;
        }

        fail("no exception thrown");
    }

    @Test
    public void test_updateEmail_Success() throws Exception {
        PowerMockito.doNothing().when(UserTable.class,
                                      "updateEmail",
                                      this.mockConnection,
                                      1,
                                      new InternetAddress("email@gc.com"));
        User user = new User(1,
                             "gundermanc",
                             OMUtil.sha256("old_pass"),
                             new Date(),
                             "email@gc.com");

        user.updateEmail(this.mockConnection,
                         "gundermanc@gmail.com");

        assertEquals(user.getEmail(), "gundermanc@gmail.com");
    }

    @Test
    public void test_equals_True() throws Exception {
        assertEquals(new User(1,
                              "gundermanc",
                              OMUtil.sha256("old_pass"),
                              new Date(),
                              "email@gc.com"),
                     new User(1,
                              "gundermanc_different",
                              OMUtil.sha256("new_pass"),
                              new Date(),
                              "email2@gc.com"));
    }

    @Test
    public void test_equals_False() throws Exception {
        assertNotEquals(new User(3,
                                 "gundermanc",
                                 OMUtil.sha256("old_pass"),
                                 new Date(),
                                 "email@gc.com"),
                        new User(1,
                                 "gundermanc",
                                 OMUtil.sha256("old_pass"),
                                 new Date(),
                                 "email@gc.com"));
    }
}
