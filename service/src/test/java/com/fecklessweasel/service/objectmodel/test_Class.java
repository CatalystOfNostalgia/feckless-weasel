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

public class test_Class {

	private Connection mockConnection;
	
	@Before
    public void setup() {
        this.mockConnection = mock(Connection.class);
    }
	
	@Test(expected = ServiceException.class)
    public void test_create_nullConnection() throws Exception{
    	Class.create(null, 9, 9, 9);
    }

}