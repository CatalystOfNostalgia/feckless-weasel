package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class test_CourseTable {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
        this.mockPreparedStatement = mock(PreparedStatement.class);
        this.mockResultSet = mock(ResultSet.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullConnection() throws Exception {
        CourseTable.insertCourse(null, 9, 9, "CourseName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullName() throws Exception {
        CourseTable.insertCourse(mockConnection, 9, 9, null);
    }
    
    @Test
    public void test_insert_valid() throws Exception {
        //id to check
        final int id = 12345;
        // mock setup
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        // set mock whens
        when(mockResultSet.getInt(1)).thenReturn(id);
        when(mockPreparedStatement.getGeneratedKeys())
            .thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(CourseTable.INSERT_ROW,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenReturn(mockPreparedStatement);
        //check return int
        assert(CourseTable.insertCourse(this.mockConnection,9,9,"CourseName") == id);
        // Check results are looked at
        verify(mockResultSet, times(1)).next();
        // Check statement is closed
        verify(mockPreparedStatement, times(1)).close();
    }
    
    @Test
    public void test_insert_SQLIntegretyViolation() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.INSERT_ROW,
                Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLIntegrityConstraintViolationException());
        try{
            CourseTable.insertCourse(this.mockConnection,9,9,"courseName");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.APP_COURSE_TAKEN);
        }
    }
    
    @Test
    public void test_insert_SQLException() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.INSERT_ROW,
                Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLException());
        try{
            CourseTable.insertCourse(this.mockConnection,9,9,"CourseName");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_lookupCourse_nullConnection() throws Exception {
        CourseTable.lookupCourse(null, 1);
    }

    @Test
    public void test_lookupCourse_SQLException() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.LOOKUP_ROW))
                .thenThrow(new SQLException());

        try {
            CourseTable.lookupCourse(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupCourse_success() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.LOOKUP_ROW))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet result = CourseTable.lookupCourse(mockConnection, 1);
        assertEquals(mockResultSet, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_lookupPaginated_nullConnection() throws Exception {
        CourseTable.lookUpPaginated(null, 1, 2, 3);
    }

    @Test
    public void test_lookupPaginated_SQLException() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.SELECT_PAGINATED))
                .thenThrow(new SQLException());

        try {
            CourseTable.lookUpPaginated(mockConnection, 1, 2, 3);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupPaginated_success() throws Exception {
        when(mockConnection.prepareStatement(CourseTable.SELECT_PAGINATED))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet result = CourseTable.lookUpPaginated(mockConnection, 1, 2, 3);
        assertEquals(mockResultSet, result);
    }
}