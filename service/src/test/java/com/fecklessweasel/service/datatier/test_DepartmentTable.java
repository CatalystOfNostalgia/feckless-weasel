package com.fecklessweasel.service.datatier;

import java.sql.Connection;
import java.sql.SQLException;

import com.fecklessweasel.service.objectmodel.ServiceException;

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
    public void test_insert_nullConnection() throws Exception{
    	DepartmentTable.insertDepartment(null, 10, "deptname", "acronym");
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void test_insert_nullUnivId() throws Exception{
		DepartmentTable.insertDepartment(this.mockConnection, 10, "deptname", "acronym");
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void test_insert_nullDeptName() throws Exception{
		DepartmentTable.insertDepartment(this.mockConnection, 10, null, "acronym");
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void test_insert_nullAcronym() throws Exception{
		DepartmentTable.insertDepartment(this.mockConnection, 10, "deptname", null);
    }

}