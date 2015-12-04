package com.fecklessweasel.service.datatier;

import com.fecklessweasel.service.objectmodel.ServiceException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fecklessweasel.service.objectmodel.ServiceStatus;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.sql.*;

public class test_CommentTable {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullConnection() throws Exception {
        CommentTable.addComment(null, 9, 9, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullText() throws Exception {
        CommentTable.addComment(mockConnection, 9, 9, null);
    }

    @Test
    public void test_insert_SQLException() throws Exception {
        when(mockConnection.prepareStatement(CommentTable.ADD_COMMENT))
                .thenThrow(new SQLException());

        try {
            CommentTable.addComment(mockConnection, 9, 9, "hello");
        } catch(ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_insert_SQLIntegretyException() throws Exception {
        when(mockConnection.prepareStatement(CommentTable.ADD_COMMENT))
                .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            CommentTable.addComment(mockConnection, 9, 9, "hello");
        } catch(ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.APP_COMMENT_TAKEN);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_insert_success() throws Exception {
        when(mockConnection.prepareStatement(CommentTable.ADD_COMMENT))
                .thenReturn(mockPreparedStatement);

        Timestamp time = new Timestamp(new Date().getTime());
        Timestamp returnTime = CommentTable.addComment(mockConnection, 9, 9, "hello");

        assertEquals(time, returnTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getFileComments_nullConnection() throws Exception {
        CommentTable.addComment(null, 9, 9, "hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getFileComments_nullText() throws Exception {
        CommentTable.addComment(mockConnection, 9, 9, null);
    }

    @Test
    public void test_getFileComments_SQLException() throws Exception {
        when(mockConnection.prepareStatement(CommentTable.GET_FILE_COMMENTS))
                .thenThrow(new SQLException());

        try {
            CommentTable.getFileComments(mockConnection, 9, 9, 9);
        } catch(ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
            return;
        }
        fail("No exception thrown");
    }

    @Test
    public void test_getFileComments_success() throws Exception {
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(CommentTable.GET_FILE_COMMENTS))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet result = CommentTable.getFileComments(mockConnection, 1, 1, 1);
        assertEquals(result, mockResultSet);
    }
}