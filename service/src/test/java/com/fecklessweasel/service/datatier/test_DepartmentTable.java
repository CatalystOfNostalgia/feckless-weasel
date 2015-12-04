package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fecklessweasel.service.objectmodel.Department;
import com.fecklessweasel.service.objectmodel.ServiceException;
import com.fecklessweasel.service.objectmodel.ServiceStatus;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

public class test_DepartmentTable {

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
        DepartmentTable.insertDepartment(null, 10, "deptname", "acronym");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullDeptName() throws Exception {
        DepartmentTable.insertDepartment(this.mockConnection, 10, null, "acronym");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_nullAcronym() throws Exception {
        DepartmentTable.insertDepartment(this.mockConnection, 10, "deptname", null);
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
        when(mockConnection.prepareStatement(DepartmentTable.INSERT_ROW,
                                             Statement.RETURN_GENERATED_KEYS))
            .thenReturn(mockPreparedStatement);
        //check return int
        assert(DepartmentTable.insertDepartment(this.mockConnection,10,"deptname","acronym") == id);
        // Check results are looked at
        verify(mockResultSet, times(1)).next();
        // Check statement is closed
        verify(mockPreparedStatement, times(1)).close();
    }
    
    @Test
    public void test_insert_SQLIntegretyViolation() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.INSERT_ROW,
                Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLIntegrityConstraintViolationException());
        try{
            DepartmentTable.insertDepartment(this.mockConnection,10,"deptname","acronym");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.APP_DEPT_TAKEN);
        }
    }
    
    @Test
    public void test_insert_SQLException() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.INSERT_ROW,
                Statement.RETURN_GENERATED_KEYS))
                .thenThrow(new SQLException());
        try{
            DepartmentTable.insertDepartment(this.mockConnection,10,"deptname","acronym");
        } catch (ServiceException ex) {
            assert(ex.status == ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_lookupDepartment_nullConnection() throws Exception {
        DepartmentTable.lookupDepartment(null, 1);
    }

    @Test
    public void test_lookupDepartment_SQLException() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.LOOKUP_ROW))
            .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            DepartmentTable.lookupDepartment(mockConnection, 1);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupDepartment_success() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.LOOKUP_ROW))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet result = DepartmentTable.lookupDepartment(mockConnection, 1);
        assertEquals(mockResultSet, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_lookupPaginated_nullConnection() throws Exception {
        DepartmentTable.lookUpPaginated(null, 1, 2, 3);
    }

    @Test
    public void test_lookupPaginated_SQLException() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.SELECT_PAGINATED))
                .thenThrow(new SQLIntegrityConstraintViolationException());

        try {
            DepartmentTable.lookUpPaginated(mockConnection, 1, 2, 3);
        } catch (ServiceException ex) {
            assertEquals(ex.status, ServiceStatus.DATABASE_ERROR);
        }
    }

    @Test
    public void test_lookupPaginated_success() throws Exception {
        when(mockConnection.prepareStatement(DepartmentTable.SELECT_PAGINATED))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery())
                .thenReturn(mockResultSet);

        ResultSet result = DepartmentTable.lookUpPaginated(mockConnection, 1, 2, 3);
        assertEquals(mockResultSet, result);
    }
}