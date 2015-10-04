package com.fecklessweasel.service.objectmodel;

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

public class test_Department {

	private Connection mockConnection;
	
	@Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception{
    	Department.create(null, 9, "deptname here", "EECS");
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_nullDeptName() throws Exception{
    	Department.create(this.mockConnection, 9, null, "EECS");
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_nullAcronym() throws Exception{
    	Department.create(this.mockConnection, 9, "deptname here", null);
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_badDeptname_numbers() throws Exception{
    	Department.create(this.mockConnection, 9, "deptname1234", "EECS");
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_badAcronym_numbers() throws Exception{
    	Department.create(this.mockConnection, 9, "deptname here", "33C5");
    }

}