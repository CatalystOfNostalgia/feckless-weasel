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
 * Unit tests for UserHasRoleTable validation and exception handling. Does
 * not validate SQL queries.
 */
public class test_UserHasRoleTable {
    private Connection mockConnection;
    
    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserHasRole_NullConnection_Exception() throws Exception {
        UserHasRoleTable.insertUserHasRole(null,
                                           11,
                                           "ROLE_ADMIN");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserHasRole_NullRoleName_Exception() throws Exception {
        UserHasRoleTable.insertUserHasRole(this.mockConnection,
                                           11,
                                           null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserHasRole_EmptyRoleName_Exception() throws Exception {
        UserHasRoleTable.insertUserHasRole(this.mockConnection,
                                           11,
                                           "");
    }

    @Test
    public void test_InsertUserHasRole_UserAlreadyHasRole_Exception() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable.INSERT_USER_HAS_ROLE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute())
            .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            UserHasRoleTable.insertUserHasRole(this.mockConnection,
                                               11,
                                               "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_USER_HAS_ROLE_DUPLICATE, ex.status);
            
            verify(mockPreparedStatement, times(1)).setLong(1, 11);
            verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
            verify(mockPreparedStatement, times(1)).execute();
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_InsertUserHasRole_SQLException_Exception() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable.INSERT_USER_HAS_ROLE_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute())
            .thenThrow(new SQLException());

        try {
            UserHasRoleTable.insertUserHasRole(this.mockConnection,
                                               11,
                                               "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            
            verify(mockPreparedStatement, times(1)).setLong(1, 11);
            verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
            verify(mockPreparedStatement, times(1)).execute();
            return;
        }

        fail("No exception thrown");
    }
    
    @Test
    public void test_InsertUserHasRole_SuccessCase() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable.INSERT_USER_HAS_ROLE_QUERY))
            .thenReturn(mockPreparedStatement);
        
        UserHasRoleTable.insertUserHasRole(this.mockConnection,
                                           11,
                                           "ROLE_ADMIN");

        verify(mockPreparedStatement, times(1)).setLong(1, 11);
        verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
        verify(mockPreparedStatement, times(1)).execute();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserHasRole_NullConnection_Exception() throws Exception {
        UserHasRoleTable.deleteUserHasRole(null,
                                           11,
                                           "ROLE_ADMIN");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserHasRole_NullRoleName_Exception() throws Exception {
        UserHasRoleTable.deleteUserHasRole(this.mockConnection,
                                           11,
                                           null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserHasRole_EmptyRoleName_Exception() throws Exception {
        UserHasRoleTable.deleteUserHasRole(this.mockConnection,
                                           11,
                                           "");
    }

    @Test
    public void test_DeleteUserHasRole_UserNotHaveRole_Exception() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable
                                                  .DELETE_USER_HAS_ROLE_NAME_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute())
            .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            UserHasRoleTable.deleteUserHasRole(this.mockConnection,
                                               11,
                                               "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_USER_NOT_HAVE_ROLE, ex.status);
            
            verify(mockPreparedStatement, times(1)).setLong(1, 11);
            verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
            verify(mockPreparedStatement, times(1)).execute();
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_DeleteUserHasRole_SQLException_Exception() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable.DELETE_USER_HAS_ROLE_NAME_QUERY))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute())
            .thenThrow(new SQLException());

        try {
            UserHasRoleTable.deleteUserHasRole(this.mockConnection,
                                               11,
                                               "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            
            verify(mockPreparedStatement, times(1)).setLong(1, 11);
            verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
            verify(mockPreparedStatement, times(1)).execute();
            return;
        }

        fail("No exception thrown");
    }
    
    @Test
    public void test_DeleteUserHasRole_SuccessCase() throws Exception {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(this.mockConnection.prepareStatement(UserHasRoleTable
                                                  .DELETE_USER_HAS_ROLE_NAME_QUERY))
            .thenReturn(mockPreparedStatement);
        
        UserHasRoleTable.deleteUserHasRole(this.mockConnection,
                                           11,
                                           "ROLE_ADMIN");

        verify(mockPreparedStatement, times(1)).setLong(1, 11);
        verify(mockPreparedStatement, times(1)).setString(2, "ROLE_ADMIN");
        verify(mockPreparedStatement, times(1)).execute();
    }
}
