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
import java.util.UUID;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for UserSession validation and exception handling. Does
 * not validate SQL queries.
 */
public class test_UserSessionTable {
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    
    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertSession_NullConnection_Exception() throws Exception {
        UserSessionTable.insertSession(null,
                                       456,
                                       UUID.randomUUID());
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_InsertSession_NullUUID_Exception() throws Exception {
        UserSessionTable.insertSession(this.mockConnection,
                                       456,
                                       null);
    }

    @Test
    public void test_InsertSession_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .INSERT_SESSION_QUERY))
            .thenThrow(new SQLException());

        try {
            UserSessionTable.insertSession(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_InsertSession_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .INSERT_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLException());
        
        try {
            UserSessionTable.insertSession(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_InsertSession_SuccessCase() throws Exception {
        final int uid = 456;
        final UUID uuid = UUID.randomUUID();
        
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .INSERT_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);
        
        UserSessionTable.insertSession(this.mockConnection,
                                       uid,
                                       uuid);

        verify(this.mockPreparedStatement, times(1)).setLong(1, uid);
        verify(this.mockPreparedStatement, times(1)).setString(2, uuid.toString());
        verify(this.mockPreparedStatement, times(1)).execute();
        verify(this.mockPreparedStatement, times(1)).close();
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteSession_NullConnection_Exception() throws Exception {
        UserSessionTable.deleteSession(null,
                                       456,
                                       UUID.randomUUID());
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteSession_NullUUID_Exception() throws Exception {
        UserSessionTable.deleteSession(this.mockConnection,
                                       456,
                                       null);
    }

    @Test
    public void test_DeleteSession_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_SESSION_QUERY))
            .thenThrow(new SQLException());

        try {
            UserSessionTable.deleteSession(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteSession_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLException());
        
        try {
            UserSessionTable.deleteSession(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteSession_SuccessCase() throws Exception {
        final long uid = 456;
        final UUID uuid = UUID.randomUUID();
        
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);
        
        UserSessionTable.deleteSession(this.mockConnection,
                                       uid,
                                       uuid);

        verify(this.mockPreparedStatement, times(1)).setLong(1, uid);
        verify(this.mockPreparedStatement, times(1)).setString(2, uuid.toString());
        verify(this.mockPreparedStatement, times(1)).execute();
        verify(this.mockPreparedStatement, times(1)).close();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteAllSessions_NullConnection_Exception() throws Exception {
        UserSessionTable.deleteAllSessions(null,
                                           456);
    }

    @Test
    public void test_DeleteAllSessions_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_QUERY))
            .thenThrow(new SQLException());

        try {
            UserSessionTable.deleteAllSessions(this.mockConnection,
                                               456);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteAllSessions_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLException());
        
        try {
            UserSessionTable.deleteAllSessions(this.mockConnection,
                                               456);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteAllSessions_SuccessCase() throws Exception {
        final int uid = 456;
        final UUID uuid = UUID.randomUUID();
        
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_QUERY))
            .thenReturn(this.mockPreparedStatement);
        
        UserSessionTable.deleteAllSessions(this.mockConnection,
                                           uid);

        verify(this.mockPreparedStatement, times(1)).setLong(1, uid);
        verify(this.mockPreparedStatement, times(1)).execute();
        verify(this.mockPreparedStatement, times(1)).close();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteAllSessionsByUser_NullConnection_Exception() throws Exception {
        UserSessionTable.deleteAllSessions(null,
                                           456);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteAllSessionsByUser_NullUser_Exception() throws Exception {
        UserSessionTable.deleteAllSessions(this.mockConnection,
                                           null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_DeleteAllSessionsByUser_EmptyUser_Exception() throws Exception {
        UserSessionTable.deleteAllSessions(this.mockConnection,
                                           "");
    }

    @Test
    public void test_DeleteAllSessionsByUser_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_NAME_QUERY))
            .thenThrow(new SQLException());

        try {
            UserSessionTable.deleteAllSessions(this.mockConnection,
                                               "username");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteAllSessionsByUser_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_NAME_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.execute())
            .thenThrow(new SQLException());
        
        try {
            UserSessionTable.deleteAllSessions(this.mockConnection,
                                               "username");
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_DeleteAllSessionsByUser_SuccessCase() throws Exception {
        final String username = "username";
        final UUID uuid = UUID.randomUUID();
        
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .DELETE_ALL_SESSIONS_NAME_QUERY))
            .thenReturn(this.mockPreparedStatement);
        
        UserSessionTable.deleteAllSessions(this.mockConnection,
                                           username);

        verify(this.mockPreparedStatement, times(1)).setString(1, username);
        verify(this.mockPreparedStatement, times(1)).execute();
        verify(this.mockPreparedStatement, times(1)).close();
    }
















    
    @Test(expected=IllegalArgumentException.class)
    public void test_SessionExists_NullConnection_Exception() throws Exception {
        UserSessionTable.sessionExists(null,
                                       456,
                                       UUID.randomUUID());
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_SessionExists_NullUUID_Exception() throws Exception {
        UserSessionTable.sessionExists(this.mockConnection,
                                       456,
                                       null);
    }

    @Test
    public void test_SessionExists_SQLException_1_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .QUERY_SESSION_QUERY))
            .thenThrow(new SQLException());

        try {
            UserSessionTable.sessionExists(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_SessionExists_SQLException_2_Exception() throws Exception {
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .QUERY_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);

        when(this.mockPreparedStatement.executeQuery())
            .thenThrow(new SQLException());
        
        try {
            UserSessionTable.sessionExists(this.mockConnection,
                                           456,
                                           UUID.randomUUID());
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown.");
    }

    @Test
    public void test_SessionExists_SuccessCase_1() throws Exception {
        final int uid = 456;
        final UUID uuid = UUID.randomUUID();

        ResultSet mockResultSet = mock(ResultSet.class);

        // Session exists.
        when(mockResultSet.next())
            .thenReturn(true);
        
        when(this.mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);
        
        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .QUERY_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);
        
        assertTrue(UserSessionTable.sessionExists(this.mockConnection,
                                                  uid,
                                                  uuid));

        verify(this.mockPreparedStatement, times(1)).setLong(1, uid);
        verify(this.mockPreparedStatement, times(1)).setString(2, uuid.toString());
        verify(this.mockPreparedStatement, times(1)).executeQuery();
        verify(this.mockPreparedStatement, times(1)).close();
    }

    @Test
    public void test_SessionExists_SuccessCase_2() throws Exception {
        final int uid = 456;
        final UUID uuid = UUID.randomUUID();

        ResultSet mockResultSet = mock(ResultSet.class);

        // Session not exist.
        when(mockResultSet.next())
            .thenReturn(false);

        when(this.mockPreparedStatement.executeQuery())
            .thenReturn(mockResultSet);

        when(this.mockConnection.prepareStatement(UserSessionTable
                                                  .QUERY_SESSION_QUERY))
            .thenReturn(this.mockPreparedStatement);

        assertFalse(UserSessionTable.sessionExists(this.mockConnection,
                                                  uid,
                                                  uuid));

        verify(this.mockPreparedStatement, times(1)).setLong(1, uid);
        verify(this.mockPreparedStatement, times(1)).setString(2, uuid.toString());
        verify(this.mockPreparedStatement, times(1)).executeQuery();
        verify(this.mockPreparedStatement, times(1)).close();
    }
}
