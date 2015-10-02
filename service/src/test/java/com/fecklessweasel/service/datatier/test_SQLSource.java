package com.fecklessweasel.service.datatier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

/**
 * Unit tests for SQLSource validation and exception handling.
 */
public class test_SQLSource {
    private Connection mockConnection;
    private Context mockEnvironmentContext;
    private DataSource mockDataSource;
    private PreparedStatement mockPreparedStatement;
    
    @Before
    public void setup() throws Exception {
        this.mockPreparedStatement = mock(PreparedStatement.class);
        
        this.mockConnection = mock(Connection.class);
        when(this.mockConnection.prepareStatement(SQLSource.USE_DB_STATEMENT))
            .thenReturn(this.mockPreparedStatement);
        
        this.mockDataSource = mock(DataSource.class);

        this.mockEnvironmentContext = mock(Context.class);

        SQLSource.initialContext = mock(InitialContext.class);
        when(SQLSource.initialContext.lookup("java:comp/env"))
            .thenReturn(mockEnvironmentContext);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_Interact_NullAction_Exception() throws Exception {
        SQLSource.interact(null);
    }

    @Test
    public void test_Interact_InitialNamingException_Exception() throws Exception {
        // Simulate server resource naming exception (misconfigured DB resource).
        when(this.mockEnvironmentContext.lookup("jdbc/FecklessWeaselDB"))
            .thenThrow(new NamingException());

        SQLInteractionInterface<Integer> mockInteraction
            = (SQLInteractionInterface<Integer>) mock(SQLInteractionInterface.class);
        try {
            SQLSource.interact(mockInteraction);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_Interact_EnvironmentNamingException_Exception() throws Exception {
        // Simulate server resource naming exception (misconfigured DB resource).
        when(SQLSource.initialContext.lookup("java:comp/env"))
            .thenThrow(new NamingException());

        SQLInteractionInterface<Integer> mockInteraction
            = (SQLInteractionInterface<Integer>) mock(SQLInteractionInterface.class);
        try {
            SQLSource.interact(mockInteraction);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_Interact_SQLException_Exception() throws Exception {
        when(this.mockEnvironmentContext.lookup("jdbc/FecklessWeaselDB"))
            .thenReturn(this.mockDataSource);
        when(this.mockDataSource.getConnection())
            .thenThrow(new SQLException());

        SQLInteractionInterface<Integer> mockInteraction
            = (SQLInteractionInterface<Integer>) mock(SQLInteractionInterface.class);
        try {
            SQLSource.interact(mockInteraction);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }

        fail("No exception thrown");
    }

    @Test
    public void test_Interact_SQLExceptionOnClose_Exception() throws Exception {
        final long testLong = 3456;
        
        when(this.mockEnvironmentContext.lookup("jdbc/FecklessWeaselDB"))
            .thenReturn(this.mockDataSource);
        when(this.mockDataSource.getConnection())
            .thenReturn(this.mockConnection);
        doThrow(new SQLException()).when(this.mockConnection).close();

        SQLInteractionInterface<Long> mockInteraction
            = (SQLInteractionInterface<Long>) mock(SQLInteractionInterface.class);

        when(mockInteraction.run(this.mockConnection)).
            thenReturn(testLong);

        try {
            SQLSource.interact(mockInteraction);
        } catch (ServiceException ex) {
            assertEquals(ServiceStatus.DATABASE_ERROR, ex.status);
            return;
        }
        
        fail("No exception was thrown.");
    }

    @Test
    public void test_Interact_Success_1() throws Exception {
        final long testLong = 3456;
        
        when(this.mockEnvironmentContext.lookup("jdbc/FecklessWeaselDB"))
            .thenReturn(this.mockDataSource);
        when(this.mockDataSource.getConnection())
            .thenReturn(this.mockConnection);

        SQLInteractionInterface<Long> mockInteraction
            = (SQLInteractionInterface<Long>) mock(SQLInteractionInterface.class);

        when(mockInteraction.run(this.mockConnection)).
            thenReturn(testLong);
        
        assertEquals((long)testLong, (long)(SQLSource.interact(mockInteraction)));

        verify(mockInteraction, times(1)).run(this.mockConnection);
    }

    public void test_Interact_Success_2() throws Exception {
        final long testLong = 3456;
        
        when(this.mockEnvironmentContext.lookup("jdbc/FecklessWeaselDB"))
            .thenReturn(this.mockDataSource);
        when(this.mockDataSource.getConnection())
            .thenReturn(this.mockConnection);

        SQLInteractionInterface<Long> mockInteraction
            = (SQLInteractionInterface<Long>) mock(SQLInteractionInterface.class);

        when(mockInteraction.run(this.mockConnection)).
            thenReturn(testLong);
        
        assertEquals((long)testLong, (long)(SQLSource.interact(mockInteraction)));

        verify(mockInteraction, times(1)).run(this.mockConnection);
    }
}
