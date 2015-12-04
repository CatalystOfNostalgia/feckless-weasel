package com.fecklessweasel.service.datatier;

import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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

public class test_RatingTable {

    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullConnection() throws Exception {
        RatingTable.addRating(null, 9, 9, 9);
    }

    @Test
    public void test_Insert_ServiceException() throws Exception{
    	when(mockConnection.prepareStatement(RatingTable.ADD_RATING))
    		.thenThrow(new SQLException());

		try {
			RatingTable.addRating(mockConnection, 9 , 9 ,9);
		} catch (ServiceException ex) {
			assert(ex.status == ServiceStatus.DATABASE_ERROR);
			// return;
		}

		// fail("no exception!")
    }

    @Test
    public void test_Insert_Success_New_Rating() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
   
    	when(mockConnection.prepareStatement(RatingTable.ADD_RATING))
    		.thenReturn(mockPreparedStatement);

    	RatingTable.addRating(mockConnection, 9 , 9 ,9);


    	verify(mockPreparedStatement, times(1)).close();
    }

    @Test
    public void test_Insert_Success_Updated_Rating() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    	PreparedStatement mockPreparedStatementUpdate = mock(PreparedStatement.class);

    	when(mockConnection.prepareStatement(RatingTable.ADD_RATING))
    		.thenThrow(new SQLIntegrityConstraintViolationException());

    	when(mockConnection.prepareStatement(RatingTable.UPDATE_RATING))
    		.thenReturn(mockPreparedStatementUpdate);

    	RatingTable.addRating(mockConnection, 9, 9, 9);

		verify(mockPreparedStatementUpdate, times(1)).close();    	
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getFileRating_null_connection() throws Exception {
    	RatingTable.getFileRating(null, 1);
    }

    @Test
    public void test_getFileRating_SQLException() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    	when (mockConnection.prepareStatement(RatingTable.GET_FILE_RATING))
    		.thenThrow(new SQLException());

    	try {
    		RatingTable.getFileRating(mockConnection, 1);
    	} catch (ServiceException ex) {
    		assert(ex.status == ServiceStatus.DATABASE_ERROR);
    	}
    }

    @Test
    public void test_getFileRating_Success() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

    	when(mockConnection.prepareStatement(RatingTable.GET_FILE_RATING))
    		.thenReturn(mockPreparedStatement);

    	RatingTable.getFileRating(mockConnection, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getUserFileRating_null_connection() throws Exception {
    	RatingTable.getUserFileRating(null, 1 ,1);
    }

    @Test
    public void test_getUserFileRating_SQLException() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    	when (mockConnection.prepareStatement(RatingTable.GET_USER_FILE_RATING))
    		.thenThrow(new SQLException());

    	try {
    		RatingTable.getUserFileRating(mockConnection, 1 ,1);
    	} catch (ServiceException ex) {
    		assert(ex.status == ServiceStatus.DATABASE_ERROR);
    	}
    }

    @Test
    public void test_getUserFileRating_RatingExists() throws Exception {
    	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    	ResultSet mockResultSet = mock (ResultSet.class);

    	when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

    	when(mockConnection.prepareStatement(RatingTable.GET_USER_FILE_RATING))
    		.thenReturn(mockPreparedStatement);

    	when(mockResultSet.next()).thenReturn(true);
    	when(mockResultSet.isBeforeFirst()).thenReturn(true);

    	RatingTable.getUserFileRating(mockConnection, 1 ,1);
    }

    @Test
    public void test_getUserFileRating_RatingDoesntExist() throws Exception {
       	PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    	ResultSet mockResultSet = mock (ResultSet.class);

    	when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

    	when(mockConnection.prepareStatement(RatingTable.GET_USER_FILE_RATING))
    		.thenReturn(mockPreparedStatement);

    	when(mockResultSet.next()).thenReturn(true);
    	when(mockResultSet.isBeforeFirst()).thenReturn(false);

    	RatingTable.getUserFileRating(mockConnection, 1 ,1);
    }
}