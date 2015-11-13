package com.fecklessweasel.service.datatier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.mail.internet.InternetAddress;
import java.util.Date;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for UserTable validation and exception handling. Does
 * not validate SQL queries.
 */
public class test_UserTable {
    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullConnection_Exception() throws Exception {
        UserTable.insertUser(null,
                             "user",
                             "pass",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullUser_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             null,
                             "pass",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullPass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             null,
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullDate_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             null,
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_NullEmail_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "pass",
                             new Date(),
                             null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_EmptyPass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_InsertUser_WhitespacePass_Exception() throws Exception {
        UserTable.insertUser(this.mockConnection,
                             "user",
                             "  ",
                             new Date(),
                             new InternetAddress("gundermanc@gmail.com"));
    }

    @Test
    public void test_InsertUser_SQLConstraintException_ServiceException() throws Exception {
        // Throw exception when insertUser called next:
        when(mockConnection.prepareStatement(UserTable.INSERT_USER_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            UserTable.insertUser(this.mockConnection,
                                 "user",
                                 "pass",
                                 new Date(),
                                 new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.APP_USERNAME_TAKEN);
        }
    }

    @Test
    public void test_InsertUser_SQLException_ServiceException() throws Exception {
        // Throw exception when insertUser called next:
        when(mockConnection.prepareStatement(UserTable.INSERT_USER_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenThrow(new SQLException());

        try {
            UserTable.insertUser(this.mockConnection,
                                 "user",
                                 "pass",
                                 new Date(),
                                 new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_InsertUser_SuccessCase() throws Exception {
        final int id = 12345;

        // Throw exception when insertUser called next:
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getInt(1)).thenReturn(id);

        when(mockPreparedStatement.getGeneratedKeys())
            .thenReturn(mockResultSet);

        when(mockConnection.prepareStatement(UserTable.INSERT_USER_QUERY,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenReturn(mockPreparedStatement);


        assert(UserTable.insertUser(this.mockConnection,
                                    "user",
                                    "pass",
                                    new Date(),
                                    new InternetAddress("gundermanc@gmail.com")) == id);

        // Verify next was called once to get the generated key.
        verify(mockResultSet, times(1)).next();

        // Verify statement was closed.
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserWithRoles_NullConnection() throws Exception {
        UserTable.lookupUserWithRoles(null,
                                      "username");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserWithRoles_NullUsername() throws Exception {
        UserTable.lookupUserWithRoles(this.mockConnection,
                                      null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserWithRoles_EmptyUsername() throws Exception {
        UserTable.lookupUserWithRoles(this.mockConnection,
                                      "");
    }

    @Test
    public void test_LookupUserWithRoles_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserTable.LOOKUP_USER_QUERY))
            .thenThrow(new SQLException());

        try {
            UserTable.lookupUserWithRoles(this.mockConnection,
                                          "username");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_LookupUserWithRoles_SuccessCase() throws Exception {
        final String user = "username";
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(UserTable.LOOKUP_USER_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        assert(UserTable.lookupUserWithRoles(this.mockConnection,
                                             user) == mockResultSet);

        verify(mockPreparedStatement, times(1)).setString(1, user);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUser_NullConnection() throws Exception {
        UserTable.deleteUser(null,
                             "username");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUser_NullUsername() throws Exception {
        UserTable.deleteUser(this.mockConnection,
                             null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUser_EmptyUsername() throws Exception {
        UserTable.deleteUser(this.mockConnection,
                             "");
    }

    @Test
    public void test_DeleteUser_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserTable.DELETE_USER_QUERY))
            .thenThrow(new SQLException());

        try {
            UserTable.deleteUser(this.mockConnection,
                                 "username");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteUser_UserNotExist() throws Exception {
        final String user = "username";
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(UserTable.DELETE_USER_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate())
            .thenReturn(0);

        try {
            UserTable.deleteUser(this.mockConnection,
                                 user);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_USER_NOT_EXIST, ex.status);

            verify(mockPreparedStatement, times(1)).setString(1, user);
            verify(mockPreparedStatement, times(1)).executeUpdate();
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_DeleteUser_SuccessCase() throws Exception {
        final String user = "username";
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(this.mockConnection.prepareStatement(UserTable.DELETE_USER_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate())
            .thenReturn(1);

        UserTable.deleteUser(this.mockConnection,
                             user);

        verify(mockPreparedStatement, times(1)).setString(1, user);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_UpdatePassword_NullConnection() throws ServiceException {
        UserTable.updatePassword(null,
                                 0,
                                 "newPass");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_UpdatePassword_NullNewPass() throws ServiceException {
        UserTable.updatePassword(this.mockConnection,
                                 0,
                                 null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_UpdatePassword_EmptyNewPass() throws ServiceException {
        UserTable.updatePassword(this.mockConnection,
                                 0,
                                 "");
    }

    @Test
    public void test_UpdatePassword_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_PASSWORD_QUERY))
            .thenReturn(mockPreparedStatement);

        try {
            UserTable.updatePassword(this.mockConnection, 12, "newpass");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        verify(mockPreparedStatement, times(1)).close();

        fail("no exception thrown");
    }

    @Test
    public void test_UpdatePassword_ExecuteUpdateReturns0() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_PASSWORD_QUERY))
            .thenReturn(mockPreparedStatement);

        try {
            UserTable.updatePassword(this.mockConnection, 12, "newpass");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_USER_NOT_EXIST, ex.status);
            return;
        }

        verify(mockPreparedStatement, times(1)).close();

        fail("no exception thrown");
    }

    @Test
    public void test_UpdatePassword_Success() throws Exception {
        final String newpass = "newpass";
        final int uid = 12;
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_PASSWORD_QUERY))
            .thenReturn(mockPreparedStatement);

        UserTable.updatePassword(this.mockConnection, uid, newpass);

        verify(mockPreparedStatement, times(1)).setString(1, newpass);
        verify(mockPreparedStatement, times(1)).setInt(2, uid);
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_UpdateEmail_NullConnection() throws Exception {
        UserTable.updateEmail(null,
                              1,
                              new InternetAddress("gundermanc@gmail.com"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_UpdateEmail_NullInternetAddress() throws Exception {
        UserTable.updateEmail(this.mockConnection,
                              1,
                              null);
    }

    @Test
    public void test_UpdateEmail_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_EMAIL_QUERY))
            .thenReturn(mockPreparedStatement);

        try {
            UserTable.updateEmail(this.mockConnection,
                                  12,
                                  new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        verify(mockPreparedStatement, times(1)).close();

        fail("no exception thrown");
    }

    @Test
    public void test_UpdateEmail_ExecuteUpdateReturns0() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_EMAIL_QUERY))
            .thenReturn(mockPreparedStatement);

        try {
            UserTable.updateEmail(this.mockConnection,
                                  12,
                                  new InternetAddress("gundermanc@gmail.com"));
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_USER_NOT_EXIST, ex.status);
            return;
        }

        verify(mockPreparedStatement, times(1)).close();

        fail("no exception thrown");
    }

    @Test
    public void test_UpdateEmail_Success() throws Exception {
        final String newEmail = "gundermanc@gmail.com";
        final int uid = 12;
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(this.mockConnection.prepareStatement(UserTable.UPDATE_USER_EMAIL_QUERY))
            .thenReturn(mockPreparedStatement);

        UserTable.updateEmail(this.mockConnection,
                              uid,
                              new InternetAddress(newEmail));

        verify(mockPreparedStatement, times(1)).setString(1, newEmail);
        verify(mockPreparedStatement, times(1)).setInt(2, uid);
        verify(mockPreparedStatement, times(1)).close();
    }






















    
    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserWithRolesById_NullConnection() throws Exception {
        UserTable.lookupUserWithRolesById(null,
                                          12);
    }

    @Test
    public void test_LookupUserWithRolesById_SQLException() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());
        when(this.mockConnection.prepareStatement(UserTable.LOOKUP_USERID_QUERY))
            .thenReturn(mockPreparedStatement);

        try {
            UserTable.lookupUserWithRolesById(this.mockConnection, 12);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        verify(mockPreparedStatement, times(1)).close();

        fail("no exception thrown");
    }

    @Test
    public void test_LookupUserWithRolesById_Success() throws Exception {
        final String newEmail = "gundermanc@gmail.com";
        final int uid = 12;
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        //when(mockPreparedStatement.executeQuery()).thenReturn(1);
        when(this.mockConnection.prepareStatement(UserTable.LOOKUP_USERID_QUERY))
             .thenReturn(mockPreparedStatement);

        UserTable.lookupUserWithRolesById(this.mockConnection,
                                          uid);

        verify(mockPreparedStatement, times(1)).setInt(1, uid);
    }
}
