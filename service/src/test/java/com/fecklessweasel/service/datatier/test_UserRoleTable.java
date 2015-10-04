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
 * Unit tests for UserRoleTable validation and exception handling. Does
 * not validate SQL queries.
 */
public class test_UserRoleTable {
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    
    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserRole_NullConnection_Exception() throws Exception {
        UserRoleTable.insertUserRole(null,
                                     "ROLE_ADMIN",
                                     "My admin role");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserRole_NullRoleId_Exception() throws Exception {
        UserRoleTable.insertUserRole(this.mockConnection,
                                     null,
                                     "My admin role");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserRole_NullRoleDescription_Exception() throws Exception {
        UserRoleTable.insertUserRole(this.mockConnection,
                                     "ROLE_ADMIN",
                                     null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserRole_EmptyRoleId_Exception() throws Exception {
        UserRoleTable.insertUserRole(this.mockConnection,
                                     "",
                                     "My admin role");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertUserRole_EmptyRoleDescription_Exception() throws Exception {
        UserRoleTable.insertUserRole(this.mockConnection,
                                     "ROLE_ADMIN",
                                     "");
    }

    @Test
    public void test_InsertUserRole_RoleExists_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.INSERT_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLIntegrityConstraintViolationException());
        
        try {
            UserRoleTable.insertUserRole(this.mockConnection,
                                         "ROLE_ADMIN",
                                         "My admin role");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_ROLE_ID_TAKEN, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_InsertUserRole_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.INSERT_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.insertUserRole(this.mockConnection,
                                         "ROLE_ADMIN",
                                         "My admin role");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_InsertUserRole_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.INSERT_ROLE_QUERY))
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.insertUserRole(this.mockConnection,
                                         "ROLE_ADMIN",
                                         "My admin role");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_InsertUserRole_SuccessCase() throws Exception {
        final String roleId = "ROLE_ADMIN";
        final String roleDesc = "My admin role";
        when(this.mockConnection.prepareStatement(UserRoleTable.INSERT_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        UserRoleTable.insertUserRole(this.mockConnection,
                                     roleId,
                                     roleDesc);

        verify(this.mockPreparedStatement, times(1)).setString(1, roleId);
        verify(this.mockPreparedStatement, times(1)).setString(2, roleDesc);
        verify(this.mockPreparedStatement, times(1)).execute();
        verify(this.mockPreparedStatement, times(1)).close();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserRole_NullConnection_Exception() throws Exception {
        UserRoleTable.lookupUserRole(null,
                                     "ROLE_ADMIN");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserRole_NullRoleId_Exception() throws Exception {
        UserRoleTable.lookupUserRole(this.mockConnection,
                                     null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_LookupUserRole_EmptyRoleId_Exception() throws Exception {
        UserRoleTable.lookupUserRole(this.mockConnection,
                                     "");
    }

    @Test
    public void test_LookupUserRole_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.LOOKUP_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.executeQuery())
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.lookupUserRole(this.mockConnection,
                                         "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_LookupUserRole_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.LOOKUP_ROLE_QUERY))
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.lookupUserRole(this.mockConnection,
                                         "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_LookupUserRole_SuccessCase() throws Exception {
        final String roleId = "ROLE_ADMIN";
        when(this.mockConnection.prepareStatement(UserRoleTable.LOOKUP_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        UserRoleTable.lookupUserRole(this.mockConnection,
                                     roleId);

        verify(this.mockPreparedStatement, times(1)).setString(1, roleId);
        verify(this.mockPreparedStatement, times(1)).executeQuery();
        verify(this.mockPreparedStatement, times(1)).close();
    }





















    
    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserRole_NullConnection_Exception() throws Exception {
        UserRoleTable.deleteUserRole(null,
                                     "ROLE_ADMIN");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserRole_NullRoleId_Exception() throws Exception {
        UserRoleTable.deleteUserRole(this.mockConnection,
                                     null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteUserRole_EmptyRoleId_Exception() throws Exception {
        UserRoleTable.deleteUserRole(this.mockConnection,
                                     "");
    }

    @Test
    public void test_DeleteUserRole_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.DELETE_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.executeUpdate())
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.deleteUserRole(this.mockConnection,
                                         "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

    @Test
    public void test_DeleteUserRole_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserRoleTable.DELETE_ROLE_QUERY))
            .thenThrow(new SQLException());
        
        try {
            UserRoleTable.deleteUserRole(this.mockConnection,
                                         "ROLE_ADMIN");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception was thrown");
    }

        @Test
    public void test_DeleteUserRole_UserRoleNotExist() throws Exception {
        final String roleId = "ROLE_ADMIN";
        when(this.mockConnection.prepareStatement(UserRoleTable.DELETE_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        // Indicate failure (0 row updated).
        when(this.mockPreparedStatement.executeUpdate())
            .thenReturn(0);

        try {
            UserRoleTable.deleteUserRole(this.mockConnection,
                                         roleId);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.APP_ROLE_NOT_EXIST, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_DeleteUserRole_SuccessCase() throws Exception {
        final String roleId = "ROLE_ADMIN";
        when(this.mockConnection.prepareStatement(UserRoleTable.DELETE_ROLE_QUERY))
            .thenReturn(this.mockPreparedStatement);

        // Indicate success (1 row updated).
        when(this.mockPreparedStatement.executeUpdate())
            .thenReturn(1);

        UserRoleTable.deleteUserRole(this.mockConnection,
                                     roleId);

        verify(this.mockPreparedStatement, times(1)).setString(1, roleId);
        verify(this.mockPreparedStatement, times(1)).executeUpdate();
        verify(this.mockPreparedStatement, times(1)).close();
    }
}
