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

public class test_DepartmentTable {

    private Connection mockConnection;

    @Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
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

}