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

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullConnection() throws Exception {
        CourseTable.insertCourse(null, 9, 9, 9);
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
        assert(CourseTable.insertCourse(this.mockConnection,
                                    9,
                                    9,
                                    9) == id);
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
            CourseTable.insertCourse(this.mockConnection,9,9,9);
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
            CourseTable.insertCourse(this.mockConnection,9,9,9);
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }

}